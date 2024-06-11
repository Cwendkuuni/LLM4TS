
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    private SettableBeanProperty forward;
    private ObjectMapper objectMapper;
    private JsonParser parser;
    private DeserializationContext context;
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setup() {
        forward = mock(SettableBeanProperty.class);
        objectMapper = mock(ObjectMapper.class);
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        objectIdInfo = mock(ObjectIdInfo.class);
    }

    @Test
    public void testConstructor() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testCopyConstructorWithDeserializerAndProvider() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider provider = mock(NullValueProvider.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.setDeserializer(deser);
        property.setNullValueProvider(provider);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deser, provider);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty.getValueDeserializer());
        assertEquals(provider, newProperty.getNullValueProvider());
    }

    @Test
    public void testCopyConstructorWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.setName(newName);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withName(newName);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withValueDeserializer(deser);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty.getValueDeserializer());
        assertEquals(property.getNullValueProvider(), newProperty.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider provider = mock(NullValueProvider.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withNullProvider(provider);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(provider, newProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.fixAccess(config);
        verify(forward, times(1)).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<AnnotatedMember> acls = AnnotatedMember.class;
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.getAnnotation(acls);
        verify(forward, times(1)).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.getMember();
        verify(forward, times(1)).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.getCreatorIndex();
        verify(forward, times(1)).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.deserializeAndSet(parser, context, instance);
        verify(property, times(1)).deserializeSetAndReturn(parser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.deserializeSetAndReturn(parser, context, instance);
        verify(context, times(1)).setObjectId(property, value);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.set(instance, value);
        verify(forward, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.setAndReturn(instance, value);
        verify(forward, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        Object id = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty.PropertyReferring referring = spy(new ObjectIdReferenceProperty.PropertyReferring(property, null, null, null));
        referring.handleResolvedForwardReference(id, value);
        verify(property, times(1)).set(null, value);
    }
}
