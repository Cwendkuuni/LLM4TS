package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NullsConstantProvider;

public final class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstruction() {
        // Test default constructor
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        assertNull(methodProperty.getName());
        assertNull(methodProperty.getType());
        assertNull(methodProperty.getValueDeserializer());
        assertNull(methodProperty.getNullProvider());
        assertNull(methodProperty._annotated);
        assertNull(methodProperty._setter);
        assertFalse(methodProperty.isWritable());
        assertFalse(methodProperty.isReadable());

        // Test constructor with BeanPropertyDefinition
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(propDef, property.getBeanPropertyDefinition());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getValueDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getAnnotated());
        assertTrue(property.isWritable());
        assertTrue(property.isReadable());
    }

    @Test
    public void testMethodPropertyDeserializeAndSet() throws IOException {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        Method method = testClass.getMethod("setTestMethod", String.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);

        when(jsonParser.getValueAsString()).thenReturn("Test Value");
        String testValue = "Test Value";

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, testInstance);

        verify(testInstance).setTestMethod(testValue);
    }

    @Test
    public void testMethodPropertyDeserializeAndReturn() throws IOException {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        Method method = testClass.getMethod("getTestMethod", null);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);

        when(jsonParser.getValueAsString()).thenReturn("Test Value");

        assertEquals(testInstance, methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, testInstance));

        verify(testInstance).getTestMethod();
        verifySetterReturnValue(testInstance, methodProperty, "Test Value");
    }

    @Test
    public void testMethodPropertySet() throws IOException {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        String testValue = "Test Value";

        Method method = testClass.getMethod("setTestMethod", String.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);

        methodProperty.set(testInstance, testValue);

        verify(testInstance).setTestMethod(testValue);
    }

    @Test
    public void testMethodPropertySetAndReturn() throws IOException {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        String testValue = "Test Value";

        Method method = testClass.getMethod("getTestMethod", null);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);

        when(method.invoke(testInstance, testValue)).thenReturn("Return Test Value");

        assertEquals("Return Test Value", methodProperty.setAndReturn(testInstance, testValue));

        verify(testInstance).setTestMethod(testValue);
        verify(testInstance).getTestMethod();
        verify(testInstance).getTestMethod(); // verify getter was called twice to return the new value
    }

    @Test
    public void testMethodPropertySerialization() throws IOException {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        String testValue = "Test Value";

        Method method = testClass.getMethod("setTestMethod", String.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);

        when(method.invoke(testInstance, testValue)).thenReturn(null);

        ObjectWriter objectWriter = mock(ObjectWriter.class);

        methodProperty.serialize(testValue, objectWriter, null, null, null);

        ObjectReader objectReader = mock(ObjectReader.class);
        ObjectNode objectNode = mock(ObjectNode.class);
        when(objectReader.readValue(objectNode)).thenReturn(testValue);

        Object result = methodProperty.deserialize(objectReader, null);

        assertEquals(testValue, result);
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() throws IOException {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        String testValue = "Test Value";

        Method method = testClass.getMethod("setTestMethod", String.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        StringDeserializer stringDeserializer = mock(StringDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.findValueDeserializer(null, null, null)).thenReturn(stringDeserializer);

        MethodProperty methodProperty = new MethodProperty(null, null, typeDeserializer, null, annotatedMethod);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn(testValue);

        methodProperty.deserializeAndSet(jsonParser, null, testInstance);

        verify(testInstance).setTestMethod(testValue);
        verify(stringDeserializer).deserialize(jsonParser, null);
    }

    @Test
    public void testMethodPropertyWithNullProvider() throws IOException {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        Method method = testClass.getMethod("setTestMethod", String.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(null, null, null, nullValueProvider, annotatedMethod);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, testInstance);

        verify(testInstance, never()).setTestMethod(null);
        verifyNoInteractions(nullValueProvider);
    }

    @Test
    public void testMethodPropertyWithNullProviderAndValueDeserializer() throws IOException {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        StringDeserializer stringDeserializer = mock(StringDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        Method method = testClass.getMethod("setTestMethod", String.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(null, null, typeDeserializer, nullValueProvider, annotatedMethod);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, testInstance);

        verify(testInstance, never()).setTestMethod(null);
        verify(nullValueProvider).getNullValue(deserializationContext);
        verify(stringDeserializer).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testMethodPropertyWithAnnotation() {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        Method method = testClass.getMethod("setTestMethod", String.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);
        when(method.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty(name = "testProperty"));

        MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);

        assertEquals("testProperty", methodProperty.getName());
    }

    @Test
    public void testMethodPropertyWithEmptyName() {
        Class<?> testClass = getClass();
        Object testInstance = mock(testClass);
        Method method = testClass.getMethod("setTestMethod", String.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);

        assertEquals("", methodProperty.getName());
    }

    private void verifySetterReturnValue(Object testInstance, MethodProperty methodProperty, String testValue) {
        Method method = ClassUtil.getSetter(methodProperty.getName(), testInstance.getClass());
        Object result;

        try {
            result = method.invoke(testInstance, testValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(testValue, result);
    }
}