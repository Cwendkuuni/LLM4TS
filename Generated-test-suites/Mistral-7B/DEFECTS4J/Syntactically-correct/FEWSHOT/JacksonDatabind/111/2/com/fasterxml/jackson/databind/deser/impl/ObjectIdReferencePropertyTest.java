package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructorWithForwardAndObjectIdInfo() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructorWithSrcDeserAndNullValueProvider() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty src = mock(SettableBeanProperty.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nva);

        assertEquals(src, property._forward);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }

    @Test
    public void testConstructorWithSrcPropertyName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty src = mock(SettableBeanProperty.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);

        assertEquals(src, property._forward);
        assertEquals(newName, property._name);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        SettableBeanProperty result = property.withName(newName);

        assertEquals(property, result._forward);
        assertEquals(newName, result._name);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        SettableBeanProperty result = property.withValueDeserializer(deser);

        assertEquals(property, result._forward);
        assertEquals(deser, result._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        SettableBeanProperty result = property.withNullProvider(nva);

        assertEquals(property, result._forward);
        assertEquals(nva, result._nullProvider);
    }

    @Test
    public void testFixAccess() {
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        DeserializationConfig config = mock(DeserializationConfig.class);

        property.fixAccess(config);

        verify(config).findBeanDeserializer(property.getType());
    }

    @Test
    public void testGetAnnotation() {
        Annotation acls = mock(Annotation.class);
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        assertEquals(property.getAnnotation(acls.getClass()), property._forward.getAnnotation(acls.getClass()));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        assertEquals(property.getMember(), property._forward.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        int creatorIndex = 0;
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        assertEquals(creatorIndex, property.getCreatorIndex());
        assertEquals(creatorIndex, property._forward.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        SettableBeanProperty property = mock(SettableBeanProperty.class);
        Object value = new Object();

        when(property.deserialize(p, ctxt)).thenReturn(value);

        property.deserializeAndSet(p, ctxt, instance);

        assertEquals(value, instance.getClass().getDeclaredField(property.getName().getShortName()).get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        SettableBeanProperty property = mock(SettableBeanProperty.class);
        Object value = new Object();

        when(property.deserialize(p, ctxt)).thenReturn(value);

        assertEquals(value, property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        property.set(instance, value);

        assertEquals(value, instance.getClass().getDeclaredField(property.getName().getShortName()).get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        assertEquals(value, property.setAndReturn(instance, value));
    }

    @Test
    public void testPropertyReferringConstructor() {
        ObjectIdReferenceProperty parent = mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = mock(Class.class);
        Object ob = new Object();

        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);

        assertEquals(parent, propertyReferring._parent);
        assertEquals(ref, propertyReferring._ref);
        assertEquals(type, propertyReferring._type);
        assertEquals(ob, propertyReferring._pojo);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty parent = mock(ObjectIdReferenceProperty.class);
        Object id = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(parent, null, null, null);

        propertyReferring.handleResolvedForwardReference(id, value);

        assertEquals(value, parent.get(null));
    }
}