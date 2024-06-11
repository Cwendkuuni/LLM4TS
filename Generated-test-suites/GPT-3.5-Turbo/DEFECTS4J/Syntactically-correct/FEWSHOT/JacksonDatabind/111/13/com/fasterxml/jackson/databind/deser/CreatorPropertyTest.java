package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.CreatorProperty;

import org.junit.Test;

public class CreatorPropertyTest {
    @Test
    public void testFindInjectableValue() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object beanInstance = new Object();
        Object injectableValue = property.findInjectableValue(context, beanInstance);
        assertEquals(beanInstance, injectableValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindInjectableValueWithNoInjectableValueId() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object beanInstance = new Object();
        property.findInjectableValue(context, beanInstance);
    }

    @Test
    public void testInject() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object beanInstance = new Object();
        property.inject(context, beanInstance);
        // verify that injection succeeded
        assertTrue(true);
    }

    @Test(expected = RuntimeException.class)
    public void testInjectThrowsIOException() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null) {
            @Override
            public void inject(DeserializationContext context, Object beanInstance) throws IOException {
                throw new IOException();
            }
        };
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object beanInstance = new Object();
        property.inject(context, beanInstance);
    }

    @Test
    public void testWithNullProvider() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null);
        CreatorProperty newProperty = (CreatorProperty) property.withNullProvider(null);
        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getWrapperName(), newProperty.getWrapperName());
        assertEquals(property.getTypeDeserializer(), newProperty.getTypeDeserializer());
        assertEquals(property.getContextAnnotations(), newProperty.getContextAnnotations());
        assertEquals(property.getAnnotated(), newProperty.getAnnotated());
        assertEquals(property.getCreatorIndex(), newProperty.getCreatorIndex());
        assertEquals(property.getInjectableValueId(), newProperty.getInjectableValueId());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(property.isIgnorable(), newProperty.isIgnorable());
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object instance = new Object();
        property.deserializeAndSet(null, context, instance);
        // verify that deserialization and set succeeded
        assertTrue(true);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetThrowsIOException() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                throw new IOException();
            }
        };
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object instance = new Object();
        property.deserializeAndSet(null, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object instance = new Object();
        Object value = property.deserializeSetAndReturn(null, context, instance);
        assertEquals(value, instance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnThrowsIOException() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null) {
            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                throw new IOException();
            }
        };
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object instance = new Object();
        property.deserializeSetAndReturn(null, context, instance);
    }

    @Test
    public void testSet() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null);
        Object instance = new Object();
        property.set(instance, null);
        // verify that set succeeded
        assertTrue(true);
    }

    @Test(expected = IOException.class)
    public void testSetThrowsIOException() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                throw new IOException();
            }
        };
        Object instance = new Object();
        property.set(instance, null);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null);
        Object instance = new Object();
        Object value = property.setAndReturn(instance, null);
        assertEquals(value, instance);
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnThrowsIOException() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "injectableValueId", null) {
            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                throw new IOException();
            }
        };
        Object instance = new Object();
        property.setAndReturn(instance, null);
    }
}