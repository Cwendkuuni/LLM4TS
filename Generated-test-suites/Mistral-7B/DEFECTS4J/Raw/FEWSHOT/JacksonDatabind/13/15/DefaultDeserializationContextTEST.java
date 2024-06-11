package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.DefaultDeserializationContextBuilder.*;
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

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("testId");
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }
        };

        DefaultDeserializationContext ctxt = newBuilder()
                .with(new DeserializerFactory() {})
                .withoutSerializerCache()
                .build();

        ctxt.findObjectId(null, generator, null);
        ctxt.findObjectId(null, generator, resolver);

        ObjectIdGenerator generator2 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("testId2");
            }
        };

        ctxt.findObjectId("test", generator2, null);
        ctxt.findObjectId("test", generator2, resolver);

        Object id = new Object();
        ctxt.findObjectId(id, generator2, resolver);

        assertEquals("testId", ctxt._objectIds.get(new IdKey("testId")).getKey().getKey());
        assertEquals("testId2", ctxt._objectIds.get(new IdKey("testId2")).getKey().getKey());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("testId");
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }
        };

        DefaultDeserializationContext ctxt = newBuilder()
                .with(new DeserializerFactory() {})
                .withoutSerializerCache()
                .build();

        ctxt.findObjectId(new Object(), generator, resolver);

        ctxt.checkUnresolvedObjectId();
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithUnresolvedRefs() {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("testId");
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }
        };

        DefaultDeserializationContext ctxt = newBuilder()
                .with(new DeserializerFactory() {})
                .withoutSerializerCache()
                .build();

        ctxt.findObjectId(new Object(), generator, resolver);

        ReadableObjectId roid = ctxt.findObjectId(new Object(), generator, resolver);
        roid.setResolver(resolver);
        ctxt._objectIds.put(new IdKey("testId"), roid);

        ctxt.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        JsonDeserializer deserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        HandlerInstantiator handlerInstantiator = mapper.getHandlerInstantiator();

        DefaultDeserializationContext ctxt = newBuilder()
                .with(new DeserializerFactory() {})
                .withoutSerializerCache()
                .build();

        JsonDeserializer deser = ctxt.deserializerInstance(new AnnotatedClass(), deserializer);
        assertEquals(deserializer, deser);

        JsonDeserializer deser2 = ctxt.deserializerInstance(new AnnotatedClass(), handlerInstantiator.deserializerInstance(mapper.getDeserializationConfig(), new AnnotatedClass(), JsonDeserializer.class));
        assertEquals(deserializer, deser2);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        KeyDeserializer deserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        HandlerInstantiator handlerInstantiator = mapper.getHandlerInstantiator();

        DefaultDeserializationContext ctxt = newBuilder()
                .with(new DeserializerFactory() {})
                .withoutSerializerCache()
                .build();

        KeyDeserializer deser = ctxt.keyDeserializerInstance(new AnnotatedClass(), deserializer);
        assertEquals(deserializer, deser);

        KeyDeserializer deser2 = ctxt.keyDeserializerInstance(new AnnotatedClass(), handlerInstantiator.keyDeserializerInstance(mapper.getDeserializationConfig(), new AnnotatedClass(), KeyDeserializer.class));
        assertEquals(deserializer, deser2);
    }
}

class AnnotatedClass {
    @JsonDeserialize
    public static class InnerClass {

    }
}