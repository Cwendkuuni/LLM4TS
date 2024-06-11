package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

import java.util.*;
import java.util.Map.Entry;

/**
 * Test cases for DefaultDeserializationContext class.
 */
public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new Impl.Builder()
                .with(new MyObjectIdGenerator())
                .build();

        Object id = new Object();

        ObjectIdGenerator.IdKey key = ObjectIdGenerator.IdKey.USE_CLASS;
        ReadableObjectId objectId = context.findObjectId(id, key, ObjectIdGenerator.class);
        assertNotNull(objectId);
        assertEquals(id.getClass(), objectId.getId());

        ObjectIdResolver resolver = new MyObjectIdResolver();
        objectId = context.findObjectId(id, key, resolver.getClass());
        assertNotNull(objectId);
        assertEquals(resolver, objectId.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new Impl.Builder()
                .build();

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey("key1"),
                new ReadableObjectId(new Object()));

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        context._objectIds.remove("key1");

        ReadableObjectId objectId = context.findObjectId(new Object(), ObjectIdGenerator.class, ObjectIdGenerator.class);
        context._objectIds.put(objectId.getKey(), objectId);

        context.checkUnresolvedObjectId();
        // no exception thrown, test passed
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new Impl.Builder()
                .build();

        Annotated ann = null;
        Object deserDef = MyJsonDeserializer.class;

        JsonDeserializer<?> deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(MyJsonDeserializer.class, deserializer.getClass());

        deserDef = new Class<?>[] {MyJsonDeserializer.class};
        deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(MyJsonDeserializer.class, deserializer.getClass());

        deserDef = null;
        deserializer = context.deserializerInstance(ann, deserDef);
        assertNull(deserializer);

        deserDef = new byte[0];
        try {
            deserializer = context.deserializerInstance(ann, deserDef);
            fail("Expected IllegalStateException exception");
        } catch (IllegalStateException e) {
            // expected
        }

        deserDef = new String("not a class");
        try {
            deserializer = context.deserializerInstance(ann, deserDef);
            fail("Expected IllegalStateException exception");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new Impl.Builder()
                .build();

        Annotated ann = null;
        Object deserDef = MyKeyDeserializer.class;

        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(MyKeyDeserializer.class, deserializer.getClass());

        deserDef = new Class<?>[] {MyKeyDeserializer.class};
        deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(MyKeyDeserializer.class, deserializer.getClass());

        deserDef = null;
        deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNull(deserializer);

        deserDef = new byte[0];
        try {
            deserializer = context.keyDeserializerInstance(ann, deserDef);
            fail("Expected IllegalStateException exception");
        } catch (IllegalStateException e) {
            // expected
        }

        deserDef = new String("not a class");
        try {
            deserializer = context.keyDeserializerInstance(ann, deserDef);
            fail("Expected IllegalStateException exception");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    static class MyObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }

        @Override
        public ObjectIdGenerator.IdKey key(JavaType idType) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }

        @Override
        public ObjectIdGenerator.IdKey key(Class<?> idType) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }

        @Override
        public ObjectIdGenerator.IdKey key(DeserializationContext ctxt) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }

        @Override
        public ObjectIdGenerator.IdKey key(DeserializationConfig config) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }

        @Override
        public ObjectIdGenerator.IdKey key(InjectableValues values) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }

        @Override
        public Object generateId(DeserializationContext ctxt, Object id) {
            return id;
        }

        @Override
        public Object generateId(DeserializationContext ctxt, JavaType idType) {
            return idType.getRawClass();
        }

        @Override
        public Object generateId(DeserializationContext ctxt, Class<?> idType) {
            return idType;
        }

        @Override
        public Object generateId(DeserializationContext ctxt, DeserializationConfig config) {
            return config;
        }

        @Override
        public Object generateId(DeserializationContext ctxt, InjectableValues values) {
            return values;
        }
    }

    static class MyObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, JavaType idType) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }

        @Override
        public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, Class<?> idType) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }

        @Override
        public Object resolveId(DeserializationContext ctxt, ObjectIdGenerator.IdKey idKey, Object id) {
            return null;
        }

        @Override
        public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, Annotated idAnnotation) {
            return null;
        }

        @Override
        public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, Annotated memberAnnotation) {
            return null;
        }

        @Override
        public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, Annotated propertyAnnotation) {
            return null;
        }

        @Override
        public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, Annotated parameterAnnotation) {
            return null;
        }
    }

    static class MyJsonDeserializer implements JsonDeserializer<Object> {}

    static class MyKeyDeserializer implements KeyDeserializer {}
}