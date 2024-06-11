package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    @org.junit.Test
    public void testFindObjectId() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey();
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId objectId = ctx.findObjectId(id, generator, resolver);
        assertNotNull(objectId);
    }

    @org.junit.Test
    public void testFindObjectIdWithNullId() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey();
            }
        };

        ReadableObjectId objectId = ctx.findObjectId(null, generator, new SimpleObjectIdResolver());
        assertNull(objectId);
    }

    @org.junit.Test
    public void testFindObjectIdWithEmptyListOfObjectIdResolver() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey();
            }
        };

        List<ObjectIdResolver> objectIdResolvers = new ArrayList<>();
        ReadableObjectId objectId = ctx.findObjectId(id, generator, new SimpleObjectIdResolver());
        assertNotNull(objectId);
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey();
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id1 = new Object();
        Object id2 = new Object();
        ctx.findObjectId(id1, generator, resolver);
        ctx.findObjectId(id2, generator, resolver);
        ctx.checkUnresolvedObjectId();
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithUnresolvedIds() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey();
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id1 = new Object();
        Object id2 = new Object();
        ctx.findObjectId(id1, generator, resolver);
        ctx.findObjectId(id2, generator, resolver);
        ObjectIdGenerator.IdKey key = generator.key(id1);
        ReadableObjectId id1ObjectId = ctx.findObjectId(id1, generator, new SimpleObjectIdResolver());
        ReadableObjectId id2ObjectId = ctx.findObjectId(id2, generator, new SimpleObjectIdResolver());
        id1ObjectId.setReferringProperties(new Referring[]{new Referring(key, Object.class, new Object(){})});
        ctx.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDeserializerInstance() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        Object deserDef = new Class<JsonDeserializer<Object>>(){};
        JsonDeserializer<Object> deserializer = ctx.deserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testDeserializerInstanceWithJsonDeserializer() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>(){};
        JsonDeserializer<Object> deserializerInstance = ctx.deserializerInstance(ann, deserializer);
        assertEquals(deserializer, deserializerInstance);
    }

    @org.junit.Test
    public void testDeserializerInstanceWithClass() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        Class<JsonDeserializer<Object>> deserDef = JsonDeserializer.class;
        JsonDeserializer<Object> deserializerInstance = ctx.deserializerInstance(ann, deserDef);
        assertNotNull(deserializerInstance);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDeserializerDefinition() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        Object deserDef = new Object(){};
        JsonDeserializer<Object> deserializerInstance = ctx.deserializerInstance(ann, deserDef);
        fail("Expected IllegalStateException");
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDeserializerDefinitionAsClass() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        Class<?> deserDef = String.class;
        JsonDeserializer<Object> deserializerInstance = ctx.deserializerInstance(ann, deserDef);
        fail("Expected IllegalStateException");
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        Object deserDef = new Class<KeyDeserializer<Object>>(){};
        KeyDeserializer keyDeserializerInstance = ctx.keyDeserializerInstance(ann, deserDef);
        assertNull(keyDeserializerInstance);
    }

    @org.junit.Test
    public void testKeyDeserializerInstanceWithKeyDeserializer() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        KeyDeserializer keyDeserializer = new KeyDeserializer(){};
        KeyDeserializer keyDeserializerInstance = ctx.keyDeserializerInstance(ann, keyDeserializer);
        assertEquals(keyDeserializer, keyDeserializerInstance);
    }

    @org.junit.Test
    public void testKeyDeserializerInstanceWithClass() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        Class<KeyDeserializer<Object>> deserDef = KeyDeserializer.class;
        KeyDeserializer keyDeserializerInstance = ctx.keyDeserializerInstance(ann, deserDef);
        assertNotNull(keyDeserializerInstance);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidKeyDeserializerDefinition() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        Object deserDef = new Object(){};
        KeyDeserializer keyDeserializerInstance = ctx.keyDeserializerInstance(ann, deserDef);
        fail("Expected IllegalStateException");
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidKeyDeserializerDefinitionAsClass() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDeserializationConfig());
        Annotated ann = null;
        Class<?> deserDef = String.class;
        KeyDeserializer keyDeserializerInstance = ctx.keyDeserializerInstance(ann, deserDef);
        fail("Expected IllegalStateException");
    }
}