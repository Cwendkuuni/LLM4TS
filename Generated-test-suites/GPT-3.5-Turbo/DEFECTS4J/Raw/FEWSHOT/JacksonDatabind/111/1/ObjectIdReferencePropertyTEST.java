package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.set.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.impl.StdIdResolver;
import com.fasterxml.jackson.databind.util.AbstractDeserializer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty property;
    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        forward = mock(SettableBeanProperty.class);
        objectIdInfo = mock(ObjectIdInfo.class);
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructor() {
        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deserializer,
                nullValueProvider);

        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deserializer, newProperty.getValueDeserializer());
        assertEquals(nullValueProvider, newProperty.getValueNullProvider());
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = mock(PropertyName.class);

        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);

        assertNotNull(newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdReferenceProperty newProperty = property.withName(newName);

        assertNotNull(newProperty);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer deserializer = mock(JsonDeserializer.class);

        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deserializer);

        assertNotNull(newProperty);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        ObjectIdReferenceProperty newProperty = property.withNullProvider(nullValueProvider);

        assertNotNull(newProperty);
        assertEquals(nullValueProvider, newProperty.getValueNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        property.fixAccess(config);

        verify(forward, times(1)).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;

        property.getAnnotation(acls);

        verify(forward, times(1)).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(member);

        AnnotatedMember result = property.getMember();

        assertEquals(member, result);
    }

    @Test
    public void testGetCreatorIndex() {
        when(forward.getCreatorIndex()).thenReturn(5);

        int result = property.getCreatorIndex();

        assertEquals(5, result);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        verify(forward, times(1)).deserialize(p, ctxt);
        verify(forward, times(1)).setAndReturn(instance, any());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(forward.getDeserializer()).thenReturn(deserializer);

        JsonParser p = mock(JsonParser.class);
        Object instance = new Object();

        property.deserializeSetAndReturn(p, ctxt, instance);

        verify(deserializer, times(1)).deserialize(p, ctxt);
        verify(forward, times(1)).setAndReturn(instance, any());
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);

        verify(forward, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        property.setAndReturn(instance, value);

        verify(forward, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testPropertyReferring() throws Exception {
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        PropertyReferring referring = property.new PropertyReferring(property, ref, String.class, new Object());

        assertNotNull(referring);

        referring.handleResolvedForwardReference(new Object(), new Object());
        // No exception thrown
    }

    private static class A {

    }
}