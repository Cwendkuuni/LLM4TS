package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Test class for InnerClassProperty.
 */
public class InnerClassPropertyTest {

    @Mock
    private JsonDeserializer<?> jsonDeserializer;

    @Mock
    private AnnotatedConstructor annotatedConstructor;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object bean;

    @Mock
    private Object value;

    @Mock
    private PropertyName newName;

    @InjectMocks
    private InnerClassProperty innerClassProperty;

    @Test
    public void testConstructor() {
        Constructor<?> constructor = mock(Constructor.class);
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        innerClassProperty = new InnerClassProperty(delegate, constructor);
        assertNotNull(innerClassProperty);
        assertNotNull(innerClassProperty._delegate);
        assertNotNull(innerClassProperty._creator);
    }

    @Test
    public void testInnerClassPropertyWithJdkSerializationConstructor() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        innerClassProperty = new InnerClassProperty(src, ann);
        assertNotNull(innerClassProperty);
        assertNotNull(innerClassProperty._delegate);
        assertNull(innerClassProperty._annotated);
        assertNotNull(innerClassProperty._creator);
    }

    @Test
    public void testInnerClassPropertyWithJsonDeserializer() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        innerClassProperty = new InnerClassProperty(src, deser);
        assertNotNull(innerClassProperty);
        assertNotNull(innerClassProperty._delegate);
        assertNotNull(innerClassProperty._valueDeserializer);
    }

    @Test
    public void testInnerClassPropertyWithNewName() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        PropertyName newName = mock(PropertyName.class);
        innerClassProperty = new InnerClassProperty(src, newName);
        assertNotNull(innerClassProperty);
        assertNotNull(innerClassProperty._delegate);
        assertNotNull(innerClassProperty._name);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = mock(Object.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String valueString = "testValue";
        when(jsonParser.getValueAsString()).thenReturn(valueString);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);
        when(bean.getClass()).thenReturn(Object.class);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertNotNull(innerClassProperty._delegate);
        assertNotNull(instance);
        assertEquals(valueString, instance.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullInstance() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String valueString = "testValue";
        when(jsonParser.getValueAsString()).thenReturn(valueString);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String valueString = "testValue";
        when(jsonParser.getValueAsString()).thenReturn(valueString);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);

        Object result = innerClassProperty.deserializesetAndReturn(jsonParser, deserializationContext, instance);

        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object valueToSet = mock(Object.class);

        innerClassProperty.set(instance, valueToSet);

        assertNotNull(instance);
        assertNotNull(valueToSet);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object valueToSet = mock(Object.class);

        when(jsonParser.getValueAsString()).thenReturn("testValue");

        Object result = innerClassProperty.setAndReturn(instance, jsonParser);

        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test
    public void testReadResolve() {
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        when(ann.getAnnotated()).thenReturn(innerClassProperty);

        InnerClassProperty result = (InnerClassProperty) innerClassProperty.readResolve();

        assertNotNull(result);
        assertNotNull(result._delegate);
        assertNotNull(result._annotated);
    }

    @Test
    public void testWriteReplace() {
        when(jsonParser.getValueAsString()).thenReturn("testValue");

        Object result = innerClassProperty.writeReplace();

        assertNotNull(result);
        assertInstanceOf(InnerClassProperty.class, result);
    }
}