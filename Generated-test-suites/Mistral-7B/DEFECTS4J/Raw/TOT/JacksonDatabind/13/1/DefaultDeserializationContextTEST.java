package com.fasterxml.jackson.databind.deser.test;

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
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.deser.impl.PropertyNameDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    public void testObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[]");
        Impl context = mapper.getFactory().createContext(jp);
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(1);
            }
        };
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @SuppressWarnings("unchecked")
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }
        };
        ReadableObjectId id = context.findObjectId(new Object() {
        }, idGen, idResolver);
        assertNotNull(id);
        assertEquals(idGen.key(new Object()), id.getKey());
    }

    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[]");
        Impl context = mapper.getFactory().createContext(jp);
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new PropertyNameDeserializer();
            }
        };
        Annotated ann = mock(Annotated.class);
        Object deserDef = new String("com.fasterxml.jackson.databind.deser.PropertyNameDeserializer");
        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertTrue(deser instanceof PropertyNameDeserializer);
    }

    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[]");
        Impl context = mapper.getFactory().createContext(jp);
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new PropertyNameDeserializer();
            }
        };
        Annotated ann = mock(Annotated.class);
        Object deserDef = new String("com.fasterxml.jackson.databind.deser.PropertyNameDeserializer");
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertTrue(deser instanceof PropertyNameDeserializer);
    }

    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[]");
        Impl context = mapper.getFactory().createContext(jp);
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(1);
            }
        };
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }
        };
        Object obj = new Object();
        ReadableObjectId id = context.findObjectId(obj, idGen, idResolver);
        assertNotNull(id);
        assertEquals(idGen.key(obj), id.getKey());
        assertEquals(idResolver, id.getResolver());
    }

    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[]");
        Impl context = mapper.getFactory().createContext(jp);
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(1);
            }
        };
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }
        };
        Object id1 = new Object();
        Object id2 = new Object();
        context.findObjectId(id1, idGen, idResolver);
        context.findObjectId(id2, idGen, idResolver);
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    public void testCopy() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[]");
        Impl context = mapper.getFactory().createContext(jp);
        Impl copy = context.copy();
        assertEquals(context, copy);
        assertNotSame(context, copy);
    }
}