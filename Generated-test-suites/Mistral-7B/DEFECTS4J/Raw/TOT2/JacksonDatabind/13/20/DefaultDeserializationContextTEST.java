package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
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

public class DefaultDeserializationContextTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test empty context
        assertNull(context.findObjectId(null, ObjectIdGenerator.Id.CLASS, null));
        assertNull(context.findObjectId(null, ObjectIdGenerator.Id.PROPERTY, null));

        // Test with null id and simple classes
        ObjectIdGenerator idGen = new SimpleObjectIdGenerator();
        context = new Impl(new ObjectMapper.DefaultDeserializationContext.Builder()
                .withRootValueFactory(mapper.getFactory().createValueFactory().withFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false))
                .build());
        Object id1 = new Object();
        assertNull(context.findObjectId(id1, idGen, null));

        // Test with null id and complex objects
        class TestClass {
            private int id;
            private String name;

            public TestClass(int id, String name) {
                this.id = id;
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }

        ObjectIdGenerator idGenComplex = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                if (id instanceof TestClass) {
                    return ObjectIdGenerator.Id.PROPERTY;
                }
                return ObjectIdGenerator.Id.CLASS;
            }

            @Override
            public Object generateId(Serializable id) {
                if (id instanceof TestClass) {
                    TestClass testClass = (TestClass) id;
                    return new IdKey(TestClass.class, testClass.getId());
                }
                return id;
            }
        };

        TestClass testClass = new TestClass(1, "Test");
        assertNull(context.findObjectId(testClass, idGenComplex, null));

        // Test with existing id
        context._objectIds = new LinkedHashMap<>();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context._objectIdResolvers = Arrays.asList(resolver);
        context.findObjectId(testClass, idGenComplex, resolver);
        ReadableObjectId readableObjectId = context._objectIds.get(new IdKey(TestClass.class, 1));
        assertNotNull(readableObjectId);
        assertEquals(TestClass.class, readableObjectId.getObjectIdClass());
        assertEquals(1, readableObjectId.getId());

        // Test with multiple object id resolvers
        ObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        ObjectIdResolver resolver2 = new SimpleObjectIdResolver();
        context._objectIdResolvers = Arrays.asList(resolver1, resolver2);
        readableObjectId = context.findObjectId(testClass, idGenComplex, resolver1);
        assertNotNull(readableObjectId);
        assertEquals(resolver1, readableObjectId.getObjectIdResolver());
        readableObjectId = context.findObjectId(testClass, idGenComplex, resolver2);
        assertNotNull(readableObjectId);
        assertEquals(resolver2, readableObjectId.getObjectIdResolver());

        // Test with missing id and no error handling
        context = new Impl(new ObjectMapper.DefaultDeserializationContext.Builder()
                .withoutFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .build());
        context._objectIds = new LinkedHashMap<>();
        assertNull(context.findObjectId(testClass, idGenComplex, null));

        // Test with missing id and error handling
        context = new Impl(new ObjectMapper.DefaultDeserializationContext.Builder()
                .withRootValueFactory(mapper.getFactory().createValueFactory().withoutFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS))
                .build());
        context._objectIds = new LinkedHashMap<>();
        try {
            context.findObjectId(testClass, idGenComplex, null);
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test empty context
        assertNull(context.deserializerInstance(null, null));

        // Test with null deserDef
        assertNull(context.deserializerInstance(null, null));

        // Test with JsonDeserializer
        JsonDeserializer<String> jsonDeserializer = new JsonDeserializer<String>() {};
        assertEquals(String.class, context.deserializerInstance(null, jsonDeserializer).getClass());

        // Test with Class
        Class<JsonDeserializer<String>> jsonDeserializerClass = JsonDeserializer.class;
        JsonDeserializer<String> deserializer = context.deserializerInstance(null, jsonDeserializerClass);
        assertEquals(String.class, deserializer.getClass());

        // Test with missing deserializer class
        try {
            context.deserializerInstance(null, Object.class);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with non-JsonDeserializer class
        try {
            context.deserializerInstance(null, Integer.class);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with null ann
        JsonDeserializer<String> jsonDeserializerAnn = new JsonDeserializer<String>() {};
        assertNull(context.deserializerInstance(null, jsonDeserializerAnn));
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test empty context
        assertNull(context.keyDeserializerInstance(null, null));

        // Test with null deserDef
        assertNull(context.keyDeserializerInstance(null, null));

        // Test with JsonKeyDeserializer
        JsonKeyDeserializer<String> jsonKeyDeserializer = new JsonKeyDeserializer<String>() {};
        assertEquals(String.class, context.keyDeserializerInstance(null, jsonKeyDeserializer).getClass());

        // Test with Class
        Class<JsonKeyDeserializer<String>> jsonKeyDeserializerClass = JsonKeyDeserializer.class;
        JsonKeyDeserializer<String> deserializer = context.keyDeserializerInstance(null, jsonKeyDeserializerClass);
        assertEquals(String.class, deserializer.getClass());

        // Test with missing deserializer class
        try {
            context.keyDeserializerInstance(null, Object.class);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with non-JsonKeyDeserializer class
        try {
            context.keyDeserializerInstance(null, Integer.class);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with null ann
        JsonKeyDeserializer<String> jsonKeyDeserializerAnn = new JsonKeyDeserializer<String>() {};
        assertNull(context.keyDeserializerInstance(null, jsonKeyDeserializerAnn));
    }
}