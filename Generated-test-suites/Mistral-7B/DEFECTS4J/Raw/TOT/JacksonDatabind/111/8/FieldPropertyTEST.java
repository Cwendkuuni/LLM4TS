import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FieldPropertyTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;
    private Object testObject;
    private static final String TEST_PROPERTY = "testProperty";
    private static final String TEST_VALUE = "testValue";
    private static final String TEST_NULL_VALUE = null;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        jsonFactory = mapper.getFactory();
        testObject = new TestClass();
        Field testField = TestClass.class.getDeclaredField(TEST_PROPERTY);
        testField.setAccessible(true);
    }

    @After
    public void tearDown() throws Exception {
        mapper = null;
        jsonFactory = null;
        testObject = null;
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(TEST_PROPERTY, TEST_VALUE);

        JsonParser parser = jsonFactory.createParser(node);
        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", TEST_PROPERTY, TestClass.class);

        fieldProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject);

        assertEquals(TEST_VALUE, testObject.getClass().getField(TEST_PROPERTY).get(testObject));
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        ObjectNode node = mapper.createObjectNode();

        JsonParser parser = jsonFactory.createParser(node);

        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", TEST_PROPERTY, TestClass.class);

        fieldProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject);

        if (!mapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)) {
            assertNull(testObject.getClass().getField(TEST_PROPERTY).get(testObject));
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSet_InvalidValue() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(TEST_PROPERTY, new Object());

        JsonParser parser = jsonFactory.createParser(node);
        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", TEST_PROPERTY, TestClass.class);

        fieldProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(TEST_PROPERTY, TEST_VALUE);

        JsonParser parser = jsonFactory.createParser(node);
        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", TEST_PROPERTY, TestClass.class);

        Object result = fieldProperty.deserializeSetAndReturn(parser, mapper.getDeserializationContext(), testObject);

        assertEquals(testObject, result);
        assertEquals(TEST_VALUE, testObject.getClass().getField(TEST_PROPERTY).get(testObject));
    }

    @Test
    public void testDeserializeSetAndReturn_NullValue() throws IOException {
        ObjectNode node = mapper.createObjectNode();

        JsonParser parser = jsonFactory.createParser(node);
        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", TEST_PROPERTY, TestClass.class);

        Object result = fieldProperty.deserializeSetAndReturn(parser, mapper.getDeserializationContext(), testObject);

        assertEquals(testObject, result);

        if (!mapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)) {
            assertNull(testObject.getClass().getField(TEST_PROPERTY).get(testObject));
        }
    }

    @Test
    public void testSet() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(TEST_PROPERTY, TEST_VALUE);

        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", TEST_PROPERTY, TestClass.class);

        JsonParser parser = jsonFactory.createParser(node);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);

        fieldProperty.set(testObject, parser);

        assertEquals(TEST_VALUE, testObject.getClass().getField(TEST_PROPERTY).get(testObject));
    }

    @Test
    public void testSet_NullValue() throws IOException {
        ObjectNode node = mapper.createObjectNode();

        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", TEST_PROPERTY, TestClass.class);

        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);

        fieldProperty.set(testObject, null);

        if (!mapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)) {
            assertNull(testObject.getClass().getField(TEST_PROPERTY).get(testObject));
        }
    }

    @Test
    public void testSet_InvalidValue() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(TEST_PROPERTY, new Object());

        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", TEST_PROPERTY, TestClass.class);

        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);

        fieldProperty.set(testObject, jsonFactory.createParser(node));

        assertNotNull(testObject.getClass().getField(TEST_PROPERTY).get(testObject));
    }

    @Test
    public void testSetValueDeserializer() throws IOException {
        TestClass testClass = new TestClass();
        ObjectNode node = mapper.createObjectNode();

        node.put("testProperty", mapper.readValueAs(String.class, "\"TestValue\""));

        ObjectMapper testMapper = new ObjectMapper();
        testMapper.setPropertyNameGenerator(PropertyNamingStrategy.DUMMY);

        TestValueDeserializer testValueDeserializer = new TestValueDeserializer();

        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", "testProperty", TestClass.class);

        JsonParser parser = jsonFactory.createParser(node);

        fieldProperty.withValueDeserializer(testValueDeserializer);

        testObject = testMapper.readValue(node.toString(), TestClass.class);

        assertEquals("TestValue", testObject.getTestProperty());
    }

    @Test
    public void testSetNullProvider() throws IOException {
        TestClass testClass = new TestClass();
        ObjectNode node = mapper.createObjectNode();

        node.put("testProperty", null);

        ObjectMapper testMapper = new ObjectMapper();
        testMapper.setPropertyNameGenerator(PropertyNamingStrategy.DUMMY);

        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return new TestClass();
            }
        };

        FieldProperty fieldProperty = mapper.findDeserializedProperty("testObject", "testProperty", TestClass.class);

        JsonParser parser = jsonFactory.createParser(node);

        fieldProperty.withNullProvider(nullValueProvider);

        testObject = testMapper.readValue(node.toString(), TestClass.class);

        assertNotNull(testObject.getClass().getField(TEST_PROPERTY).get(testObject));
    }

    private static class TestClass {
        private String testProperty;

        public String getTestProperty() {
            return testProperty;
        }

        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }
    }

    private static class TestValueDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken token = p.getCurrentToken();
            switch (token) {
                case VALUE_STRING:
                    return p.getValueAsString();
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    return p.getValueAsString();
                case VALUE_TRUE:
                case VALUE_FALSE:
                    return "";
                case START_OBJECT:
                    StringBuilder builder = new StringBuilder();
                    while (!p.isClosed()) {
                        builder.append(p.nextTextValue());
                        p.nextToken();
                    }
                    return builder.toString();
                default:
                    throw ctxt.mappingException("TestValueDeserializer can only deserialize String");
            }
        }
    }
}