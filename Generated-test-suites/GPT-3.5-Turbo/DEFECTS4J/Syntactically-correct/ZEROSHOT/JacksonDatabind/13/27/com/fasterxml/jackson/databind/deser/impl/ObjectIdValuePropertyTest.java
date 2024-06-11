package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objIdValueProperty;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        A annotation = objIdValueProperty.getAnnotation(acls);

        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = objIdValueProperty.getMember();

        assertNull(member);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        objIdValueProperty.deserializeAndSet(jp, ctxt, instance);

        // Assert something
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        Object result = objIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);

        // Assert something
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        objIdValueProperty.set(instance, value);

        // Assert something
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Object result = objIdValueProperty.setAndReturn(instance, value);

        // Assert something
    }
}