package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.utils.Annotations;

public class FieldPropertyTest {

    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A createMockAnnotation(Class<A> annotationType) {
        return mock(annotationType);
    }

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    @SuppressWarnings("unchecked")
    private static <T> T newMockedInstance(Class<T> clazz) {
        return mock(clazz);
    }

    @SuppressWarnings("rawtypes")
    private static JsonDeserializer createMockJsonDeserializer() {
        return mock(JsonDeserializer.class);
    }

    @SuppressWarnings("rawtypes")
    private static NullValueProvider createMockNullValueProvider() {
        return mock(NullValueProvider.class);
    }

    @SuppressWarnings("unchecked")
    private static TypeDeserializer createMockTypeDeserializer() {
        return mock(TypeDeserializer.class);
    }

    @SuppressWarnings("unchecked")
    private static AnnotationIntrospector createMockAnnotationIntrospector() {
        return mock(AnnotationIntrospector.class);
    }

    @SuppressWarnings("unchecked")
    private static AnnotatedMember createMockAnnotatedMember() {
        return mock(AnnotatedMember.class);
    }

    @SuppressWarnings("unchecked")
    private static AnnotatedField createMockAnnotatedField() {
        return mock(AnnotatedField.class);
    }

    @SuppressWarnings("unchecked")
    private static BeanPropertyDefinition createMockBeanPropertyDefinition() {
        return mock(BeanPropertyDefinition.class);
    }

    @SuppressWarnings("unchecked")
    private static PropertyName createMockPropertyName() {
        return mock(PropertyName.class);
    }

    @SuppressWarnings("unchecked")
    private static FieldProperty propertyUnderTest;

    @SuppressWarnings("unchecked")
    private static Field testField;

    @SuppressWarnings("unchecked")
    private static JsonParser testJsonParser;

    @SuppressWarnings("unchecked")
    private static DeserializationContext testDeserializationContext;

    @SuppressWarnings("unchecked")
    private static Object testInstance;

    @SuppressWarnings("unchecked")
    private static Annotation[] testAnnotations;

    @SuppressWarnings("unchecked")
    private static AnnotationIntrospector testAnnotationIntrospector;

    @SuppressWarnings("unchecked")
    private static AnnotatedField testAnnotatedField;

    @SuppressWarnings("unchecked")
    private static BeanPropertyDefinition testBeanPropertyDefinition;

    @SuppressWarnings("unchecked")
    private static JsonDeserializer testJsonDeserializer;

    @SuppressWarnings("unchecked")
    private static TypeDeserializer testTypeDeserializer;

    @SuppressWarnings("unchecked")
    private static NullValueProvider testNullValueProvider;

    @SuppressWarnings("unchecked")
    private static TypeDeserializer testValueTypeDeserializer;

    @SuppressWarnings("rawtypes")
    @Before
    public void setUp() throws Exception {
        testJsonParser = mock(JsonParser.class);
        testDeserializationContext = mock(DeserializationContext.class);
        testInstance = newInstance(Object.class);
        testAnnotations = new Object[0];
        testAnnotationIntrospector = newMockedInstance(AnnotationIntrospector.class);
        testAnnotatedField = newMockedInstance(AnnotatedField.class);
        testBeanPropertyDefinition = newMockedInstance(BeanPropertyDefinition.class);
        testJsonDeserializer = createMockJsonDeserializer();
        testTypeDeserializer = createMockTypeDeserializer();
        testNullValueProvider = createMockNullValueProvider();
        testValueTypeDeserializer = createMockTypeDeserializer();

        when(testAnnotationIntrospector.findAnnotationValues(testInstance.getClass(), JsonProperty.class)).thenReturn(testAnnotations);
        when(testBeanPropertyDefinition.getMember()).thenReturn(testAnnotatedField);
        when(testAnnotatedField.getAnnotated()).thenReturn(testField);

        propertyUnderTest = new FieldProperty(testBeanPropertyDefinition, createMockJavaType(Integer.TYPE), testTypeDeserializer, testAnnotationIntrospector, testAnnotatedField);
    }

    @Test
    public void testConstructor() {
        // Default constructor
        new FieldProperty();

        // Constructor with property definition, type, type deserializer, and context annotations
        new FieldProperty(createMockBeanPropertyDefinition(), createMockJavaType(String.class), testTypeDeserializer, testAnnotationIntrospector, createMockAnnotatedField());

        // Constructor for JDK serialization
        new FieldProperty(propertyUnderTest);
    }

