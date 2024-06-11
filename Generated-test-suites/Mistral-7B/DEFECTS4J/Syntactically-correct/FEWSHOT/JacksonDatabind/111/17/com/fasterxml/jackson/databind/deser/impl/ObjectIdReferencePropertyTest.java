package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstruction() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo);
        assertNotNull(property);
        assertEquals(null, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyCopyConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty copiedProperty = new ObjectIdReferenceProperty(property, null);
        assertEquals(property, copiedProperty);
        assertEquals(forward, copiedProperty._forward);
        assertEquals(objectIdInfo, copiedProperty._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyNameChange() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        PropertyName newName = PropertyName.construct("newName");
        ObjectIdReferenceProperty renamedProperty = new ObjectIdReferenceProperty(property, newName);
        assertEquals(newName, renamedProperty.getName());
        assertEquals(property, renamedProperty);
        assertEquals(forward, renamedProperty._forward);
        assertEquals(objectIdInfo, renamedProperty._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyValueDeserializerChange() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty deserializerChangedProperty = new ObjectIdReferenceProperty(property, deserializer, null);
        assertNotEquals(property, deserializerChangedProperty);
        assertNull(deserializerChangedProperty._forward);
        assertEquals(objectIdInfo, deserializerChangedProperty._objectIdInfo);
        assertEquals(deserializer, deserializerChangedProperty._valueDeserializer);
        assertEquals(forward, deserializerChangedProperty._forward);
    }

    @Test
    public void testObjectIdReferencePropertyNullProviderChange() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        NullValueProvider nva = mock(NullValueProvider.class);
        ObjectIdReferenceProperty nullProviderChangedProperty = new ObjectIdReferenceProperty(property, null, nva);
        assertNotEquals(property, nullProviderChangedProperty);
        assertNull(nullProviderChangedProperty._forward);
        assertEquals(objectIdInfo, nullProviderChangedProperty._objectIdInfo);
        assertNull(nullProviderChangedProperty._forward);
        assertEquals(nva, nullProviderChangedProperty._nullProvider);
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        DeserializationConfig config = mock(DeserializationConfig.class);
        property.fixAccess(config);
        verify(forward).fixAccess(config);
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Class<? extends Annotation> annotationType = mock(Annotation.class);
        assertNull(property.getAnnotation(annotationType));
        verifyNoMoreInteractions(forward);
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        AnnotatedMember member = mock(AnnotatedMember.class);
        assertEquals(member, property.getMember());
        verifyNoMoreInteractions(forward);
    }

    @Test
    public void testObjectIdReferencePropertyGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        int creatorIndex = 1;
        assertEquals(creatorIndex, property.getCreatorIndex());
        verifyNoMoreInteractions(forward);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonParser parser = mock(JsonParser.class);
        Object instance = new Object();
        Object value = new Object();
        Object id = new Object();

        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("valueString");

        doAnswer(i -> {
            property.deserializeSetAndReturn(parser, null, instance);
            return value;
        }).when(property).deserializeAndSet(parser, null, instance);

        assertEquals(value, property.deserializeAndSet(parser, null, instance));
        verify(parser).nextToken();
        verify(parser).getText();
        verify(parser).nextToken();
        verify(parser).nextToken();
        verifyNoMoreInteractions(parser);
        verify(forward).set(instance, value);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturnNull() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonParser parser = mock(JsonParser.class);
        Object instance = new Object();

        doAnswer(i -> null).when(property).deserialize(parser, null);

        assertNull(property.deserializeSetAndReturn(parser, null, instance));
        verify(parser).nextToken();
        verify(parser).nextToken();
        verify(parser).nextToken();
        verify(parser).nextToken();
        verify(parser).nextToken();
        verifyNoMoreInteractions(parser);
        verifyNoInteractions(forward);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(property.deserialize(mock(JsonParser.class), null)).thenReturn(reference);

        Object instance = new Object();

        PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, reference, Object.class, instance);
        Object id = new Object();
        Object value = new Object();

        referring.handleResolvedForwardReference(id, value);

        assertEquals(value, property.get(instance));
        verify(forward).set(instance, value);
        verifyNoMoreInteractions(property);
    }

    @Test
    public void testObjectIdReferencePropertySet() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);
        assertEquals(value, property.get(instance));
        verify(forward).set(instance, value);
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object instance = new Object();
        Object value = new Object();

        Object returnValue = new Object();
        when(property.setAndReturn(instance, value)).thenReturn(returnValue);

        assertEquals(returnValue, property.setAndReturn(instance, value));
        assertEquals(value, property.get(instance));
        verify(forward).setAndReturn(instance, value);
    }
}