package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.deser.impl.AnnotatedConstructor;

public class InnerClassPropertyTest {

    @Test
    public void testConstruction() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);

        InnerClassProperty property = new InnerClassProperty(delegate, constructor);
        assertEquals(delegate, property._delegate);
        assertNull(property._annotated);
        assertSame(constructor, property._creator);

        property = new InnerClassProperty(delegate, annotatedConstructor);
        assertEquals(delegate, property._delegate);
        assertEquals(annotatedConstructor, property._annotated);
        assertSame(constructor, property._creator);
    }

    @Test
    public void testSerialization() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);

        InnerClassProperty property = new InnerClassProperty(delegate, constructor);
        Object writeReplace = property.writeReplace();
        assertSame(property, writeReplace);

        property = new InnerClassProperty(delegate, annotatedConstructor);
        Object writeReplace = property.writeReplace();
        assertSame(property, writeReplace);
    }

    @Test
    public void testSerializationWithAnnotatedConstructor() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);

        when(annotatedConstructor.getAnnotated()).thenReturn(mock(JsonDeserializer.class));

        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor);
        Object writeReplace = property.writeReplace();
        assertSame(property, writeReplace);
    }

    @Test
    public void testDeserialization() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);

        InnerClassProperty src = new InnerClassProperty(delegate, constructor);
        Object deserialized = new InnerClassProperty(src, annotatedConstructor).deserializeWithType(null, null, Object.class);
        assertEquals(src, deserialized);

        src = new InnerClassProperty(delegate, annotatedConstructor);
        deserialized = new InnerClassProperty(src, null).deserializeWithType(null, null, Object.class);
        assertEquals(src, deserialized);
    }

    @Test
    public void testDeserializationWithValueDeserializer() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);

        when(annotatedConstructor.getAnnotated()).thenReturn(valueDeserializer);

        InnerClassProperty src = new InnerClassProperty(delegate, constructor);
        Object deserialized = new InnerClassProperty(src, annotatedConstructor, valueDeserializer).deserializeWithType(null, null, Object.class);
        assertEquals(src, deserialized);
    }

    @Test
    public void testDeserializationWithValueTypeDeserializer() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<?> valueTypeDeserializer = mock(JsonDeserializer.class);

        when(annotatedConstructor.getAnnotated()).thenReturn(annotatedConstructor);
        when(annotatedConstructor.getValueType()).thenReturn(ClassUtil.valueToRawType(String.class));
        when(annotatedConstructor.getParameterTypes()).thenReturn(new Class[]{String.class});

        when(valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(JsonDeserializer.class))).thenReturn("test");
        when(valueTypeDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(JsonDeserializer.class))).thenReturn("testType");

        InnerClassProperty src = new InnerClassProperty(delegate, constructor);
        Object deserialized = new InnerClassProperty(src, annotatedConstructor, valueDeserializer, valueTypeDeserializer).deserializeWithType(null, null, String.class);
        assertEquals("test", deserialized);
        assertEquals("testType", deserialized.getClass());
    }

    @Test
    public void testSetterMethods() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        Object instance = mock(Object.class);
        Object value = new Object();

        InnerClassProperty property = new InnerClassProperty(delegate, constructor);
        property.set(instance, value);
        verify(delegate).set(instance, value);

        property = new InnerClassProperty(delegate, constructor);
        property.setAndReturn(instance, value);
        verify(delegate).setAndReturn(instance, value);
    }

    @Test
    public void testGetters() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor);

        PropertyName newName = mock(PropertyName.class);
        assertEquals(property, property.withName(newName));

        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        assertEquals(property, property.withValueDeserializer(deser));

        int index = 0;
        assertEquals(property, property.withIndex(index));
        verify(delegate).assignIndex(index);

        assertEquals(0, property.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Annotation annotation = mock(Annotation.class);
        InnerClassProperty property = new InnerClassProperty(delegate, null);

        assertEquals(annotation, property.getAnnotation(annotation.getClass()));
        verify(delegate).getAnnotation(annotation.getClass());
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        InnerClassProperty property = new InnerClassProperty(delegate, null);

        assertEquals(member, property.getMember());
        verify(delegate).getMember();
    }
}