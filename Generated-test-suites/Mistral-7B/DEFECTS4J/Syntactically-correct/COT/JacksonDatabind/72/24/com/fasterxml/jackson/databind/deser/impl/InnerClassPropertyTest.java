package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty(new Object(), new Object());
        Constructor<?> constructor = new Object() {}.getClass().getDeclaredConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        InnerClassProperty innerClassProperty2 = new InnerClassProperty(innerClassProperty, null);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
        assertEquals(delegate.getClass(), innerClassProperty2._delegate.getClass());
        assertEquals(constructor.getClass(), innerClassProperty2._creator.getClass());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), new Object() {}.getClass().getDeclaredConstructor());

        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getValueAsString()).thenReturn("testValue");
        when(mockContext.findValueDeserializer(innerClassProperty.getMember().getRawClass())).thenReturn(new JsonDeserializer<Object>() {});

        innerClassProperty.deserializeAndSet(mockParser, mockContext, bean);

        verify(mockParser).nextToken();
        verify(mockParser).getValueAsString();
        verify(mockContext).findValueDeserializer(innerClassProperty.getMember().getRawClass());
        verify(mockContext).findValueDeserializer(innerClassProperty.getMember().getRawClass()).deserialize(mockParser, mockContext, eq(bean), any());
        assertEquals("testValue", innerClassProperty.get(bean));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetMissingConstructor() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), new Object() {}.getClass().getDeclaredConstructor(String.class));

        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getValueAsString()).thenReturn("testValue");

        innerClassProperty.deserializeAndSet(mockParser, mockContext, bean);

        verify(mockParser).nextToken();
        verify(mockParser).getValueAsString();
        verify(mockContext).findValueDeserializer(innerClassProperty.getMember().getRawClass()).deserialize(mockParser, mockContext, eq(bean), any());
        verify(mockParser, never()).nextToken();
        verify(mockParser, never()).getValueAsString();
        verify(mockContext, never()).findValueDeserializer(innerClassProperty.getMember().getRawClass()).deserialize(mockParser, mockContext, eq(bean), any(JsonDeserializer.class));
        verify(mockContext, times(2)).findValueDeserializer(innerClassProperty.getMember().getRawClass());
        throw new AssertionError("Deserialization should have failed with an IllegalArgumentException");
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        Object instance = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), new Object() {}.getClass().getDeclaredConstructor());

        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getValueAsString()).thenReturn("testValue");
        when(mockContext.findValueDeserializer(innerClassProperty.getMember().getRawClass())).thenReturn(new JsonDeserializer<Object>() {});

        Object result = innerClassProperty.deserializeSetAndReturn(mockParser, mockContext, instance);

        verify(mockParser).nextToken();
        verify(mockParser).getValueAsString();
        verify(mockContext).findValueDeserializer(innerClassProperty.getMember().getRawClass()).deserialize(mockParser, mockContext, eq(instance), any());
        assertEquals("testValue", innerClassProperty.get(instance));
        assertEquals(result, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), new Object() {}.getClass().getDeclaredConstructor());
        String testValue = "testValue";

        innerClassProperty.set(bean, testValue);

        assertEquals(testValue, innerClassProperty.get(bean));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), new Object() {}.getClass().getDeclaredConstructor());
        Object testValue = new Object();

        Object result = innerClassProperty.setAndReturn(bean, testValue);

        assertEquals(testValue, innerClassProperty.get(bean));
        assertEquals(result, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteReplaceNullAnnotatedConstructor() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), null);

        try {
            innerClassProperty.writeReplace();
            fail("writeReplace should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWriteReplace() {
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), annotatedConstructor);

        assertEquals(innerClassProperty, innerClassProperty.writeReplace());
    }

    @Test
    public void testReadResolve() {
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), annotatedConstructor);

        assertEquals(innerClassProperty, innerClassProperty.readResolve());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializationWithNullAnnotatedConstructor() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), null);

        try {
            innerClassProperty.serialize(null, null);
            fail("Serialization should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSerialization() throws IOException {
        Object objectToBeSerialized = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(new Object(), new Object() {}.getClass().getDeclaredConstructor());
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        ObjectWriter mockObjectWriter = mock(ObjectWriter.class);

        when(mockObjectWriter.writeValue(eq(mockGenerator), any())).thenReturn(mockGenerator);

        innerClassProperty.serialize(mockObjectWriter, null);

        verify(mockObjectWriter, times(1)).writeValue(eq(mockGenerator), any());
        ObjectWriter objectWriter = new ObjectMapper().writerFor(innerClassProperty.getClass());
        assertEquals(objectToBeSerialized.getClass().getName(), objectWriter.writeValueAsString(objectToBeSerialized));
        assertEquals("{\"class\":\""+InnerClassProperty.class.getName()+"\"}", objectWriter.writeValueAsString(innerClassProperty));
    }
}