package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdReferencePropertyTest {

    private JsonDeserializer<Object> jsonDeserializer;
    private ObjectIdInfo objectIdInfo;
    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private SettableBeanProperty settableBeanProperty;

    @Before
    public void setUp() throws Exception {
        objectIdInfo = new ObjectIdInfo(new AnnotatedMember(new AnnotationIntrospector().findProperty("name", TestClass.class), TestClass.class), null);
        jsonDeserializer = mock(JsonDeserializer.class);
        settableBeanProperty = mock(SettableBeanProperty.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWithName() throws IOException {
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
        Field nameField = ObjectIdReferenceProperty.class.getDeclaredField("_name");
        nameField.setAccessible(true);
        assertSame(PropertyName.construct("newPropertyName"), nameField.get(newObjectIdReferenceProperty));
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(jsonDeserializer);
        assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
        Field valueDeserializerField = ObjectIdReferenceProperty.class.getDeclaredField("_valueDeserializer");
        valueDeserializerField.setAccessible(true);
        verify(jsonDeserializer, times(1)).__init__(eq(objectIdReferenceProperty), any(NullValueProvider.class));
        assertSame(jsonDeserializer, valueDeserializerField.get(newObjectIdReferenceProperty));
    }

    @Test
    public void testWithNullProvider() throws IOException {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
        Field nullProviderField = ObjectIdReferenceProperty.class.getDeclaredField("_nullProvider");
        nullProviderField.setAccessible(true);
        assertSame(nullValueProvider, nullProviderField.get(newObjectIdReferenceProperty));
    }

    @Test
    public void testFixAccess() throws IOException {
        Object testClassInstance = new TestClass();
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        newObjectIdReferenceProperty.fixAccess(new DeserializationConfig());
        newObjectIdReferenceProperty.set(testClassInstance, "testValue");
        assertSame("testValue", newObjectIdReferenceProperty.getMember().get(testClassInstance));
    }

    @Test
    public void testGetAnnotation() throws IOException {
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        ArgumentCaptor<Class<? extends Annotation>> annotationTypeArgumentCaptor = ArgumentCaptor.forClass(Annotation.class);
        verify(objectIdInfo, times(1)).findAnnotation(annotationTypeArgumentCaptor.capture());
        assertEquals("@JsonProperty", annotationTypeArgumentCaptor.getValue().annotationType().getName());
        assertSame(objectIdInfo, newObjectIdReferenceProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() throws IOException {
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        assertEquals(objectIdInfo.getMember(), newObjectIdReferenceProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() throws IOException {
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        assertEquals(objectIdReferenceProperty.getCreatorIndex(), newObjectIdReferenceProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object testClassInstance = new TestClass();
        Object testValue = new Object();
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.getValueAsToken()).thenReturn(mock(JsonToken.class));
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        newObjectIdReferenceProperty.deserializeAndSet(mockJsonParser, new DeserializationContext(), testClassInstance);
        newObjectIdReferenceProperty.set(testClassInstance, testValue);
        verify(settableBeanProperty, times(1)).set(eq(testClassInstance), eq(testValue));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object testClassInstance = new TestClass();
        Object testValue = new Object();
        JsonParser mockJsonParser = mock(JsonParser.class);
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        when(mockJsonParser.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(jsonDeserializer.deserialize(mock(JsonParser.class), new DeserializationContext()))
                .thenReturn(testValue);
        Object result = newObjectIdReferenceProperty.deserializeSetAndReturn(mockJsonParser, new DeserializationContext(), testClassInstance);
        verify(settableBeanProperty, times(1)).set(eq(testClassInstance), eq(testValue));
        assertSame(testValue, result);
    }

    @Test
    public void testSet() throws IOException {
        Object testClassInstance = new TestClass();
        Object testValue = new Object();
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        newObjectIdReferenceProperty.set(testClassInstance, testValue);
        verify(settableBeanProperty, times(1)).set(eq(testClassInstance), eq(testValue));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object testClassInstance = new TestClass();
        Object testValue = new Object();
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        Object result = newObjectIdReferenceProperty.setAndReturn(testClassInstance, testValue);
        verify(settableBeanProperty, times(1)).set(eq(testClassInstance), eq(testValue));
        assertSame(testValue, result);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        Object testClassInstance = new TestClass();
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        UnresolvedForwardReference unresolvedForwardReference = mock(UnresolvedForwardReference.class);
        Object id = new Object();
        Object value = new Object();
        when(unresolvedForwardReference.getId()).thenReturn(id);
        when(newObjectIdReferenceProperty.getObjectIdInfo()).thenReturn(objectIdInfo);
        when(objectIdInfo.getObjectIdReader()).thenReturn(mock(ObjectIdReader.class));
        when(objectIdInfo.getObjectIdReader().readObjectId(mock(JsonParser.class), new DeserializationContext())).thenReturn(id);
        PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(newObjectIdReferenceProperty, unresolvedForwardReference, TestClass.class, testClassInstance);
        newObjectIdReferenceProperty.handleResolvedForwardReference(id, value);
        verify(newObjectIdReferenceProperty).set(eq(testClassInstance), eq(value));
    }

    @Test(expected = JsonMappingException.class)
    public void testUnresolvedForwardReferenceNoIdentityInfo() throws IOException {
        Object testClassInstance = new TestClass();
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.construct("newPropertyName"));
        UnresolvedForwardReference unresolvedForwardReference = mock(UnresolvedForwardReference.class);
        when(newObjectIdReferenceProperty.getObjectIdInfo()).thenReturn(null);
        when(unresolvedForwardReference.getId()).thenReturn(new Object());
        newObjectIdReferenceProperty.handleResolvedForwardReference(new Object(), null);
    }

    private static class TestClass {
    }
}