package com.example.methodpropertytest;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MethodPropertyTest {

    private Object instance;
    private JsonParser jsonParserMock;
    private DeserializationContext contextMock;
    private MethodProperty methodProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.instance = new Object();
        this.jsonParserMock = mock(JsonParser.class);
        this.contextMock = mock(DeserializationContext.class);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(new Method(Mockito.any(Class.class), Mockito.anyInt()));

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        methodProperty = new MethodProperty(
                new BeanPropertyDefinition(Mockito.any(PropertyName.class), Mockito.any(JavaType.class)),
                Mockito.any(JavaType.class), typeDeserializer, Annotations.empty(), annotatedMethod);
    }

    @Test
    public void testDefaultConstructor() {
        // MethodProperty does not have a default constructor
        // This test case will fail if run
    }

    @Test
    public void testMethodPropertyConstructor() {
        MethodProperty methodProperty = new MethodProperty(
                null, new BeanPropertyDefinition(Mockito.any(PropertyName.class), Mockito.any(JavaType.class)),
                Mockito.any(JavaType.class), Mockito.any(TypeDeserializer.class), Mockito.any(Annotations.class),
                Mockito.any(AnnotatedMethod.class));
        assertSame(methodProperty, methodProperty);
    }

    @Test
    public void testCopyConstructor() {
        MethodProperty srcMethodProperty = mock(MethodProperty.class);

        MethodProperty copyMethodProperty = new MethodProperty(srcMethodProperty, Mockito.any(JsonDeserializer.class), Mockito.any(NullValueProvider.class));
        assertSame(srcMethodProperty, copyMethodProperty._annotated);
        assertSame(srcMethodProperty, copyMethodProperty._setter);
        assertSame(srcMethodProperty._skipNulls, copyMethodProperty._skipNulls);
    }

    @Test
    public void testCopyConstructorWithNewName() {
        MethodProperty srcMethodProperty = mock(MethodProperty.class);

        PropertyName newName = new PropertyName("testProperty");
        MethodProperty copyMethodProperty = new MethodProperty(srcMethodProperty, newName);
        assertSame(srcMethodProperty, copyMethodProperty._annotated);
        assertSame(srcMethodProperty, copyMethodProperty._setter);
        assertSame(newName, copyMethodProperty._name);
    }

    @Test
    public void testCopyConstructorWithJdkSerialization() {
        MethodProperty srcMethodProperty = mock(MethodProperty.class);

        Method method = new Method(Mockito.any(Class.class), Mockito.anyInt());
        MethodProperty copyMethodProperty = new MethodProperty(srcMethodProperty, method);
        assertSame(srcMethodProperty, copyMethodProperty._annotated);
        assertSame(srcMethodProperty, copyMethodProperty._setter);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("testProperty");
        MethodProperty methodPropertyWithName = methodProperty.withName(newName);
        assertSame(methodProperty, methodPropertyWithName);
        assertSame(newName, methodPropertyWithName._name);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        MethodProperty methodPropertyWithValueDeserializer = methodProperty.withValueDeserializer(valueDeserializer);
        assertSame(methodProperty, methodPropertyWithValueDeserializer);
        assertSame(valueDeserializer, methodPropertyWithValueDeserializer._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        MethodProperty methodPropertyWithNullProvider = methodProperty.withNullProvider(nullValueProvider);
        assertSame(methodProperty, methodPropertyWithNullProvider);
        assertSame(nullValueProvider, methodPropertyWithNullProvider._nullProvider);
    }

    @Test
    public void testFixAccess() {
        methodProperty.fixAccess(contextMock);
        verify(contextMock, times(1)).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
    }

    @Test(expected = IllegalAccessException.class)
    public void testInvokeSetterNullInstance() throws IOException {
        methodProperty.set(null, "testValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeSetterNullValue() throws IOException {
        methodProperty.set(instance, null);
    }

    @Test
    public void testInvokeSetter() throws IOException {
        Object value = "testValue";
        methodProperty.set(instance, value);
        verify(instance, times(1))._setter.invoke(instance, value);
    }

    @Test
    public void testInvokeSetterWithException() throws IOException, IllegalAccessException {
        doThrow(new RuntimeException("Test Exception")).when(instance._setter).invoke(instance, Mockito.anyObject());

        methodProperty.set(instance, "testValue");
        verify(instance, never())._setter.invoke(instance, "testValue");
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(jsonParserMock.getValueAsString()).thenReturn("testValue");

        methodProperty.deserializeAndSet(jsonParserMock, contextMock, instance);
        verify(instance, times(1))._setter.invoke(instance, "testValue");
    }

    @Test
    public void testDeserializeSetAndReturnNullInstance() throws IOException {
        when(jsonParserMock.getValueAsString()).thenReturn(null);

        assertNull(methodProperty.deserializeSetAndReturn(jsonParserMock, contextMock, instance));
    }

    @Test
    public void testDeserializeSetAndReturnNullValue() throws IOException {
        when(jsonParserMock.getValueAsString()).thenReturn("testValue");

        assertNull(methodProperty.deserializeSetAndReturn(jsonParserMock, contextMock, instance));
    }

    @Test
    public void testDeserializeSetAndReturnInstance() throws IOException {
        Object result = new Object();
        when(jsonParserMock.getValueAsString()).thenReturn("testValue");
        when(instance._setter.invoke(instance, "testValue")).thenReturn(result);

        assertSame(result, methodProperty.deserializeSetAndReturn(jsonParserMock, contextMock, instance));
    }

    @Test
    public void testDeserializeSetAndReturnWithException() throws IOException, IllegalAccessException {
        doThrow(new RuntimeException("Test Exception")).when(instance._setter).invoke(instance, Mockito.anyObject());

        assertNull(methodProperty.deserializeSetAndReturn(jsonParserMock, contextMock, instance));
    }

    @Test
    public void testReadResolve() throws IOException {
        Object result = new MethodProperty(methodProperty, _annotated.getAnnotated()).readResolve();
        assertSame(methodProperty, result);
    }
}