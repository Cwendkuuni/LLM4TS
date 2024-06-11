package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty.PropertyReferring;

public class ObjectIdReferencePropertyTest {
    private ObjectIdReferenceProperty property;

    @Before
    public void setup() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithName() {
        property = property.withName(PropertyName.construct("newName"));
        assertEquals("newName", property.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        property = property.withValueDeserializer(deser);
        assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        property = property.withNullProvider(nva);
        assertEquals(nva, property.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        SettableBeanProperty forward = property._forward;
        property.fixAccess(config);
        verify(forward).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<Deprecated> acls = Deprecated.class;
        SettableBeanProperty forward = property._forward;
        property.getAnnotation(acls);
        verify(forward).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty forward = property._forward;
        property.getMember();
        verify(forward).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = property._forward;
        property.getCreatorIndex();
        verify(forward).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        verify(property._forward).deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeSetAndReturn(p, ctxt, instance);

        verify(property._forward).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);

        verify(property._forward).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        property.setAndReturn(instance, value);

        verify(property._forward).setAndReturn(instance, value);
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty parent = mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);

        Object id = new Object();
        Object value = new Object();

        propertyReferring.handleResolvedForwardReference(id, value);

        verify(parent).set(ob, value);
    }
}