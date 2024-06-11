
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        forward = mock(SettableBeanProperty.class);
        objectIdInfo = mock(ObjectIdInfo.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructor() {
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testCopy() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdReferenceProperty newProperty = objectIdReferenceProperty.copy(deser, nva);
        assertNotSame(newProperty, objectIdReferenceProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(nva, newProperty._nullProvider);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);

        ObjectIdReferenceProperty newProperty = objectIdReferenceProperty.withName(newName);
        assertNotSame(newProperty, objectIdReferenceProperty);
        assertEquals(newName, newProperty.getFullName());
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        ObjectIdReferenceProperty newProperty = objectIdReferenceProperty.withValueDeserializer(deser);
        assertNotSame(newProperty, objectIdReferenceProperty);
        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdReferenceProperty newProperty = objectIdReferenceProperty.withNullProvider(nva);
        assertNotSame(newProperty, objectIdReferenceProperty);
        assertEquals(nva, newProperty._nullProvider);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        objectIdReferenceProperty.fixAccess(config);
        verify(forward).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<?> acls = mock(Class.class);

        objectIdReferenceProperty.getAnnotation(acls);
        verify(forward).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        objectIdReferenceProperty.getMember();
        verify(forward).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        objectIdReferenceProperty.getCreatorIndex();
        verify(forward).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
        verify(objectIdReferenceProperty).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        assertNull(objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance));
        verify(p, times(0)).getCurrentLocation();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeSetAndReturnWithException() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(ClassUtil.nonNullString(new Object()), new Object(), (Class<?>) null);

        UnresolvedForwardReference reference = new UnresolvedForwardReference(null, null, null, idKey);
        doThrow(reference).when(objectIdReferenceProperty).deserialize(p, ctxt);

        objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        objectIdReferenceProperty.set(instance, value);
        verify(forward).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        assertEquals(value, objectIdReferenceProperty.setAndReturn(instance, value));
        verify(forward).setAndReturn(instance, value);
    }

    @Test
    public void testPropertyReferring() throws IOException {
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = mock(Class.class);
        Object ob = mock(Object.class);

        ObjectIdReferenceProperty.PropertyReferring propertyReferring =
                             new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, ref, type, ob);

        assertSame(ref, propertyReferring.getUnresolvedId());
        assertSame(type, propertyReferring.getType());
        assertSame(ob, propertyReferring._pojo);

        Object id = new Object();
        Object value = new Object();
        propertyReferring.handleResolvedForwardReference(id, value);

        verify(objectIdReferenceProperty).set(ob, value);
    }
}
