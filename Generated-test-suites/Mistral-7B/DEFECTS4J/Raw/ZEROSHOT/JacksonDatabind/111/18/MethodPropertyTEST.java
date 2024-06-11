package com.yourpackage.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;

public class MethodPropertyTest {

    private ObjectInstance instance;
    private ObjectMapper mapper;
    private AnnotatedMethod annotatedMethod;
    private MethodProperty methodProperty;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method setterMethod = Mockito.mock(Method.class);
        methodProperty = new MethodProperty(
                new BeanPropertyDefinition("testProperty", String.class),
                Object.class.getTypeName(), null, Annotations.empty(), annotatedMethod,
                setterMethod, false);
        instance = new ObjectInstance(new Object());
    }

    @Test
    public void testConstructor() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Assert.assertNull(methodProperty.getAnnotated());
        Assert.assertNull(methodProperty._setter);
        Assert.assertFalse(methodProperty._skipNulls);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(annotatedMethod, methodProperty.getAnnotated());
        Assert.assertEquals(setterMethod, methodProperty._setter);
        Assert.assertFalse(methodProperty._skipNulls);
    }

    @Test
    public void testSetter() throws Exception {
        Object value = "testValue";
        when(annotatedMethod.getAnnotated()).thenReturn(annotatedMethod);
        when(setterMethod.invoke(instance, anyObject())).thenReturn(value);

        methodProperty.set(instance, "testValueToSet");
        verify(setterMethod).invoke(instance, "testValueToSet");
        Assert.assertEquals(value, methodProperty.getValue(instance));
    }

    @Test
    public void testSetterWithNullValue() throws Exception {
        Object value = null;

        when(annotatedMethod.getAnnotated()).thenReturn(annotatedMethod);
        when(setterMethod.invoke(instance, anyObject())).thenReturn(value);

        methodProperty.set(instance, "testValueToSet");
        verify(setterMethod).invoke(instance, "testValueToSet");

        methodProperty.set(instance, null);
        verify(setterMethod, never()).invoke(instance, null);
        Assert.assertNull(methodProperty.getValue(instance));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String testValue = "testValue";
        when(annotatedMethod.getAnnotated()).thenReturn(annotatedMethod);

        JsonParser parser = mapper.getFactory().createParser("{\"testProperty\":\"" + testValue + "\"}");
        when(setterMethod.invoke(instance, anyObject())).thenReturn(testValue);

        methodProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
        verify(parser).nextToken();
        verify(parser).nextText();
        verify(parser).nextToken();
        verify(setterMethod).invoke(instance, testValue);
        Assert.assertEquals(testValue, methodProperty.getValue(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String testValue = "testValue";
        when(annotatedMethod.getAnnotated()).thenReturn(annotatedMethod);

        JsonParser parser = mapper.getFactory().createParser("{\"testProperty\":\"" + testValue + "\"}");
        when(setterMethod.invoke(instance, anyObject())).thenReturn(testValue);
        when(setterMethod.invoke(instance, null)).thenReturn(instance);

        Object result = methodProperty.deserializeSetAndReturn(parser, mapper.getDeserializationContext(), instance);
        verify(parser).nextToken();
        verify(parser).nextText();
        verify(parser).nextToken();
        verify(setterMethod).invoke(instance, testValue);
        Assert.assertEquals(instance, result);
        Assert.assertEquals(testValue, methodProperty.getValue(instance));
    }

    @Test
    public void testWithName() {
        String newName = "newName";
        MethodProperty result = methodProperty.withName(new PropertyName(newName));
        Assert.assertEquals(newName, result.getName());
        Assert.assertEquals(methodProperty, result);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> deserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        MethodProperty result = methodProperty.withValueDeserializer(deserializer);
        Assert.assertEquals(deserializer, result._valueDeserializer);
        Assert.assertEquals(methodProperty, result);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        MethodProperty result = methodProperty.withNullProvider(nullValueProvider);
        Assert.assertEquals(nullValueProvider, result._nullProvider);
        Assert.assertEquals(methodProperty, result);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        methodProperty.fixAccess(config);
        verify(annotatedMethod).fixAccess(eq(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)));
    }

    @Test
    public void testGetAnnotation() {
        MyAnnotation annotation = Mockito.mock(MyAnnotation.class);
        when(annotatedMethod.getAnnotation(MyAnnotation.class)).thenReturn(annotation);
        Assert.assertEquals(annotation, methodProperty.getAnnotation(MyAnnotation.class));
    }

    @Test
    public void testGetMember() {
        Assert.assertEquals(annotatedMethod, methodProperty.getMember());
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws Exception {
        String testValue = "testValue";
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        when(annotatedMethod.getAnnotated()).thenReturn(annotatedMethod);

        JsonParser parser = mapper.getFactory().createParser("{\"testProperty\":\"" + testValue + "\"}");
        when(setterMethod.invoke(instance, anyObject())).thenReturn(testValue);

        methodProperty = new MethodProperty(methodProperty, null, typeDeserializer);

        methodProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
        verify(parser).nextToken();
        verify(parser).nextText();
        verify(parser).nextToken();
        verify(setterMethod).invoke(instance, testValue);
        Assert.assertEquals(testValue, methodProperty.getValue(instance));
    }

    @Test
    public void testDeserializeSetAndReturnWithTypeDeserializer() throws Exception {
        String testValue = "testValue";
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        when(annotatedMethod.getAnnotated()).thenReturn(annotatedMethod);

        JsonParser parser = mapper.getFactory().createParser("{\"testProperty\":\"" + testValue + "\"}");
        when(setterMethod.invoke(instance, anyObject())).thenReturn(testValue);
        when(setterMethod.invoke(instance, null)).thenReturn(instance);

        Object result = methodProperty.deserializeSetAndReturn(parser, mapper.getDeserializationContext(), instance);
        verify(parser).nextToken();
        verify(parser).nextText();
        verify(parser).nextToken();
        verify(setterMethod).invoke(instance, testValue);
        Assert.assertEquals(instance, result);
        Assert.assertEquals(testValue, methodProperty.getValue(instance));
    }

    @Test
    public void testFixAccessWithAccessDenied() throws Exception {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        methodProperty.fixAccess(config);
        verify(annotatedMethod).fixAccess(eq(false));
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetterWithAccessDenied() throws Exception {
        when(setterMethod.isAccessible()).thenReturn(false);
        methodProperty.set(instance, "testValueToSet");
    }

    @Test
    public void testToString() {
        String expected = "MethodProperty(name=testProperty, getter=null, setter=public java.lang.String com.yourpackage.Object.setTestProperty(java.lang.String), isWritable=true, isReadable=true, valueDeserializer=null, nullProvider=null, annotations=[], type=class java.lang.Object, beanPropertyDefinition=BeanPropertyDefinition(name=testProperty, typeClass=java.lang.String, rawType=class java.lang.Object))";
        Assert.assertEquals(expected, methodProperty.toString());
    }
}

class ObjectInstance {
    private Object object;

    public ObjectInstance(Object object) {
        this.object = object;
    }

    public Object getInstance() {
        return object;
    }
}

class MyAnnotation {}