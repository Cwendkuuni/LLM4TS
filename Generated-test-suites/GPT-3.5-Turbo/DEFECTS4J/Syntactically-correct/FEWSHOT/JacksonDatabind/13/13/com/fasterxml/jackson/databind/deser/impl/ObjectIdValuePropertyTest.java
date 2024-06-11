package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Mock
    private JsonParser jp;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private Object instance;

    @Mock
    private ReadableObjectId roid;

    @Mock
    private SettableBeanProperty idProp;

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        objectIdValueProperty.setAndReturn(instance, null);
    }

    @Test
    public void testWithname() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        objectIdValueProperty.withName(Mockito.mock(PropertyName.class));
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        objectIdValueProperty.withValueDeserializer(Mockito.mock(JsonDeserializer.class));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        assertNull(objectIdValueProperty.getAnnotation(Mockito.mock(Class.class)));
    }

    @Test
    public void testGetMember() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        assertNull(objectIdValueProperty.getMember());
    }
}