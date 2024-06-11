package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstruction() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyCopy() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty copied = new ObjectIdReferenceProperty(property, mock(JsonDeserializer.class), mock(NullValueProvider.class));

        assertSame(forward, copied._forward);
        assertSame(objectIdInfo, copied._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyNameChange() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);

        assertSame(forward, newProperty._forward);
        assertSame(objectIdInfo, newProperty._objectIdInfo);
        assertSame(newName, newProperty._name);
    }

    @Test
    public void testObjectIdReferencePropertyValueDeserializerChange() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, valueDeserializer, nullValueProvider);

        assertSame(forward, newProperty._forward);
        assertSame(objectIdInfo, newProperty._objectIdInfo);
        assertSame(valueDeserializer, newProperty._valueDeserializer);
        assertSame(nullValueProvider, newProperty._nullProvider);
    }

    @Test
    public void testObjectIdReferencePropertyNullProviderChange() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo, nullValueProvider);

        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, valueDeserializer);

        assertSame(forward, newProperty._forward);
        assertSame(objectIdInfo, newProperty._objectIdInfo);
        assertNull(newProperty._nullProvider);
        assertSame(valueDeserializer, newProperty._valueDeserializer);
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        DeserializationConfig config = mock(DeserializationConfig.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        property.fixAccess(config);

        if (forward != null) {
            forward.fixAccess(config);
        }
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        Annotation annotation = mock(Annotation.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        when(forward.getAnnotation(Annotation.class)).thenReturn(annotation);

        Annotation result = property.getAnnotation(Annotation.class);

        assertSame(annotation, result);
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        AnnotatedMember member = mock(AnnotatedMember.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        when(forward.getMember()).thenReturn(member);

        AnnotatedMember result = property.getMember();

        assertSame(member, result);
    }

    @Test
    public void testObjectIdReferencePropertyGetCreatorIndex() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        int creatorIndex = 0;

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        when(forward.getCreatorIndex()).thenReturn(creatorIndex);

        int result = property.getCreatorIndex();

        assertSame(creatorIndex, result);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        when(forward.deserializeAndSet(p, ctxt, instance)).thenReturn("value");

        Object result = property.deserializeAndSet(p, ctxt, instance);

        assertSame("value", result);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        when(forward.deserializeSetAndReturn(p, ctxt, instance)).thenReturn("value");

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertSame("value", result);
    }

    @Test
    public void testObjectIdReferencePropertySet() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        Object instance = new Object();
        Object value = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        property.set(instance, value);

        forward.set(instance, value);
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        Object instance = new Object();
        Object value = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object result = property.setAndReturn(instance, value);

        assertSame(value, result);
    }

    @Test
    public void testObjectIdReferencePropertyWithName() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty newProperty = property.withName(newName);

        assertSame(forward, newProperty._forward);
        assertSame(objectIdInfo, newProperty._objectIdInfo);
        assertSame(newName, newProperty._name);
    }

    @Test
    public void testObjectIdReferencePropertyWithValueDeserializer() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo, nullValueProvider);

        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(valueDeserializer);

        assertSame(forward, newProperty._forward);
        assertSame(objectIdInfo, newProperty._objectIdInfo);
        assertSame(valueDeserializer, newProperty._valueDeserializer);
        assertSame(nullValueProvider, newProperty._nullProvider);
    }

    @Test
    public void testObjectIdReferencePropertyWithNullProvider() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo, nullValueProvider);

        ObjectIdReferenceProperty newProperty = property.withNullProvider(mock(NullValueProvider.class));

        assertSame(forward, newProperty._forward);
        assertSame(objectIdInfo, newProperty._objectIdInfo);
        assertNull(newProperty._nullProvider);
        assertSame(valueDeserializer, newProperty._valueDeserializer);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        Object id = mock(Object.class);
        Object value = new Object();

        when(forward.deserializeAndSet(p, ctxt, instance)).thenThrow(UnresolvedForwardReference.class);
        when(reference.getRoid()).thenReturn(new ObjectIdReader.ObjectIdReaderBase.Id("id"));

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        property.deserializeAndSet(p, ctxt, instance);

        property.handleResolvedForwardReference(id, value);
    }
}