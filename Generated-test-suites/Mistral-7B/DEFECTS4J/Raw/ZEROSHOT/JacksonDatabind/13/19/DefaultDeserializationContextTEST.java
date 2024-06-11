package com.example.jackson.test;

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
import com.fasterxml.jackson.test.base.TestBase;

import java.util.*;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest extends TestBase {

    @SuppressWarnings("unchecked")
    private DefaultDeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }

    @Test
    public void testFindObjectId() throws Exception {
        DefaultDeserializationContext context = createContext();

        ObjectIdGenerator<Integer> idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(Integer.class, "id");
            }
        };

        Object id = new Integer(123);
        ReadableObjectId objectId = context.findObjectId(id, idGenerator, null);
        assertNotNull(objectId);
        assertEquals(IdKey.class, objectId.getClass());
        assertEquals("id", objectId.getKey().getName());
        assertEquals(Integer.class, objectId.getKey().getRawClass());
        assertEquals(id, objectId.getId());

        objectId = context.findObjectId(id, idGenerator, SimpleObjectIdResolver.instance);
        assertNotNull(objectId);
        assertEquals(ReadableObjectId.class, objectId.getClass());
        assertNull(objectId.getResolver());

        context.findObjectId(null, idGenerator, SimpleObjectIdResolver.instance);
        assertNotNull(context._objectIds);
        assertTrue(context._objectIds.isEmpty());
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        DefaultDeserializationContext context = createContext();
        context.findObjectId(new Object(), new ObjectIdGenerator<Object>() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(String.class, "id");
            }
        }, null);

        try {
            context.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            assertEquals(1, e.getIds().size());
            assertEquals("id", e.getIds().get(0).getName());
            assertEquals(String.class, e.getIds().get(0).getRawClass());
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = createContext();
        ObjectMapper mapper = new ObjectMapper();

        Annotated ann = mapper.getFactory().findValueTypeAnnotation(String.class, String.class);
        JsonDeserializer<String> deserializer = context.deserializerInstance(ann, StringDeserializer.class);
        assertNotNull(deserializer);
        assertEquals(StringDeserializer.class, deserializer.getClass());

        ann = mapper.getFactory().findValueTypeAnnotation(List.class, ArrayList.class);
        JsonDeserializer<List<String>> listDeserializer = context.deserializerInstance(ann, ArrayListDeserializer.class);
        assertNotNull(listDeserializer);
        assertEquals(ArrayListDeserializer.class, listDeserializer.getClass());

        ann = mapper.getFactory().findValueTypeAnnotation(Map.class, HashMap.class);
        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, HashMapKeySerializer.class);
        assertNotNull(keyDeserializer);
        assertEquals(HmapKeySerializer.class, keyDeserializer.getClass());
    }

    @Test
    public void testCreateInstance() throws Exception {
        DefaultDeserializationContext context = createContext();
        ObjectMapper mapper = new ObjectMapper();

        DefaultDeserializationContext newContext = context.createInstance(null, null, null);
        assertNotSame(context, newContext);
        assertNull(newContext._objectIds);
        assertNull(newContext._objectIdResolvers);
    }

    @Test
    public void testWith() throws Exception {
        DefaultDeserializationContext context = createContext();

        DefaultDeserializationContext newContext = context.with(new DefaultDeserializationContext.Impl.Builder()
                .withoutAnnotationIntrospector()
                .withoutBeanDescription()
                .build());
        assertNotSame(context, newContext);
        assertNull(newContext._objectIds);
        assertNull(newContext._objectIdResolvers);
    }

    private static class StringDeserializer extends JsonDeserializer<String> {}
    private static class ArrayListDeserializer extends JsonDeserializer<List<String>> {}
    private static class HashMapKeySerializer extends KeyDeserializer {}
}