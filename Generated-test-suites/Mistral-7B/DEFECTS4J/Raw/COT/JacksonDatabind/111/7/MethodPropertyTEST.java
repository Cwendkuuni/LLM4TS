package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for MethodProperty
 */
public class MethodPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);

        propDef = new BeanPropertyDefinition(new StandardPropertyMetadata(null), null, null);
        type = Object.class.getType();
        typeDeser = null;
        contextAnnotations = new Annotations();
        method = new AnnotatedMethod(MethodPropertyTest.class.getDeclaredMethod("testMethod"), MethodPropertyTest.class);

        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
    }

    @Test
    public void testWithName() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        PropertyName newName = new PropertyName("newName");

        MethodProperty newMethodProperty = methodProperty.withName(newName);
        assertEquals(newName, newMethodProperty.getName());
        assertEquals(methodProperty, newMethodProperty.getBackingBean());
    }

    @Test
    public void testWithValueDeserializer() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        JsonDeserializer<?> deserializer = null;

        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, newMethodProperty.getValueDeserializer());
        assertEquals(methodProperty, newMethodProperty.getBackingBean());
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        NullValueProvider nva = null;

        MethodProperty newMethodProperty = methodProperty.withNullProvider(nva);
        assertEquals(nva, newMethodProperty.getNullProvider());
        assertEquals(methodProperty, newMethodProperty.getBackingBean());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        Object value = null;
        if (MethodProperty.class.isInstance(parser)) {
            parser = (JsonParser) MethodProperty.class.newInstance();
            parser.setValue(JsonToken.VALUE_NULL);
        } else {
            parser = createMock(JsonParser.class);
            when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
            when(parser.getValueAsString()).thenReturn(null);
        }

        ctxt = createMock(DeserializationContext.class);

        methodProperty.deserializeAndSet(parser, ctxt, instance);
        assertNull(instance);

        parser.reset();
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(parser.getValueAsString()).thenReturn("value");

        value = "value";
        when(methodProperty.getValueDeserializer()).thenReturn(createMock(JsonDeserializer.class));

        methodProperty.deserializeAndSet(parser, ctxt, instance);
        assertEquals(value, instance);

        verify(parser).getValueAsString();
        verifyNoMoreInteractions(parser);
        verify(ctxt).handleUnknownProperty(_eq(methodProperty.getName()), anyObject());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        Object value = null;
        if (MethodProperty.class.isInstance(parser)) {
            parser = (JsonParser) MethodProperty.class.newInstance();
            parser.setValue(JsonToken.VALUE_NULL);
        } else {
            parser = createMock(JsonParser.class);
            when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
            when(parser.getValueAsString()).thenReturn(null);
        }

        ctxt = createMock(DeserializationContext.class);

        Object result = methodProperty.deserializeSetAndReturn(parser, ctxt, instance);
        assertNull(result);
        assertNull(instance);

        parser.reset();
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(parser.getValueAsString()).thenReturn("value");

        value = "value";
        when(methodProperty.getValueDeserializer()).thenReturn(createMock(JsonDeserializer.class));

        result = methodProperty.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals(value, result);
        assertEquals(value, instance);

        verify(parser).getValueAsString();
        verify(parser).nextToken();
        verifyNoMoreInteractions(parser);
        verify(ctxt).handleUnknownProperty(_eq(methodProperty.getName()), anyObject());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

        Object value = new Object();
        methodProperty.set(instance, value);
        assertEquals(value, instance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

        Object value = new Object();
        Object result = methodProperty.setAndReturn(instance, value);
        assertEquals(value, result);
        assertEquals(value, instance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithException() throws IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        parser = createMock(JsonParser.class);
        when(parser.getValueAsString()).thenThrow(new IOException("Exception"));

        ctxt = createMock(DeserializationContext.class);

        methodProperty.deserializeAndSet(parser, ctxt, instance);
        fail("Should throw IOException");

        verify(parser).getValueAsString();
        verify(parser).nextToken();
        verify(ctxt).handleUnknownProperty(_eq(methodProperty.getName()), anyObject());
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithException() throws IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        parser = createMock(JsonParser.class);
        when(parser.getValueAsString()).thenThrow(new IOException("Exception"));

        ctxt = createMock(DeserializationContext.class);

        methodProperty.deserializeSetAndReturn(parser, ctxt, instance);
        fail("Should throw IOException");

        verify(parser).getValueAsString();
        verify(parser).nextToken();
        verifyNoMoreInteractions(parser);
        verify(ctxt).handleUnknownProperty(_eq(methodProperty.getName()), anyObject());
    }

    @Test
    public void testFixAccess() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        DeserializationConfig config = null;

        methodProperty.fixAccess(config);
        assertNull(config);
    }

    @Test
    public void testGetAnnotation() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        JsonSetter jsonSetter = new JsonSetter();

        Annotation annotation = jsonSetter;
        when(methodProperty.getAnnotation(JsonSetter.class)).thenReturn(annotation);

        assertEquals(annotation, methodProperty.getAnnotation(JsonSetter.class));
    }

    @Test
    public void testGetMember() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

        AnnotatedMember member = methodProperty;
        assertEquals(member, methodProperty.getMember());
    }

    @Test(expected = IllegalAccessException.class)
    public void testInvokeSetterWithNullInstance() throws IllegalAccessException, InvocationTargetException {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Object value = new Object();

        methodProperty.invoke(null, value);
        fail("Should throw IllegalAccessException");
    }

    @Test(expected = IllegalAccessException.class)
    public void testInvokeSetterWithNullValue() throws IllegalAccessException, InvocationTargetException {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Object instance = new Object();

        methodProperty.invoke(instance, null);
        fail("Should throw IllegalAccessException");
    }

    @Test
    public void testInvokeSetter() throws IllegalAccessException, InvocationTargetException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethod", new Class[]{Object.class, Integer.class});

        Object value = new Object();
        methodProperty.invoke(instance, value);
        assertEquals(value, instance);
        assertEquals(value, method.invoke(instance, value));
    }

    @Test
    public void testInvokeSetterWithAnnotation() throws IllegalAccessException, InvocationTargetException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethodWithAnnotation", new Class[]{Object.class, Integer.class});
        JsonSetter jsonSetter = new JsonSetter();

        methodProperty.setAnnotation(JsonSetter.class, jsonSetter);
        Object value = new Integer(1);

        methodProperty.invoke(instance, value);
        assertEquals(value, instance);
        assertEquals(value, method.invoke(instance, value));
        assertEquals(jsonSetter, methodProperty.getAnnotation(JsonSetter.class));
    }

    @Test
    public void testInvokeSetterWithValueDeserializer() throws IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethodWithValueDeserializer", new Class[]{Object.class, String.class});
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"value\"");

        methodProperty.setValueDeserializer(parser.getValueDeserializer("value"));
        methodProperty.invoke(instance, parser);
        assertEquals(parser.getValueAsString(), instance);
    }

    @Test
    public void testInvokeSetterWithNullValueDeserializer() throws IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethodWithValueDeserializer", new Class[]{Object.class, String.class});

        methodProperty.invoke(instance, null);
        assertNull(instance);
    }

    @Test(expected = NullPointerException.class)
    public void testInvokeSetterWithNullMethod() throws IllegalAccessException, InvocationTargetException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

        methodProperty.invoke(instance, null);
        fail("Should throw NullPointerException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeSetterWithWrongArgumentType() throws IllegalAccessException, InvocationTargetException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethodWithValueDeserializer", new Class[]{Object.class, String.class});

        methodProperty.invoke(instance, new Object());
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testInvokeSetterWithAnnotationAndValueDeserializer() throws IllegalAccessException, InvocationTargetException, IOException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethodWithAnnotationAndValueDeserializer", new Class[]{Object.class, String.class});
        JsonSetter jsonSetter = new JsonSetter();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"value\"");

        methodProperty.setAnnotation(JsonSetter.class, jsonSetter);
        methodProperty.setValueDeserializer(parser.getValueDeserializer("value"));
        Object value = new Integer(1);

        methodProperty.invoke(instance, value);
        assertEquals(value, instance);
        assertEquals(value, method.invoke(instance, value));
        assertEquals(jsonSetter, methodProperty.getAnnotation(JsonSetter.class));
    }

    @Test
    public void testInvokeSetterWithNullValueDeserializerAndAnnotation() throws IllegalAccessException, InvocationTargetException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethodWithAnnotationAndValueDeserializer", new Class[]{Object.class, String.class});
        JsonSetter jsonSetter = new JsonSetter();

        methodProperty.setAnnotation(JsonSetter.class, jsonSetter);
        methodProperty.invoke(instance, null);
        assertNull(instance);
    }

    @Test
    public void testInvokeSetterWithNullValueDeserializerAndWrongArgumentType() throws IllegalAccessException, InvocationTargetException {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethodWithAnnotationAndValueDeserializer", new Class[]{Object.class, String.class});
        JsonSetter jsonSetter = new JsonSetter();

        methodProperty.setAnnotation(JsonSetter.class, jsonSetter);
        methodProperty.invoke(instance, new Object());
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testReadResolve() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Method method = new Method(MethodPropertyTest.class, "testMethod", new Class[]{Object.class, Integer.class});

        MethodProperty newMethodProperty = (MethodProperty) methodProperty.readResolve();
        assertEquals(method, newMethodProperty._setter);
    }

    private JsonParser createMock(Class<?> clazz) {
        return Mockito.mock(JsonParser.class);
    }

    private DeserializationContext createMock(Class<?> clazz) {
        return Mockito.mock(DeserializationContext.class);
    }
}