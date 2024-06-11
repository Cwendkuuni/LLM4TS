package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        AnnotatedConstructor constructor = Object()::new;
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(creator, innerClassProperty._creator);
    }

    @Test
    public void testSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedConstructor constructor = Object()::new;
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        Object writeReplace = innerClassProperty.writeReplace();
        Object readResolve = mapper.readValue(mapper.writeValueAsBytes(writeReplace), InnerClassProperty.class);

        assertEquals(innerClassProperty, readResolve);
    }

    @Test
    public void testDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedConstructor constructor = Object()::new;
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        Object writeReplace = new AnnotatedClass(null, constructor, null, null);
        Object readResolve = mapper.readValue(mapper.writeValueAsBytes(writeReplace), InnerClassProperty.class);

        assertEquals(innerClassProperty, readResolve);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedConstructor constructor = Object()::new;
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        Object value = new Object();
        Object result = innerClassProperty.setAndReturn(null, value);

        assertEquals(value, result);

        assertNotNull(mapper.readValue(mapper.writeValueAsBytes(result), Object.class));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedConstructor constructor = Object()::new;
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        Object value = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.readValueAs(Object.class)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jp, ctxt, null);

        verify(delegate).set(null, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedConstructor constructor = Object()::new;
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        Object value = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.readValueAs(Object.class)).thenReturn(value);

        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, null);

        assertEquals(value, result);
    }

    @Test
    public void testGetterMethods() {
        AnnotatedConstructor constructor = Object()::new;
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        assertEquals(delegate._delegate, innerClassProperty._delegate);
        assertEquals(creator, innerClassProperty._creator);
        assertEquals(delegate._valueDeserializer, innerClassProperty._valueDeserializer);
        assertEquals(delegate._annotated, innerClassProperty._annotated);
    }
}