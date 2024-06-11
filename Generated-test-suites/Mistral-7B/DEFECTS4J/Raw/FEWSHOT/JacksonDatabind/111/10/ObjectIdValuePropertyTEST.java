package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.ToStringBeanProperty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdReaderForTest;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader _objectIdReader;

    @org.junit.Before
    public void setUp() {
        initMocks(this);
        _objectIdReader = new ObjectIdReaderForTest();
    }

    @Test
    public void testSerialization() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        BeanPropertyDefinition metadata = new BeanPropertyDefinition(
                "testId", String.class, null, null, null);
        SettableBeanProperty settableProperty = new ObjectIdValueProperty(
                property, new ToStringBeanProperty(metadata), null);
        assertEquals(expected("testId"), settableProperty.getName());
        assertEquals(String.class, settableProperty.getType());
        assertSame(property, settableProperty.getBackRef());
        assertNull(settableProperty.getValueDeserializer());
        assertNull(settableProperty.getNullProvider());
    }

    @Test
    public void testCopyConstructor() throws Exception {
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        ObjectIdValueProperty deserializedProperty = new ObjectIdValueProperty(
                srcProperty, mock(JsonDeserializer.class), mock(NullValueProvider.class));
        assertEquals(srcProperty._objectIdReader, deserializedProperty._objectIdReader);
    }

    @Test
    public void testNameChange() throws Exception {
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        ObjectIdValueProperty newNameProperty = new ObjectIdValueProperty(
                srcProperty, new PropertyName("newName"));
        assertEquals("newName", newNameProperty.getName().asPrefixString());
    }

    @Test
    public void testValueDeserializerChange() throws Exception {
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        ObjectIdValueProperty newDeserializerProperty = new ObjectIdValueProperty(
                srcProperty, mock(JsonDeserializer.class), null);
        assertNotNull(newDeserializerProperty.getValueDeserializer());
    }

    @Test
    public void testNullProviderChange() throws Exception {
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        ObjectIdValueProperty newNullProviderProperty = new ObjectIdValueProperty(
                srcProperty, null, mock(NullValueProvider.class));
        assertNotNull(newNullProviderProperty.getNullProvider());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        Object id = "testId";
        Object instance = new Object();
        when(_objectIdReader.idProperty).thenReturn(new ToStringBeanProperty(new PropertyName("test")));
        property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        verify(_objectIdReader.idProperty).setAndReturn(instance, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        Object id = "testId";
        Object instance = new Object();
        when(_objectIdReader.idProperty).thenReturn(new ToStringBeanProperty(new PropertyName("test")));
        when(_objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(_objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        Object result = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertEquals(id, result);
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        Object id = "testId";
        Object instance = new Object();
        when(_objectIdReader.idProperty).thenReturn(new ToStringBeanProperty(new PropertyName("test")));
        property.set(instance, id);
        verify(_objectIdReader.idProperty).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        Object id = "testId";
        Object instance = new Object();
        when(_objectIdReader.idProperty).thenReturn(new ToStringBeanProperty(new PropertyName("test")));
        Object result = property.setAndReturn(instance, id);
        assertEquals(id, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetUnsupported() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        Object instance = new Object();
        property.set(instance, null);
    }

    @Test
    public void testAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(
                _objectIdReader, new PropertyMetadata() {});
        assertNull(property.getMember());
    }

    private String expected(String name) {
        return ClassUtil.simpleName(ObjectIdValueProperty.class) + "@" + name;
    }
}