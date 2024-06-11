package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator$IdKeyTest;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;

    @Before
    public void setup() {
        DeserializerFactory factory = new DefaultDeserializerFactory();
        context = new DefaultDeserializationContext.Impl(factory);
    }

    @Test
    public void testFindObjectId() throws JsonProcessingException {
        ObjectIdGenerator<ObjectIdGenerator.IdKey> idGenerator = new IdGenerator();
        ObjectIdResolver idResolver = new ObjectIdResolverImpl();

        context.setObjectIdGenerator(idGenerator);
        context.setObjectIdResolver(idResolver);

        Object id = new Object();
        ObjectIdGenerator.IdKey idKey = ObjectIdGenerator.IdKey.USE_DEFAULT;
        Object expectedId = new ObjectIdGenerator.IdKeyTest.IdKeyTestObj();

        ReadableObjectId actualId = context.findObjectId(id, idGenerator, ObjectIdGenerator.IdKey.class);
        assertEquals(expectedId.hashCode(), actualId.getKey().hashCode());

        IdKey customIdKey = new IdKey("custom");
        actualId = context.findObjectId(id, idGenerator, customIdKey);
        assertNull(actualId);

        actualId = context.findObjectId(expectedId, idGenerator, ObjectIdGenerator.IdKey.class);
        assertEquals(expectedId, actualId.getKey());

        actualId = context.findObjectId(expectedId, idGenerator, IdKey.class);
        assertEquals(expectedId, actualId.getKey());

        actualId = context.findObjectId(expectedId, idGenerator, Object.class);
        assertNull(actualId);

        actualId = context.findObjectId(expectedId, null, ObjectIdGenerator.IdKey.class);
        assertNull(actualId);

        ObjectIdResolver simpleObjectIdResolver = new SimpleObjectIdResolver();
        actualId = context.findObjectId(id, idGenerator, simpleObjectIdResolver.getClass());
        assertNull(actualId);

        actualId = context.findObjectId(expectedId, idGenerator, ObjectIdResolver.class);
        assertNull(actualId);

        actualId = context.findObjectId(expectedId, idGenerator, SimpleObjectIdResolver.class);
        assertNull(actualId);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws JsonProcessingException {
        ObjectIdGenerator<ObjectIdGenerator.IdKey> idGenerator = new IdGenerator();
        ObjectIdResolver idResolver = new ObjectIdResolverImpl();

        context.setObjectIdGenerator(idGenerator);
        context.setObjectIdResolver(idResolver);

        Object id1 = new Object();
        Object id2 = new Object();

        ObjectIdGenerator.IdKey idKey1 = ObjectIdGenerator.IdKey.USE_DEFAULT;
        ObjectIdGenerator.IdKey idKey2 = new IdKey("custom");

        // No object ids to check
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception to be thrown");
        } catch (UnresolvedForwardReference e) {
            // Pass
        }

        // Check unresolved object id with default key
        context._objectIds.put(idKey1, new ReadableObjectId(idKey1));
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("Expected exception with idKey1, but got idKey2: " + e.getMessage());
        }
        context._objectIds.remove(idKey1);

        context._objectIds.put(idKey1, new ReadableObjectId(idKey1));
        context._objectIds.put(idKey2, new ReadableObjectId(idKey2));

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception to be thrown");
        } catch (UnresolvedForwardReference e) {
            // Pass
            assertEquals(idKey2.toString(), e.getPropertyName());
            assertEquals(id2.getClass().getName(), e.getAnnotationType().getName());
        }

        context._objectIds.remove(idKey2);

        // Check unresolved object id with custom key
        context._objectIds.put(idKey1, new ReadableObjectId(idKey1));
        context._objectIds.put(idKey2, new ReadableObjectId(idKey2));

        context._objectIds.put(new IdKey("customKey"), new ReadableObjectId(idKey2));

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception to be thrown");
        } catch (UnresolvedForwardReference e) {
            fail("Expected exception for customKey, but got idKey2: " + e.getMessage());
        }

        context._objectIds.remove(idKey2);
        context._objectIds.remove(new IdKey("customKey"));

        context._objectIds.put(idKey1, new ReadableObjectId(idKey1));
        ReadableObjectId id = new ReadableObjectId(idKey1);
        id.setReferringProperties(new Referring[] { new Referring(Object.class, new Location("test")) });

        context._objectIds.put(idKey1, id);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception to be thrown");
        } catch (UnresolvedForwardReference e) {
            assertEquals(idKey1.toString(), e.getPropertyName());
            assertEquals(Object.class.getName(), e.getAnnotationType().getName());
            assertEquals("test", e.getLocation().getClassName());
        }

        context._objectIds.remove(idKey1);
    }

    @Test
    public void testDeserializerInstance() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Annotated ann = mapper.getTypeFactory().constructType(JsonDeserializer.class);
        Object deserDef = new JsonDeserializer<Object>() {};

        JsonDeserializer<Object> actualDeserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(actualDeserializer);

        ann = mapper.getTypeFactory().constructType(KeyDeserializer.class);
        Object keyDeserDef = new KeyDeserializer() {};

        KeyDeserializer actualKeyDeserializer = context.keyDeserializerInstance(ann, keyDeserDef);
        assertNotNull(actualKeyDeserializer);

        ann = mapper.getTypeFactory().constructType(JsonDeserializer.class.getName());
        deserDef = new JsonDeserializer<Object>() {};

        actualDeserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(actualDeserializer);

        ann = mapper.getTypeFactory().constructType(KeyDeserializer.class.getName());
        keyDeserDef = new KeyDeserializer() {};

        actualKeyDeserializer = context.keyDeserializerInstance(ann, keyDeserDef);
        assertNotNull(actualKeyDeserializer);

        ann = mapper.getTypeFactory().constructType(SimpleObjectIdResolver.class);
        Object idResolverDef = new SimpleObjectIdResolver();

        actualDeserializer = context.deserializerInstance(ann, idResolverDef);
        assertNull(actualDeserializer);

        ann = mapper.getTypeFactory().constructType(ObjectIdGenerator.IdKey.class.getName());
        Object idKeyDef = new ObjectIdGenerator.IdKeyTest.IdKeyTestObj();

        actualDeserializer = context.deserializerInstance(ann, idKeyDef);
        assertNull(actualDeserializer);
    }

    private static class IdGenerator extends ObjectIdGenerator<ObjectIdGenerator.IdKey> {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return ObjectIdGenerator.IdKey.USE_DEFAULT;
        }
    }

    private static class ObjectIdResolverImpl implements ObjectIdResolver {
        @Override
        public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
            return this;
        }

        @Override
        public ObjectId findObjectId(Object id, DeserializationContext ctxt) {
            return new ReadableObjectId(ObjectIdGenerator.IdKey.USE_DEFAULT);
        }
    }
}