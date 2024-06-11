package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {
    
    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private SettableBeanProperty forward;
    private JsonDeserializer<?> deser;
    private NullValueProvider nva;
    private PropertyName newName;
    private DeserializationConfig config;
    private JsonParser p;
    private DeserializationContext ctxt;
    private Object instance;
    private UnresolvedForwardReference reference;
    private Class<?> type;
    private Object ob;
    private Object id;
    private Object value;
    
    @Before
    public void setup() {
        forward = mock(SettableBeanProperty.class);
        deser = mock(JsonDeserializer.class);
        nva = mock(NullValueProvider.class);
        newName = mock(PropertyName.class);
        config = mock(DeserializationConfig.class);
        p = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        instance = new Object();
        reference = mock(UnresolvedForwardReference.class);
        type = Object.class;
        ob = new Object();
        id = new Object();
        value = new Object();
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructor() {
        assertEquals(forward, objectIdReferenceProperty._forward);
    }

    @Test
    public void testWithNullValueProvider() {
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withNullProvider(nva);
        assertEquals(nva, result._nullProvider);
        assertEquals(objectIdReferenceProperty._valueDeserializer, result._valueDeserializer);
        assertEquals(objectIdReferenceProperty._forward, result._forward);
        assertEquals(objectIdReferenceProperty._objectIdInfo, result._objectIdInfo);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(forward.deserializeSetAndReturn(p, ctxt, instance)).thenReturn(value);
        
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        assertEquals(value, result);
        verify(forward).deserializeSetAndReturn(p, ctxt, instance);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferringHandleResolvedForwardReferenceInvalidId() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, type, ob);
        
        propertyReferring.handleResolvedForwardReference(id, value);
    }
}