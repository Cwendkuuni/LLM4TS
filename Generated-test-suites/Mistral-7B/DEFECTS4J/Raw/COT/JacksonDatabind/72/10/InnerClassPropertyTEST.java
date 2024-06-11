package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class InnerClassPropertyTest {

    private static final Class<?> INNER_CLASS = InnerClass.class;

    private Object bean;
    private SettableBeanProperty delegate;
    private InnerClassProperty innerClassProperty;
    private JsonDeserializer<?> valueDeserializer;
    private JsonDeserializer<Object> valueTypeDeserializer;
    private Object value;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        valueDeserializer = mock(JsonDeserializer.class);
        valueTypeDeserializer = mock(JsonDeserializer.class);

        JsonFactory jsonFactory = new JsonFactory();

        AnnotatedConstructor constructor = mock(AnnotatedConstructor.class);
        when(constructor.getAnnotated()).thenReturn(mock(Annotated.class));
        when(constructor.getDeclaringClass()).thenReturn(jsonFactory.getConstructors().nextValue().getDeclaringClass());

        innerClassProperty = new InnerClassProperty(new SettableBeanProperty(mock(BeanPropertyDefinition.class)), constructor);
        innerClassProperty.setDelegate(delegate = mock(SettableBeanProperty.class));

        delegate.setValueDeserializer(valueDeserializer);
        innerClassProperty.setValueDeserializer(valueDeserializer);

        innerClassProperty.setName(new PropertyName("testProperty"));

        bean = new Object();
    }

    @Test
    public void testInnerClassPropertyConstructor() {
        assertEquals("InnerClassProperty", innerClassProperty.getClass().getSimpleName());
        assertEquals("testProperty", innerClassProperty.getName().asString());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String jsonValue = "{}";
        when(jsonParser.getValueAsString()).thenReturn(jsonValue);

        when(valueDeserializer.deserialize(jsonParser, context, eq(null))).thenReturn(value = new Object());

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        verify(delegate).set(bean, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String jsonValue = "{}";
        when(jsonParser.getValueAsString()).thenReturn(jsonValue);

        when(valueDeserializer.deserialize(jsonParser, context, eq(null))).thenReturn(value = new Object());

        Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, context, bean);

        verify(delegate).set(bean, value);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        Object newValue = new Object();

        innerClassProperty.set(bean, newValue);

        verify(delegate).set(bean, newValue);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object newValue = new Object();

        Object result = innerClassProperty.setAndReturn(bean, newValue);

        verify(delegate).set(bean, newValue);
        assertEquals(newValue, result);
    }

    @Test
    public void testReadResolve() throws IOException {
        Object result = innerClassProperty.readResolve();

        verify(this).new InnerClassProperty(innerClassProperty, _annotated);
        assertEquals(innerClassProperty.getClass(), result.getClass());
    }

    @Test
    public void testWriteReplace() {
        Object result = innerClassProperty.writeReplace();

        verify(this).new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, _creator, null, null));
        assertEquals(innerClassProperty.getClass(), result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteReplaceMissingConstructor() {
        innerClassProperty._annotated = null;
        innerClassProperty._creator = null;

        Object result = innerClassProperty.writeReplace();

        fail("Expected IllegalArgumentException to be thrown");
    }

    @Test
    public void testDeserializeWithNullValueDeserializer() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        verify(valueDeserializer).getNullValue(context);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String jsonValue = "{'type':'ClassName'}";
        when(jsonParser.getValueAsString()).thenReturn(jsonValue);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(valueTypeDeserializer.deserialize(jsonParser, context, eq(null))).thenReturn(typeDeserializer);
        when(valueDeserializer.deserializeWithType(jsonParser, context, valueTypeDeserializer)).thenReturn(value = new Object());

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        ArgumentCaptor<Object> argCaptor = ArgumentCaptor.forClass(Object.class);
        verify(delegate).set(bean, argCaptor.capture());
        assertEquals(value, argCaptor.getValue());

        verify(valueDeserializer).deserializeWithType(jsonParser, context, valueTypeDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullCreator() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String jsonValue = "{}";
        when(jsonParser.getValueAsString()).thenReturn(jsonValue);

        Constructor<?> constructor = null;
        when(valueDeserializer.deserialize(jsonParser, context, eq(null))).thenReturn(value = new Object());

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        fail("Expected IllegalArgumentException to be thrown");
    }

    @Test
    public void testDeserializeWithExceptionDuringCreation() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String jsonValue = "{}";
        when(jsonParser.getValueAsString()).thenReturn(jsonValue);

        try {
            when(constructor.newInstance(bean)).thenThrow(new Exception("Test Exception"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        verify(valueDeserializer).deserialize(jsonParser, context, eq(null));
        verify(valueDeserializer, times(2)).deserialize(jsonParser, context, eq(null));
        verify(ClassUtil).unwrapAndThrowAsIAE(any(Exception.class), anyString());
    }

    @Test
    public void testDeserializeWithAnnotation() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextFieldName()).thenReturn("annotation");
        when(jsonParser.nextValue()).thenReturn(new JsonNodeFactory().textNode("annotationValue"));

        JsonDeserialize annotation = mock(JsonDeserialize.class);
        when(annotation.getValueClass()).thenReturn(JsonDeserialize.class);
        when(valueDeserializer.deserializeWithValueHandler(eq(context), any(ValueHandler.class))).thenReturn(annotation);

        innerClassProperty._annotated = annotation;

        Object result = innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        verify(annotation).getValueClass();
        verify(valueDeserializer).deserializeWithValueHandler(eq(context), any(ValueHandler.class));
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        verify(valueDeserializer, never()).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }
}