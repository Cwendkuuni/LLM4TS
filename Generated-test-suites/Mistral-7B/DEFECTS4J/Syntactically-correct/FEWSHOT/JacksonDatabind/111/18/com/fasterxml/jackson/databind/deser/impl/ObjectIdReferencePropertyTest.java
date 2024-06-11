package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

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
    public void testObjectIdReferencePropertyCopyConstruction() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, null);
        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyNameChange() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo, mock(PropertyName.class));
        PropertyName newName = mock(PropertyName.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);
        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertSame(newName, property.getName());
    }

    @Test
    public void testObjectIdReferencePropertyValueDeserializerChange() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo, null, null);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, null);
        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertSame(deser, property._valueDeserializer);
    }

    @Test
    public void testObjectIdReferencePropertyNullProviderChange() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo, null, null, nva);
        NullValueProvider newNva = mock(NullValueProvider.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, null, null, null, newNva);
        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertNull(property._nullProvider);
        assertSame(newNva, property._nullProvider);
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        DeserializationConfig config = mock(DeserializationConfig.class);
        property.fixAccess(config);
        when(forward.fixAccess(config)).thenCallRealMethod();
        forward.fixAccess(config);
        verify(forward).fixAccess(config);
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Class<?> annotationType = mock(Annotation.class);
        when(forward.getAnnotation(annotationType)).thenReturn(mock(Annotation.class));
        assertSame(annotationType.newInstance(), property.getAnnotation(annotationType));
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(member);
        assertSame(member, property.getMember());
    }

    @Test
    public void testObjectIdReferencePropertyGetCreatorIndex() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        when(forward.getCreatorIndex()).thenReturn(1);
        assertEquals(1, property.getCreatorIndex());
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String value = "value";
        when(p.getValueAsString()).thenReturn(value);
        when(forward.deserializeAndSet(p, ctxt, instance)).thenReturn(instance);
        Object result = property.deserializeAndSet(p, ctxt, instance);
        assertSame(instance, result);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object deserializedValue = mock(Object.class);
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertSame(instance, result);
        verify(forward).deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testObjectIdReferencePropertySet() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        property.set(instance, value);
        verify(forward).set(instance, value);
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        Object result = property.setAndReturn(instance, value);
        assertSame(value, result);
        verify(forward).setAndReturn(instance, value);
    }

    @Test
    public void testObjectIdReferencePropertyHandleResolvedForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object id = mock(Object.class);
        Object value = mock(Object.class);
        PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, null, null, null) {
            @Override
            public void handleResolvedForwardReference(Object id, Object value) throws IOException {
                super.handleResolvedForwardReference(id, value);
            }
        };
        when(forward.getMember()).thenReturn(mock(AnnotatedMember.class));
        property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        referring.handleResolvedForwardReference(id, value);
        verify(property).set(mock(Object.class), value);
    }
}