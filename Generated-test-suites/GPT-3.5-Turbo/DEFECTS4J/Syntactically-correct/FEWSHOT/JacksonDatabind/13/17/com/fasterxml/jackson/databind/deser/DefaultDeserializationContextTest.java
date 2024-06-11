package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private DeserializationConfig config;
    private ObjectMapper mapper;
    private boolean exceptionThrown;

    @Before
    public void setup() {
        context = new DefaultDeserializationContext.Impl(null);
        config = new DeserializationConfig(null, null, null);
        mapper = new ObjectMapper();
        exceptionThrown = false;
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext copiedContext = context.copy();
        assertNotNull(copiedContext);
        assertEquals(context.getClass(), copiedContext.getClass());
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext newInstance = context.createInstance(config, null, null);
        assertNotNull(newInstance);
        assertEquals(context.getClass(), newInstance.getClass());
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext newContext = context.with(null);
        assertNotNull(newContext);
        assertEquals(context.getClass(), newContext.getClass());
    }

    @Test
    public void testDeserializerInstance() {
        Exception error = null;
        try {
            context.deserializerInstance(null, null);
        } catch (Exception e) {
            error = e;
        }
        assertNotNull(error);
        assertEquals(IllegalStateException.class, error.getClass());
        assertEquals("AnnotationIntrospector returned deserializer definition of type null; expected type JsonDeserializer or Class<JsonDeserializer> instead", error.getMessage());
    }

    @Test
    public void testKeyDeserializerInstance() {
        Exception error = null;
        try {
            context.keyDeserializerInstance(null, null);
        } catch (Exception e) {
            error = e;
        }
        assertNotNull(error);
        assertEquals(IllegalStateException.class, error.getClass());
        assertEquals("AnnotationIntrospector returned key deserializer definition of type null; expected type KeyDeserializer or Class<KeyDeserializer> instead", error.getMessage());
    }

    @Test
    public void testFindObjectId() {
        ObjectIdResolver resolverType = new TestObjectIdResolver();
        ReadableObjectId objectId = context.findObjectId("1", null, resolverType);
        assertNotNull(objectId);
    }

    @Test
    public void testFindObjectIdWithResolver() {
        ObjectIdResolver resolverType = new TestObjectIdResolver();
        ReadableObjectId objectId = context.findObjectId("1", null, resolverType);
        assertNotNull(objectId);

        ReadableObjectId objectId2 = context.findObjectId("1", null, resolverType);
        assertNotNull(objectId);
    }

    @Test
    public void testFindObjectIdWithNullObjectIdResolvers() {
        context._objectIdResolvers = null;
        ObjectIdResolver resolverType = new TestObjectIdResolver();
        ReadableObjectId objectId = context.findObjectId("1", null, resolverType);
        assertNotNull(objectId);
    }

    @Test
    public void testFindObjectIdWithObjectIdResolvers() {
        List<ObjectIdResolver> objectIdResolvers = new ArrayList<>();
        ObjectIdResolver resolverType = new TestObjectIdResolver();
        objectIdResolvers.add(resolverType);
        context._objectIdResolvers = objectIdResolvers;
        ReadableObjectId objectId = context.findObjectId("1", null, resolverType);
        assertNotNull(objectId);

        ReadableObjectId objectId2 = context.findObjectId("1", null, resolverType);
        assertNotNull(objectId);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        UnresolvedForwardReference exception = null;
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            exception = e;
        }
        assertNull(exception);

        context.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        
        exceptionThrown = false;
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    private static class TestObjectIdResolver implements ObjectIdResolver {

        @Override
        public void bindItem(ObjectIdGenerator.IdKey idKey, Object pojo) {}

        @Override
        public Object resolveId(ObjectIdGenerator.IdKey id) {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdResolver resolverType) {
            return getClass().isAssignableFrom(resolverType.getClass());
        }

        @Override
        public ObjectIdResolver newForDeserialization(Object context) {
            return new TestObjectIdResolver();
        }

        @Override
        public Iterable<Object> inSerializationOrder(boolean x) {
            return new HashMap<Object, Object>().keySet();
        }

        @Override
        public void bindItem(ObjectIdGenerator.IdKey idKey, Object pojo, JsonDeserializer<?> deser) {}

        @Override
        public ObjectIdResolver withDeserializationConfig(ObjectMapper mapper, DeserializationConfig config, JsonDeserializer<?> deserializer) {
            return new TestObjectIdResolver();
        }
    }
}