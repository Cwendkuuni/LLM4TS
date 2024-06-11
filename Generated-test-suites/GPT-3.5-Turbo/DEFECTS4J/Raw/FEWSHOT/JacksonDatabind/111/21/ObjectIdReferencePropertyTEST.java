
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {

    @Mock
    private SettableBeanProperty forward;

    @Mock
    private ObjectIdInfo objectIdInfo;

    private ObjectIdReferenceProperty property;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructor() {
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testDeserializerConstructor() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nvp = mock(NullValueProvider.class);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deser, nvp);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(nvp, newProperty._nullProvider);
    }

    @Test
    public void testPropertyNameConstructor() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty._propName);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withName(newName);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty._propName);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withValueDeserializer(deser);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nvp = mock(NullValueProvider.class);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withNullProvider(nvp);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(nvp, newProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        property.fixAccess(config);
        verify(forward, times(1)).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<? extends Annotation> acls = Annotation.class;
        property.getAnnotation(acls);
        verify(forward, times(1)).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        property.getMember();
        verify(forward, times(1)).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        property.getCreatorIndex();
        verify(forward, times(1)).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        verifyZeroInteractions(p);
        verifyZeroInteractions(ctxt);
        verifyZeroInteractions(forward);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));

        verifyZeroInteractions(p);
        verifyZeroInteractions(ctxt);
        verifyZeroInteractions(forward);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);

        verify(forward, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        assertEquals(value, property.setAndReturn(instance, value));

        verify(forward, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        Object id = new Object();
        Object value = new Object();

        ObjectIdReferenceProperty.PropertyReferring referring = spy(new ObjectIdReferenceProperty.PropertyReferring(property, null, null, null));
        referring.handleResolvedForwardReference(id, value);

        verify(referring, times(1)).hasId(id);
        verify(property, times(1)).set(referring._pojo, value);
    }

}
