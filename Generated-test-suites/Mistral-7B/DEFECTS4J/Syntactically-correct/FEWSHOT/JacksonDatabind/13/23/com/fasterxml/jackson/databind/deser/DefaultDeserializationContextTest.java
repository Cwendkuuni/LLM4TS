package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
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
    public void testFindObjectId() {
        DefaultDeserializationContext context = new Impl(null);
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String toString() {
                        return "test";
                    }
                };
            }
        };
        Object id = new Object();
        ReadableObjectId readableObjectId = context.findObjectId(id, generator, null);
        assertNotNull(readableObjectId);
        assertEquals("test", readableObjectId.getKey().toString());

        context = new Impl(null);
        ObjectIdResolver resolver = new SimpleObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }
        };
        readableObjectId = context.findObjectId(id, null, resolver);
        assertNotNull(readableObjectId);

        context = new Impl(null);
        ObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        ObjectIdResolver resolver2 = new SimpleObjectIdResolver();
        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(resolver1);
        context._objectIdResolvers.add(resolver2);
        readableObjectId = context.findObjectId(id, generator, null);
        assertNotNull(readableObjectId);
        assertEquals("test", readableObjectId.getKey().toString());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new Impl(null);
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new IdKey() {
            @Override
            public String toString() {
                return "test";
            }
        }, new ReadableObjectId(new IdKey() {
            @Override
            public String toString() {
                return "test";
            }
        }) {
            @Override
            public boolean hasReferringProperties() {
                return true;
            }
        });

        try {
            context.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: test", e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new Impl(null);
        Annotated ann = null;
        JsonDeserializer deserializer = context.deserializerInstance(ann, null);
        assertNull(deserializer);

        ann = mock(Annotated.class);
        deserializer = context.deserializerInstance(ann, new StubJsonDeserializer());
        assertNotNull(deserializer);

        ann = mock(Annotated.class);
        Class deserClass = JsonDeserializer.class;
        deserializer = context.deserializerInstance(ann, deserClass);
        assertNotNull(deserializer);

        ann = mock(Annotated.class);
        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        deserializer = context.deserializerInstance(ann, deserClass);
        assertNull(deserializer);

        deserializer = ClassUtil.createInstance(deserClass, context.canOverrideAccessModifiers());
        deserializer = context.deserializerInstance(ann, deserializer);
        assertNotNull(deserializer);

        deserializer = new StubJsonDeserializer();
        deserializer.resolve(context);
        deserializer = context.deserializerInstance(ann, deserializer);
        assertNotNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new Impl(null);
        Annotated ann = null;
        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, null);
        assertNull(deserializer);

        ann = mock(Annotated.class);
        deserializer = context.keyDeserializerInstance(ann, new StubKeyDeserializer());
        assertNotNull(deserializer);

        ann = mock(Annotated.class);
        Class deserClass = KeyDeserializer.class;
        deserializer = context.keyDeserializerInstance(ann, deserClass);
        assertNotNull(deserializer);

        ann = mock(Annotated.class);
        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        deserializer = context.keyDeserializerInstance(ann, deserClass);
        assertNull(deserializer);

        deserializer = ClassUtil.createInstance(deserClass, context.canOverrideAccessModifiers());
        deserializer = context.keyDeserializerInstance(ann, deserializer);
        assertNotNull(deserializer);

        deserializer = new StubKeyDeserializer();
        deserializer.resolve(context);
        deserializer = context.keyDeserializerInstance(ann, deserializer);
        assertNotNull(deserializer);
    }

    private static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public void resolve(DefaultDeserializationContext ctxt) {
        }
    }

    private static class StubKeyDeserializer extends KeyDeserializer {
        @Override
        public void resolve(DefaultDeserializationContext ctxt) {
        }
    }
}