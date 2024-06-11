package com.example.junit;

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

import java.util.*;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testFindObjectId() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());

        ObjectIdGenerator<Object> idGenerator = new IdGenerator();
        ObjectIdResolver objectIdResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }

            @Override
            public boolean canUseFor(ObjectIdResolver resolverType) {
                return true;
            }
        };

        Object id = new Object();
        ObjectIdGenerator.IdKey key = ObjectIdGenerator.IdKey.USE_CLASS;
        ReadableObjectId readableObjectId = context.findObjectId(id, idGenerator, objectIdResolver);

        assertNotNull(readableObjectId);
        assertEquals(id.getClass(), readableObjectId.getId());
    }

    @org.junit.Test
    public void testFindObjectIdDeprecated() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());

        ObjectIdGenerator<Object> idGenerator = new IdGenerator();

        Object id = new Object();
        ReadableObjectId readableObjectId = context.findObjectId(id, idGenerator);

        assertNotNull(readableObjectId);
        assertEquals(id.getClass(), readableObjectId.getId());
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectId() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());

        Object id1 = new Object();
        Object id2 = new Object();

        context.findObjectId(id1, new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Serializable serializable) {
                return id1;
            }

            @Override
            public ObjectIdGenerator.Key key(Serializable s) {
                return ObjectIdGenerator.IdKey.USE_CLASS;
            }
        }, new SimpleObjectIdResolver());

        context.findObjectId(id2, new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Serializable serializable) {
                return id2;
            }

            @Override
            public ObjectIdGenerator.Key key(Serializable s) {
                return ObjectIdGenerator.IdKey.USE_CLASS;
            }
        }, new SimpleObjectIdResolver());

        context.checkUnresolvedObjectId();
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdUnresolved() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());

        Object id1 = new Object();
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Serializable serializable) {
                return id1;
            }

            @Override
            public ObjectIdGenerator.Key key(Serializable s) {
                return ObjectIdGenerator.IdKey.USE_CLASS;
            }
        };

        context.findObjectId(new Object(), idGenerator, new SimpleObjectIdResolver());

        context.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());

        Annotated ann = mock(Annotated.class);
        Object deserDef = new ObjectDeserializer();

        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, deserDef);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ObjectDeserializer);
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());

        Annotated ann = mock(Annotated.class);
        Object deserDef = new KeyDeserializer();

        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);

        assertNotNull(keyDeserializer);
        assertTrue(keyDeserializer instanceof KeyDeserializer);
    }

    @org.junit.Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());

        DefaultDeserializationContext newContext = context.with(new DefaultDeserializerFactory());

        assertNotSame(context, newContext);
    }

    @org.junit.Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());

        DeserializationConfig config = mock(DeserializationConfig.class);
        JsonParser jp = mock(JsonParser.class);
        InjectableValues values = mock(InjectableValues.class);

        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);

        assertNotSame(context, newContext);
    }

    static class ObjectDeserializer extends JsonDeserializer<Object> {
    }

    static class KeyDeserializer extends JsonDeserializer<Object> {
    }

    static class IdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public Object generateId(Serializable serializable) {
            return serializable;
        }

        @Override
        public ObjectIdGenerator.Key key(Serializable s) {
            return ObjectIdGenerator.IdKey.USE_CLASS;
        }
    }
}

class ObjectDeserializer extends JsonDeserializer<Object> {
}

class KeyDeserializer extends JsonDeserializer<Object> {
}