    @Test
    public void testWithName() throws Exception {
        PropertyName newName = createMockPropertyName();
        FieldProperty result = propertyUnderTest.withName(newName);

        assertEquals(propertyUnderTest.getName(), propertyUnderTest.getName());
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        JsonDeserializer deser = createMockJsonDeserializer();
        FieldProperty result = propertyUnderTest.withValueDeserializer(deser);

        assertNotSame(propertyUnderTest.getValueDeserializer(), propertyUnderTest.getValueDeserializer());
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws Exception {
        NullValueProvider nva = createMockNullValueProvider();
        FieldProperty result = propertyUnderTest.withNullProvider(nva);

        assertNotSame(propertyUnderTest._nullProvider, propertyUnderTest._nullProvider);
        assertEquals(nva, result._nullProvider);
    }

    @Test
    public void testFixAccess() throws Exception {
        when(testField.isAccessible()).thenReturn(false);

        propertyUnderTest.fixAccess(testDeserializationContext);

        verify(testField, times(1)).setAccessible(true);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        @JsonProperty(value = "test")
        Annotation testAnnotation = createMockAnnotation(JsonProperty.class);

        when(testAnnotatedField.getAnnotation(JsonProperty.class)).thenReturn(testAnnotation);

        assertEquals(testAnnotation, propertyUnderTest.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(testAnnotatedField, propertyUnderTest.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        int testValue = 42;

        when(testJsonParser.getValueAsInt()).thenReturn(testValue);
        when(testJsonParser.getCurrentName()).thenReturn("test");

        Object instance = testInstance;
        propertyUnderTest.deserializeAndSet(testJsonParser, testDeserializationContext, instance);

        assertEquals(testValue, testField.get(instance));
        verify(testJsonParser, times(1)).nextToken();
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        int testValue = 42;

        when(testJsonParser.getValueAsInt()).thenReturn(testValue);
        when(testJsonParser.getCurrentName()).thenReturn("test");

        Object instance = testInstance;
        Object result = propertyUnderTest.deserializeSetAndReturn(testJsonParser, testDeserializationContext, instance);

        assertEquals(testValue, testField.get(instance));
        assertEquals(instance, result);
        verify(testJsonParser, times(1)).nextToken();
    }

    @Test
    public void testSet() throws Exception {
        int testValue = 42;

        testField.setAccessible(true);
        testField.set(testInstance, testValue);

        propertyUnderTest.set(testInstance, testValue);

        assertEquals(testValue, testField.get(testInstance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        int testValue = 42;

        testField.setAccessible(true);
        Object instance = testInstance;
        Object result = propertyUnderTest.setAndReturn(testInstance, testValue);

        assertEquals(testValue, testField.get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testReadResolve() throws Exception {
        propertyUnderTest = new FieldProperty(propertyUnderTest);

        assertEquals(propertyUnderTest, testField.get(testInstance));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws Exception {
        when(testJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        propertyUnderTest.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);

        fail("Expected IOException but received none");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithUnexpectedToken() throws Exception {
        when(testJsonParser.getValueAsString()).thenReturn("foo");
        when(testJsonParser.getCurrentName()).thenReturn("test");

        propertyUnderTest.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);

        fail("Expected JsonMappingException but received none");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithTypeMismatch() throws Exception {
        when(testJsonParser.getValueAsInt()).thenReturn(42);
        when(testJsonParser.getCurrentName()).thenReturn("test");
        when(testValueDeserializer.deserialize(testJsonParser, testDeserializationContext)).thenThrow(new JsonMappingException("Test error"));

        propertyUnderTest.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);

        fail("Expected JsonMappingException but received none");
    }

    @Test
    public void testDeserializeAndSetWithNullValueSkipped() throws Exception {
        when(testJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(testJsonDeserializer.getNullValue()).thenReturn(null);

        propertyUnderTest.deserializeAndSet(testJsonParser, testDeserializationContext, testInstance);

        assertNull(testField.get(testInstance));
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullValue() throws Exception {
        when(testJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        propertyUnderTest.deserializeSetAndReturn(testJsonParser, testDeserializationContext, testInstance);

        fail("Expected IOException but received none");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeSetAndReturnWithUnexpectedToken() throws Exception {
        when(testJsonParser.getValueAsString()).thenReturn("foo");
        when(testJsonParser.getCurrentName()).thenReturn("test");

        propertyUnderTest.deserializeSetAndReturn(testJsonParser, testDeserializationContext, testInstance);

        fail("Expected JsonMappingException but received none");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeSetAndReturnWithTypeMismatch() throws Exception {
        when(testJsonParser.getValueAsInt()).thenReturn(42);
        when(testJsonParser.getCurrentName()).thenReturn("test");
        when(testValueDeserializer.deserializeWithType(testJsonParser, testDeserializationContext, testValueTypeDeserializer)).thenThrow(new JsonMappingException("Test error"));

        propertyUnderTest.deserializeSetAndReturn(testJsonParser, testDeserializationContext, testInstance);

        fail("Expected JsonMappingException but received none");
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValueSkipped() throws Exception {
        when(testJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(testJsonDeserializer.getNullValue()).thenReturn(null);

        propertyUnderTest.deserializeSetAndReturn(testJsonParser, testDeserializationContext, testInstance);

        assertEquals(testInstance, testInstance);
    }

    @Test
    public void testSetWithNullValue() throws Exception {
        testField.setAccessible(true);
        testField.set(testInstance, null);

        propertyUnderTest.set(testInstance, null);

        assertNull(testField.get(testInstance));
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnWithNullValue() throws Exception {
        testField.setAccessible(true);
        Object instance = testInstance;
        Object result = propertyUnderTest.setAndReturn(testInstance, null);

        assertNull(testField.get(instance));
        fail("Expected IOException but received none");
    }

    @Test
    public void testToString() {
        String expected = "FieldProperty[name='test', getter=null, setter=class com.fasterxml.jackson.databind.deser.impl.FieldProperty$1, field=class java.lang.Object, type=class java.lang.Integer]";

        assertEquals(expected, propertyUnderTest.toString());
    }

    private static JavaType createMockJavaType(Class clazz) {
        return mock(JavaType.class);
    }
}