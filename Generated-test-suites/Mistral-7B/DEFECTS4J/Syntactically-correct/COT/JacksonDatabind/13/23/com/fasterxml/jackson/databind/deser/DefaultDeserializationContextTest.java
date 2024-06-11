package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class DefaultDeserializationContextTest extends TestBase {

    @Test
    public void testFindObjectId() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("myId");
            }
        };

        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        ReadableObjectId objectId = context.findObjectId(new Object(), idGen, idResolver);
        Assert.assertNotNull(objectId);
        Assert.assertEquals(objectId.getKey().key, "myId");

        // Test with null id
        objectId = context.findObjectId(null, idGen, idResolver);
        Assert.assertNull(objectId);
    }

    @Test
    public void testFindObjectIdWithResolver() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("myId");
            }
        };

        ObjectIdResolver idResolver = new ObjectIdGenerator() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }
        };

        ReadableObjectId objectId = context.findObjectId(new Object(), idGen, idResolver);
        Assert.assertNotNull(objectId);
        Assert.assertEquals(objectId.getKey().key, "myId");

        // Test with already existing id
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey("myId"), new ReadableObjectId(new ObjectIdGenerator.IdKey("myId")));
        objectId = context.findObjectId(new Object(), idGen, idResolver);
        Assert.assertNotNull(objectId);
        Assert.assertEquals(objectId.getKey().key, "myId");
    }

    @Test(expectedExceptions = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey("myId"), new ReadableObjectId(new ObjectIdGenerator.IdKey("myId")));

        context.checkUnresolvedObjectId();

        // Add unresolved referencing id
        context._objectIds.put(new ObjectIdGenerator.IdKey("refId"), new ReadableObjectId(new ObjectIdGenerator.IdKey("refId")));

        context.checkUnresolvedObjectId();
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializerInstanceNullDeserDef() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        Object deserDef = null;

        context.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testDeserializerInstanceJsonDeserializer() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        JsonDeserializer<Object> deser = new StubJsonDeserializer<>();

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deser);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, deser);
    }

    @Test
    public void testDeserializerInstanceClass() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        Class<JsonDeserializer<Object>> deserClass = JsonDeserializer.class;

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserClass);
        Assert.assertNotNull(result);
        Assert.assertTrue(JsonDeserializer.class.isInstance(result));
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializerInstanceInvalidClass() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        Class<String> deserClass = String.class;

        context.deserializerInstance(ann, deserClass);
    }

    @Test
    public void testDeserializerInstanceHandlerInstantiator() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        HandlerInstantiator hi = context._config.getHandlerInstantiator();

        JsonDeserializer<Object> result = context.deserializerInstance(ann, null);
        Assert.assertNull(result);

        result = hi.deserializerInstance(context._config, ann, JsonDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(JsonDeserializer.class.isInstance(result));
    }

    @Test
    public void testKeyDeserializerInstanceJsonDeserializer() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        KeyDeserializer deser = new StubKeyDeserializer<>();

        KeyDeserializer result = context.keyDeserializerInstance(ann, deser);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, deser);
    }

    @Test
    public void testKeyDeserializerInstanceClass() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        Class<KeyDeserializer> deserClass = KeyDeserializer.class;

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserClass);
        Assert.assertNotNull(result);
        Assert.assertTrue(KeyDeserializer.class.isInstance(result));
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testKeyDeserializerInstanceInvalidClass() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        Class<String> deserClass = String.class;

        context.keyDeserializerInstance(ann, deserClass);
    }

    @Test
    public void testKeyDeserializerInstanceHandlerInstantiator() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());

        Annotated ann = createTestAnnotation();
        HandlerInstantiator hi = context._config.getHandlerInstantiator();

        KeyDeserializer result = context.keyDeserializerInstance(ann, null);
        Assert.assertNull(result);

        result = hi.keyDeserializerInstance(context._config, ann, KeyDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(KeyDeserializer.class.isInstance(result));
    }

    private static Annotated createTestAnnotation() {
        // TODO: implement test-specific Annotated creation
        return null;
    }

    private static class StubJsonDeserializer<T> implements JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public boolean isCachable() {
            return true;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt) throws JsonMappingException {
            return null;
        }

        @Override
        public void resolveReference(DeserializationContext ctxt) throws JsonMappingException {
            // empty
        }
    }

    private static class StubKeyDeserializer<T> implements KeyDeserializer {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt) throws JsonMappingException {
            return null;
        }
    }
}