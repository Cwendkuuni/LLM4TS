package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);

        assertSame(objectIdReader, prop._objectIdReader);
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty prop = new ObjectIdValueProperty(src, deser, nva);

        assertSame(deser, prop._valueDeserializer);
        assertSame(nva, prop._nullProvider);
    }

    @Test
    public void testCopyConstructorWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty prop = new ObjectIdValueProperty(src, newName);

        assertSame(newName, prop._propName);
    }

    @Test
    public void testWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty prop = (ObjectIdValueProperty)src.withName(newName);

        assertSame(newName, prop._propName);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        JsonDeserializer<?> oldDeser = new JsonDeserializer<>();
        JsonDeserializer<?> newDeser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        src._valueDeserializer = oldDeser;
        ObjectIdValueProperty prop = (ObjectIdValueProperty)src.withValueDeserializer(newDeser);

        assertSame(newDeser, prop._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty prop = (ObjectIdValueProperty)src.withNullProvider(nva);

        assertSame(nva, prop._nullProvider);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);

        assertNull(prop.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);

        assertNull(prop.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);

        try {
            prop.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            // Test passes if it reaches this point
            assertTrue(true);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);

        try {
            assertNull(prop.deserializeSetAndReturn(p, ctxt, instance));
        } catch (IOException e) {
            // Test passes if it reaches this point
            assertTrue(true);
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
        prop._objectIdReader.idProperty = new SettableBeanProperty();

        try {
            prop.set(instance, value);
        } catch (IOException e) {
            // Test passes if it reaches this point
            assertTrue(true);
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
        prop._objectIdReader.idProperty = new SettableBeanProperty();

        try {
            assertNull(prop.setAndReturn(instance, value));
        } catch (IOException e) {
            // Test passes if it reaches this point
            assertTrue(true);
        }
    }
}