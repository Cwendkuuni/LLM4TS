package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

import java.util.*;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(getClass(), id);
            }
        };

        DefaultDeserializationContext context = new Impl(new Impl.Builder().withoutFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));
        ReadableObjectId objectId = context.findObjectId(new Object(), idGenerator, null);
        assertNull(objectId);

        context = new Impl(new Impl.Builder().withoutFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));
        objectId = context.findObjectId(new Object(), idGenerator, new SimpleObjectIdResolver());
        assertNull(objectId);

        context = new Impl(new Impl.Builder().withFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }
        };
        objectId = context.findObjectId(new Object(), idGenerator, resolver);
        assertNull(objectId);

        context = new Impl(new Impl.Builder().withFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));
        ReadableObjectId existingObjectId = new ReadableObjectId(new ObjectIdGenerator.IdKey(getClass(), new Object()));
        context._objectIds.put(existingObjectId.getKey(), existingObjectId);
        objectId = context.findObjectId(new Object(), idGenerator, resolver);
        assertSame(existingObjectId, objectId);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(getClass(), id);
            }
        };

        DefaultDeserializationContext context = new Impl(new Impl.Builder().withoutFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));
        context.findObjectId(new Object(), idGenerator, null);

        context = new Impl(new Impl.Builder().withFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }

            @Override
            public ReadableObjectId idFromValue(Object id) {
                throw new UnresolvedForwardReference("test", getClass(), new Object());
            }
        };
        context._objectIdResolvers.add(resolver);
        context.findObjectId(new Object(), idGenerator, resolver);
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        Class<JsonDeserializer<Object>> jsonDeserializerClass = JsonDeserializer.class;
        Class<Object> jsonDeserializerClassParameterized = JsonDeserializer.class;

        DefaultDeserializationContext context = new Impl(new Impl.Builder().withoutFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        Object deserDef = deserializer;
        Object deser = context.deserializerInstance(null, deserDef);
        assertSame(deserializer, deser);

        deserDef = jsonDeserializerClass;
        deser = context.deserializerInstance(null, deserDef);
        assertSame(JsonDeserializer.None.class, deser);

        deserDef = jsonDeserializerClassParameterized.asInstanceOf(Class.class);
        deser = context.deserializerInstance(null, deserDef);
        assertSame(JsonDeserializer.None.class, deser);

        HandlerInstantiator handlerInstantiator = null;
        deserDef = jsonDeserializerClassParameterized;
        deser = context.deserializerInstance(null, deserDef);
        assertSame(JsonDeserializer.None.class, deser);

        context = new Impl(new Impl.Builder().withFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));
        deserDef = jsonDeserializerClassParameterized;
        deser = context.deserializerInstance(null, deserDef);
        assertSame(JsonDeserializer.None.class, deser);

        handlerInstantiator = context.getConfig().getHandlerInstantiator();
        deser = context.deserializerInstance(null, deserDef);
        assertSame(JsonDeserializer.None.class, deser);

        deserializer = new JsonDeserializer<Object>() {};
        deserDef = deserializer;
        deser = context.deserializerInstance(null, deserDef);
        assertSame(deserializer, deser);

        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new RuntimeException("Test");
            }
        };
        deserDef = deserializer;
        deser = context.deserializerInstance(null, deserDef);
        assertSame(deserializer, deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        Class<KeyDeserializer> keyDeserializerClass = KeyDeserializer.class;
        Class<Object> keyDeserializerClassParameterized = KeyDeserializer.class;

        DefaultDeserializationContext context = new Impl(new Impl.Builder().withoutFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));

        KeyDeserializer deserializer = new KeyDeserializer() {};
        Object deserDef = deserializer;
        Object deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(deserializer, deser);

        deserDef = keyDeserializerClass;
        deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(KeyDeserializer.None.class, deser);

        deserDef = keyDeserializerClassParameterized.asInstanceOf(Class.class);
        deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(KeyDeserializer.None.class, deser);

        HandlerInstantiator handlerInstantiator = null;
        deserDef = keyDeserializerClassParameterized;
        deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(KeyDeserializer.None.class, deser);

        context = new Impl(new Impl.Builder().withFeature(FAIL_ON_UNRESOLVED_OBJECT_IDS));
        deserDef = keyDeserializerClassParameterized;
        deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(KeyDeserializer.None.class, deser);

        handlerInstantiator = context.getConfig().getHandlerInstantiator();
        deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(KeyDeserializer.None.class, deser);

        deserializer = new KeyDeserializer() {};
        deserDef = deserializer;
        deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(deserializer, deser);

        deserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new RuntimeException("Test");
            }
        };
        deserDef = deserializer;
        deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(deserializer, deser);
    }
}