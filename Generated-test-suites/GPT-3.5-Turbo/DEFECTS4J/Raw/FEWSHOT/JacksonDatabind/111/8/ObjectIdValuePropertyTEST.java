
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.lang.annotation.Annotation;
import static org.mockito.Mockito.*;

@Test
public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        new ObjectIdValueProperty(objectIdReader, metadata);
        verify(objectIdReader, times(1)).propertyName;
        verify(objectIdReader, times(1)).getIdType();
        verify(objectIdReader, times(1)).getDeserializer();
        verify(objectIdReader, times(1)).getPropertyMetadata();
    }

    @Test
    public void testConstructor_ObjectIdValueProperty() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        new ObjectIdValueProperty(src, deser, nva);
        verify(src, times(1))._objectIdReader;
        verify(src, times(1)).getValueDeserializer();
    }

    @Test
    public void testConstructor_ObjectIdValueProperty_PropertyName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);
        new ObjectIdValueProperty(src, newName);
        verify(src, times(1))._objectIdReader;
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty result = src.withName(newName);
        verify(src, times(1))._objectIdReader;
        verify(src, times(1)).getValueDeserializer();
        verify(newName, times(1)).toString();
    }

    @Test
    public void testWithValueDeserializer_SameDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer deser = mock(JsonDeserializer.class);
        when(src.getValueDeserializer()).thenReturn(deser);
        ObjectIdValueProperty result = src.withValueDeserializer(deser);
        verify(src, times(1))._objectIdReader;
        verify(src, times(1)).getValueDeserializer();
        verify(deser, times(1)).getClass();
        verify(deser, times(0)).deserialize(any(), any());
    }

    @Test
    public void testWithValueDeserializer_DifferentDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer deser = mock(JsonDeserializer.class);
        when(src.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));
        ObjectIdValueProperty result = src.withValueDeserializer(deser);
        verify(src, times(1))._objectIdReader;
        verify(src, times(1)).getValueDeserializer();
        verify(deser, times(2)).getClass();
        verify(deser, times(0)).deserialize(any(), any());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        ObjectIdValueProperty result = src.withNullProvider(nva);
        verify(src, times(1))._objectIdReader;
        verify(src, times(1)).getValueDeserializer();
        verify(nva, times(1)).getClass();
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty instance = mock(ObjectIdValueProperty.class);
        Class<A> aalc = mock(Class.class);
        instance.getAnnotation(aalc);
        verify(instance, times(1)).getAnnotation(aalc);
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty instance = mock(ObjectIdValueProperty.class);
        instance.getMember();
        verify(instance, times(1)).getMember();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty instance = mock(ObjectIdValueProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instanceObj = mock(Object.class);
        instance.deserializeAndSet(p, ctxt, instanceObj);
        verify(instance, times(1)).deserializeAndSet(p, ctxt, instanceObj);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty instance = mock(ObjectIdValueProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instanceObj = mock(Object.class);
        instance.deserializeSetAndReturn(p, ctxt, instanceObj);
        verify(instance, times(1)).deserializeSetAndReturn(p, ctxt, instanceObj);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty instance = mock(ObjectIdValueProperty.class);
        Object instanceObj = mock(Object.class);
        Object value = mock(Object.class);
        instance.set(instanceObj, value);
        verify(instance, times(1)).set(instanceObj, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty instance = mock(ObjectIdValueProperty.class);
        Object instanceObj = mock(Object.class);
        Object value = mock(Object.class);
        instance.setAndReturn(instanceObj, value);
        verify(instance, times(1)).setAndReturn(instanceObj, value);
    }
}
