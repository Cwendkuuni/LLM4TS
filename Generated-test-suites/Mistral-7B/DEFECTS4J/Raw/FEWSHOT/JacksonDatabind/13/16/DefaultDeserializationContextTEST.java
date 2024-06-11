package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
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

public class DefaultDeserializationContextTest {

    @Test
    public void testCreateInstance() {
        DeserializerFactory df = new DefaultDeserializerFactory();
        DefaultDeserializationConfig config = new DefaultDeserializationConfig();
        JsonParser jp = createMockJsonParser();
        InjectableValues iv = new InjectableValues();

        DefaultDeserializationContext ctx = DefaultDeserializationContext.Impl.createInstance(config, jp, iv);
        assertNotNull(ctx);
    }

    @Test
    public void testWith() {
        DeserializerFactory df = new DefaultDeserializerFactory();
        DefaultDeserializationContext ctx = new DefaultDeserializationContext(df, null);

        DefaultDeserializationConfig config = new DefaultDeserializationConfig();
        JsonParser jp = createMockJsonParser();
        InjectableValues iv = new InjectableValues();

        DefaultDeserializationContext newCtx = ctx.with(new DefaultDeserializerFactory());

        assertEquals(DefaultDeserializationContext.class, newCtx.getClass());
        assertEquals(DefaultDeserializerFactory.class, newCtx.getFactory().getClass());
        assertNotSame(df, newCtx.getFactory());

        ctx = newCtx.createInstance(config, jp, iv);
    }

    @Test
    public void testFindObjectId() {
        DeserializerFactory df = new DefaultDeserializerFactory();
        DefaultDeserializationContext ctx = new DefaultDeserializationContext(df, null);

        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(getClass(), id.getClass()) {
                    @Override
                    public String toString() {
                        return getClass().getSimpleName() + "[" + id.getClass().getSimpleName() + "]";
                    }
                };
            }

            @Override
            public Object generateId(SerializerProvider serializers, Object id) {
                return id;
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId idResolved(ObjectId id, DeserializationContext ctxt) {
                return id;
            }
        };

        Object id = new Object();
        ObjectId generatedId = ctx.findObjectId(id, idGenerator, resolver);

        assertNotNull(generatedId);
        assertEquals(idGenerator.getClass().getSimpleName() + "[" + id.getClass().getSimpleName() + "]", generatedId.toString());
        assertEquals(id, generatedId.getId());
        assertNull(generatedId.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DeserializerFactory df = new DefaultDeserializerFactory();
        DefaultDeserializationContext ctx = new DefaultDeserializationContext(df, null);

        Object id1 = new Object();
        Object id2 = new Object();

        ctx._objectIds = new LinkedHashMap<>();
        ctx._objectIds.put(new ObjectIdGenerator.IdKey(Object.class, Object.class), new ReadableObjectId(null));
        ctx._objectIds.put(new ObjectIdGenerator.IdKey(id1.getClass(), Object.class), new ReadableObjectId(null));

        try {
            ctx.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            assertEquals(2, e.size());
            for (Entry<ObjectId, Object> entry : e.getUnresolvedIds().entrySet()) {
                assertEquals(id1.getClass().getSimpleName() + "[" + id1.getClass().getSimpleName() + "]", entry.getKey().toString());
                assertEquals(id2.getClass().getSimpleName() + "[" + id2.getClass().getSimpleName() + "]", e.getUnresolvedIds().get(entry.getKey()).toString());
            }
        }
    }

    @Test
    public void testDeserializerInstance() {
        DeserializerFactory df = new DefaultDeserializerFactory();
        DefaultDeserializationContext ctx = new DefaultDeserializationContext(df, null);

        Annotated ann = createMockAnnotated();
        JsonDeserializer deser = new JsonDeserializer() {};

        JsonDeserializer result = ctx.deserializerInstance(ann, deser);
        assertEquals(JsonDeserializer.class, result.getClass());
        assertEquals(deser, result);
    }

    @Test
    public void testKeyDeserializerInstance() {
        DeserializerFactory df = new DefaultDeserializerFactory();
        DefaultDeserializationContext ctx = new DefaultDeserializationContext(df, null);

        Annotated ann = createMockAnnotated();
        KeyDeserializer deser = new KeyDeserializer() {};

        KeyDeserializer result = ctx.keyDeserializerInstance(ann, deser);
        assertEquals(KeyDeserializer.class, result.getClass());
        assertEquals(deser, result);
    }

    private JsonParser createMockJsonParser() {
        return mock(JsonParser.class);
    }

    private Annotated createMockAnnotated() {
        return mock(Annotated.class);
    }
}