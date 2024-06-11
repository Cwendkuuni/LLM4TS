package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;

import static com.fasterxml.jackson.databind.util.TestBase.STANDARD_OBJECT_MAPPER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FieldPropertyTest {

    private static final String TEST_FIELD_NAME = "testField";
    private static final String TEST_CLASS_NAME = "TestClass";
    private static final Class<TestClass> TEST_CLASS = (Class<TestClass>) ClassUtil.loadClassQuietly(TestClass.class.getName());

    @Mock
    private AnnotatedField testAnnotatedField;

    @Mock
    private Field testField;

    @Mock
    private JsonDeserializer<String> testJsonDeserializer;

    @Mock
    private NullValueProvider testNullValueProvider;

    @Mock
    private TypeDeserializer testTypeDeserializer;

    @InjectMocks
    private FieldProperty fieldProperty;

    @Before
    public void setup() {
        when(testAnnotatedField.getAnnotated()).thenReturn(testField);
        when(testField.getName()).thenReturn(TEST_FIELD_NAME);
        when(testField.getType()).thenReturn(String.class);
        when(testField.isAccessible()).thenReturn(true);
        when(testJsonDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("Test Value");
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new JsonMapper();
        ObjectNode testObjectNode = objectMapper.createObjectNode();
        testObjectNode.put(TEST_FIELD_NAME, "Test Value");

        TestClass testInstance = new TestClass();
        fieldProperty = new FieldProperty(null, TEST_FIELD_NAME, null, null, testAnnotatedField);
        fieldProperty.deserializeAndSet(STANDARD_OBJECT_MAPPER.getFactory().createParser(testObjectNode), STANDARD_OBJECT_MAPPER, testInstance);
        assertEquals("Test Value", testInstance.testField);

        ObjectNode testReturnObjectNode = objectMapper.createObjectNode();
        testReturnObjectNode.put(TEST_FIELD_NAME, fieldProperty.deserializeSetAndReturn(STANDARD_OBJECT_MAPPER.getFactory().createParser(testObjectNode), STANDARD_OBJECT_MAPPER, testInstance));
        assertEquals("Test Value", testReturnObjectNode.get(TEST_FIELD_NAME).asText());
    }

    @Test
    public void testNullDeserialization() throws IOException {
        TestClass testInstance = new TestClass();
        fieldProperty = new FieldProperty(null, TEST_FIELD_NAME, null, null, testAnnotatedField);
        fieldProperty.deserializeAndSet(STANDARD_OBJECT_MAPPER.getFactory().createParser("null"), STANDARD_OBJECT_MAPPER, testInstance);
        assertNull(testInstance.testField);
    }

    @Test
    public void testNullValueProvider() throws IOException {
        doReturn(testNullValueProvider).when(fieldProperty)._nullProvider;
        ObjectNode testObjectNode = objectMapper.createObjectNode();
        TestClass testInstance = new TestClass();
        fieldProperty.deserializeAndSet(STANDARD_OBJECT_MAPPER.getFactory().createParser(testObjectNode), STANDARD_OBJECT_MAPPER, testInstance);
        assertNull(testInstance.testField);
    }

    @Test
    public void testSetter() throws IOException {
        TestClass testInstance = new TestClass();
        String testValue = "Test Value";
        fieldProperty = new FieldProperty(null, TEST_FIELD_NAME, null, null, testAnnotatedField);
        fieldProperty.set(testInstance, testValue);
        assertEquals(testValue, testInstance.testField);
    }

    @Test
    public void testSetterAndReturn() throws IOException {
        TestClass testInstance = new TestClass();
        String testValue = "Test Value";
        fieldProperty = new FieldProperty(null, TEST_FIELD_NAME, null, null, testAnnotatedField);
        Object testReturn = fieldProperty.setAndReturn(testInstance, testValue);
        assertEquals(testInstance, testReturn);
        assertEquals(testValue, testInstance.testField);
    }

    @Test
    public void testDeserializationWithTypeHandler() throws IOException {
        ObjectMapper objectMapper = new JsonMapper();
        objectMapper.registerModule(new SimpleModule().addDeserializer(TestClass.class, new JsonDeserializer<TestClass>() {
            @Override
            public TestClass deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException, JsonProcessingException {
                return new TestClass();
            }

            @Override
            public JsonDeserializer<TestClass> createContextual(DeserializationContext context) {
                return new JsonDeserializer<TestClass>(TestClass.class) {
                    @Override
                    public TestClass deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vtd)
                            throws IOException, JsonProcessingException {
                        TestClass testInstance = new TestClass();
                        fieldProperty = new FieldProperty(null, TEST_FIELD_NAME, null, null, testAnnotatedField, testJsonDeserializer, testNullValueProvider);
                        fieldProperty.deserializeAndSet(p, ctxt, testInstance);
                        return testInstance;
                    }
                };
            }
        }));

        ObjectNode testObjectNode = objectMapper.createObjectNode();
        TestClass testInstance = objectMapper.readValue(testObjectNode, TestClass.class);
        assertEquals("Test Value", testInstance.testField);
    }
}

class TestClass {
    @JsonDeserialize(using = FieldProperty.class)
    private String testField;
}