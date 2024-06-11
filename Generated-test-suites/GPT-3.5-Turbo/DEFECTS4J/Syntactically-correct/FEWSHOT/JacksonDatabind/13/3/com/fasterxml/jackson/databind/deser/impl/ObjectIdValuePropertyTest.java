package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;
    private Object instance;
    private JsonParser jp;
    private DeserializationContext ctxt;

    @Before
    public void setup() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        
        jp = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        
        property = new ObjectIdValueProperty(objectIdReader, null);
        instance = new Object();
        
        property._objectIdReader = objectIdReader;
        property._valueDeserializer = deserializer;
        objectIdReader.idProperty = property;
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        property.deserializeAndSet(jp, ctxt, instance);
        
        verify(property._valueDeserializer).deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        property.deserializeSetAndReturn(jp, ctxt, instance);
        
        verify(property._valueDeserializer).deserialize(jp, ctxt);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_noIdProperty() throws Exception {
        property._objectIdReader.idProperty = null;
        
        property.setAndReturn(instance, null);
    }

    @Test
    public void testSetAndReturn_withIdProperty() throws Exception {
        property.setAndReturn(instance, null);
        
        verify(property._objectIdReader.idProperty).setAndReturn(instance, null);
    }
}