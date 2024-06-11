package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.SmartIdentifier;
import com.fasterxml.jackson.databind.util.TypeUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class CreatorPropertyTest {

    private static final String TEST_CLASS_NAME = "com.test.MyTestClass";
    private static final String TEST_PROPERTY_NAME = "testProperty";
    private static final String TEST_ANNOTATION_NAME = "TestAnnotation";
    private static final String TEST_ANNOTATION_VALUE = "testValue";

    private Object testInstance;
    private CreatorProperty testProperty;
    private AnnotatedParameter annotatedParameter;
    private TypeDeserializer typeDeserializer;
    private JsonDeserializer<Object> jsonDeserializer;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        testInstance = mock(Object.class);
        annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getName()).thenReturn(new PropertyName(TEST_PROPERTY_NAME));
        when(annotatedParameter.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(annotatedParameter.getRawType()).thenReturn(ClassUtil.getSimpleType(TEST_CLASS_NAME));
        when(annotatedParameter.getDeclaringClass()).thenReturn(ClassUtil.getSimpleType(TEST_CLASS_NAME));
        when(annotatedParameter.hasParameterName()).thenReturn(true);
        when(annotatedParameter.getParameterName()).thenReturn("testParameter");

        typeDeserializer = mock(TypeDeserializer.class);
        jsonDeserializer = mock(JsonDeserializer.class);

        testProperty = new CreatorProperty(
                new PropertyName(TEST_PROPERTY_NAME),
                TypeFactory.defaultInstance().constructType(Object.class),
                new PropertyName(TEST_PROPERTY_NAME),
                typeDeserializer,
                Annotations.empty(),
                annotatedParameter,
                0,
                null,
                PropertyMetadata.empty()
        );
    }

    @Test
    public void testFindInjectableValue() throws JsonMappingException {
        when(annotatedParameter.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty(TEST_ANNOTATION_NAME, TEST_ANNOTATION_VALUE));
        when(annotatedParameter.getMember().getName()).thenReturn(new PropertyName("testMember"));

        DeserializationContext context = mock(DeserializationContext.class);

        Object injectedValue = testProperty.findInjectableValue(context, testInstance);
        assertEquals(TEST_ANNOTATION_VALUE, injectedValue);
    }

    @Test
    public void testFindInjectableValue_NoAnnotation() throws JsonMappingException {
        when(annotatedParameter.getAnnotation(JsonProperty.class)).thenReturn(null);

        DeserializationContext context = mock(DeserializationContext.class);

        assertNull(testProperty.findInjectableValue(context, testInstance));
    }

    @Test
    public void testInject() throws IOException {
        Object injectedValue = "injectedValue";
        when(context.findInjectableValue(_eq(new ObjectId(SmartIdentifier.fromString(TEST_ANNOTATION_NAME))), ArgumentMatchers.<CreatorProperty>any(), ArgumentMatchers.<Object>any())).thenReturn(injectedValue);

        testProperty.inject(mock(DeserializationContext.class), testInstance);

        Object propertyValue = testProperty.getValue(testInstance);
        assertEquals(injectedValue, propertyValue);
    }

    @Test
    public void testInject_NullInjectedValue() throws IOException {
        when(context.findInjectableValue(_eq(new ObjectId(SmartIdentifier.fromString(TEST_ANNOTATION_NAME))), ArgumentMatchers.<CreatorProperty>any(), ArgumentMatchers.<Object>any())).thenReturn(null);

        testProperty.inject(mock(DeserializationContext.class), testInstance);

        assertNull(testProperty.getValue(testInstance));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String jsonString = "{\"testProperty\": \"testValue\"}";
        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsString()).thenReturn(jsonString);

        Object expectedValue = "testValue";
        when(jsonDeserializer.deserialize(p, mock(DeserializationContext.class))).thenReturn(expectedValue);

        testProperty.setValueDeserializer(jsonDeserializer);

        testProperty.deserializeAndSet(p, mock(DeserializationContext.class), testInstance);

        Object propertyValue = testProperty.getValue(testInstance);
        assertEquals(expectedValue, propertyValue);
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        String jsonString = "{\"testProperty\": null}";
        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsString()).thenReturn(jsonString);

        JsonDeserializer<Object> nullValueDeserializer = mock(JsonDeserializer.class);
        when(jsonDeserializer.deserialize(p, mock(DeserializationContext.class))).thenReturn(null);
        when(jsonDeserializer.getNullValueProvider()).thenReturn(nullValueDeserializer);

        testProperty.setNullValueProvider(nullValueDeserializer);
        testProperty.setValueDeserializer(jsonDeserializer);

        testProperty.deserializeAndSet(p, mock(DeserializationContext.class), testInstance);

        assertNull(testProperty.getValue(testInstance));
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeAndSet_MissingSetter() throws IOException {
        testProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), testInstance);
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();

        testProperty.set(testInstance, value);

        Object propertyValue = testProperty.getValue(testInstance);
        assertEquals(value, propertyValue);
    }

    @Test
    public void testSet_NullValue() throws IOException {
        testProperty.set(testInstance, null);

        Object propertyValue = testProperty.getValue(testInstance);
        assertNull(propertyValue);
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'testProperty'; inject id 'null']", testProperty.toString());
    }

    @Test
    public void testToString_WithAnnotation() {
        when(annotatedParameter.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty(TEST_ANNOTATION_NAME, TEST_ANNOTATION_VALUE));

        assertEquals("[creator property, name 'testProperty'; inject id 'testAnnotation']", testProperty.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValue_NullInjectedValueId() throws JsonMappingException {
        testProperty.findInjectableValue(mock(DeserializationContext.class), testInstance);
    }

    @Test(expected = IOException.class)
    public void testInject_NullContext() throws IOException {
        testProperty.inject(null, testInstance);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeAndSet_MalformedJson() throws IOException, JsonProcessingException {
        String jsonString = "{\"testProperty\": \"testValue\"";
        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsString()).thenReturn(jsonString);

        testProperty.setValueDeserializer(jsonDeserializer);

        testProperty.deserializeAndSet(p, mock(DeserializationContext.class), testInstance);
    }
}