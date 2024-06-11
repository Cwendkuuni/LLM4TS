package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

/**
 * Test class for DefaultDeserializationContext.
 */
public class DefaultDeserializationContextTest {

    private ObjectMapper objectMapper;
    private DefaultDeserializationContext context;

    @Before
    public void setUp() {
        ObjectMapper.Factory factory = new ObjectMapper.DefaultFactory();
        factory.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper = factory.build();
        context = objectMapper.getDeserializationContext();
    }

    /**
     * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator, ObjectIdResolver)}.
     */
    @Test
    public void testFindObjectId() {
        // Test with null id, generator and resolver
        assertNull(context.findObjectId(null, null, null));

        // Test with valid id, generator and resolver
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }
        };
        Object id = new Object();
        ReadableObjectId objectId = context.findObjectId(id, generator, resolver);
        assertNotNull(objectId);
        assertEquals("testKey", objectId.getKey().key);

        // Test with generator and null resolver
        objectId = context.findObjectId(id, generator, null);
        assertNotNull(objectId);
        // Test with null id, generator and null resolver
        objectId = context.findObjectId(null, generator, null);
        assertNull(objectId);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#checkUnresolvedObjectId()}.
     */
    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        // Test with unresolved object id
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey("testKey"), new ReadableObjectId(new Object()));

        context.checkUnresolvedObjectId();

        // Test with no unresolved object ids
        context._objectIds = null;
        context.checkUnresolvedObjectId();
    }

    /**
     * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)}.
     */
    @Test
    public void testDeserializerInstance() {
        // Test with null ann and deserDef
        assertNull(context.deserializerInstance(null, null));

        // Test with valid ann and deserDef
        ObjectMapper.DefaultFactory factory = new ObjectMapper.DefaultFactory();
        ObjectMapper mapper = factory.build();
        JsonDeserializer<Integer> deserializer = mapper.deserializerFactory().findValueDeserializer(Integer.class);
        Annotated ann = mapper.introspectForSerialization(Integer.class);
        JsonDeserializer<Integer> deser = context.deserializerInstance(ann, deserializer);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        // Test with null ann and valid deserDef
        deserializer = mapper.deserializerFactory().findValueDeserializer(Integer[].class);
        deser = context.deserializerInstance(null, deserializer);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        // Test with valid ann and null deserDef
        deser = context.deserializerInstance(ann, null);
        assertNull(deser);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)}.
     */
    @Test
    public void testKeyDeserializerInstance() {
        // Test with null ann and deserDef
        assertNull(context.keyDeserializerInstance(null, null));

        // Test with valid ann and deserDef
        ObjectMapper.DefaultFactory factory = new ObjectMapper.DefaultFactory();
        ObjectMapper mapper = factory.build();
        KeyDeserializer deserializer = mapper.deserializerFactory().findKeyDeserializer(String.class);
        Annotated ann = mapper.introspectForSerialization(String.class);
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserializer);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        // Test with null ann and valid deserDef
        deserializer = mapper.deserializerFactory().findKeyDeserializer(String[].class);
        deser = context.keyDeserializerInstance(null, deserializer);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        // Test with valid ann and null deserDef
        deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#with(DeserializerFactory)}.
     */
    @Test
    public void testWith() {
        // Test with null factory
        try {
            context.with(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with valid factory
        ObjectMapper.DefaultFactory factory = new ObjectMapper.DefaultFactory();
        DefaultDeserializationContext newContext = context.with(factory);
        assertNotSame(context, newContext);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#createInstance(DeserializationConfig, JsonParser, InjectableValues)}.
     */
    @Test
    public void testCreateInstance() {
        // Test with null config, jp and values
        try {
            context.createInstance(null, null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with valid config, jp and values
        ObjectMapper.DefaultFactory factory = new ObjectMapper.DefaultFactory();
        ObjectMapper mapper = factory.build();
        JsonParser jp = mapper.getFactory().createParser("{\"name\":\"John\"}");
        InjectableValues values = mapper.injectables();
        DefaultDeserializationContext newContext = context.createInstance(mapper.getDeserializationConfig(), jp, values);
        assertNotSame(context, newContext);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#copy()}.
     */
    @Test
    public void testCopy() {
        // Test with null context
        try {
            context.copy();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with valid context
        DefaultDeserializationContext newContext = context.copy();
        assertNotSame(context, newContext);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#Impl()}.
     */
    @Test
    public void testImpl() {
        // Test with null context
        try {
            context.Impl();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with valid context
        DefaultDeserializationContext.Impl impl = (DefaultDeserializationContext.Impl) context;
        assertNotNull(impl);
    }
}