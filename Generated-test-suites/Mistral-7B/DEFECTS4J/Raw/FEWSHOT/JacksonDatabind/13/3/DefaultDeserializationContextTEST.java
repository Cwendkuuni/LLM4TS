package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    public static void main(String[] args) throws Exception {
        // Run tests here
        junit.runner.JUnitCore.main(DefaultDeserializationContextTest.class.getName());
    }

    @org.junit.Test
    public void testFindObjectId() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        ObjectIdGenerator idGenerator = new IdKey("id");
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        // Test null id
        assertNull(context.findObjectId(null, idGenerator, idResolver));

        // Test valid id
        Object id = new Object();
        ReadableObjectId objectId = context.findObjectId(id, idGenerator, idResolver);
        assertNotNull(objectId);

        // Test same id again
        objectId = context.findObjectId(id, idGenerator, idResolver);
        assertEquals(objectId, context.findObjectId(id, idGenerator, idResolver));

        // Test different id
        Object otherId = new Object();
        objectId = context.findObjectId(otherId, idGenerator, idResolver);
        assertNotNull(objectId);
        assertNotEquals(objectId, context.findObjectId(id, idGenerator, idResolver));

        // Test multiple ids
        List<Object> ids = new ArrayList<>();
        ids.add(id);
        ids.add(otherId);
        Map<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new HashMap<>();
        for (Object id : ids) {
            objectIds.put(idGenerator.key(id), context.findObjectId(id, idGenerator, idResolver));
        }
        for (Entry<ObjectIdGenerator.IdKey, ReadableObjectId> entry : objectIds.entrySet()) {
            assertNotNull(entry.getValue());
            for (Entry<ObjectIdGenerator.IdKey, ReadableObjectId> otherEntry : objectIds.entrySet()) {
                if (!entry.getKey().equals(otherEntry.getKey())) {
                    assertNotEquals(entry.getValue(), otherEntry.getValue());
                }
            }
        }
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectId() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        ObjectIdGenerator idGenerator = new IdKey("id");
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        // Test empty objectIds
        context.checkUnresolvedObjectId();

        // Test no unresolved ids
        Object id1 = new Object();
        Object id2 = new Object();
        context.findObjectId(id1, idGenerator, idResolver);
        context.findObjectId(id2, idGenerator, idResolver);
        context.checkUnresolvedObjectId();

        // Test unresolved ids
        context._objectIds = new HashMap<>();
        Object id3 = new Object();
        context.findObjectId(id3, idGenerator, idResolver);
        context.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Annotated ann = mapper.findConcreteAnnotated(JsonDeserializer.class, Object.class);

        // Test null deserDef
        assertNull(context.deserializerInstance(ann, null));

        // Test null ann
        assertNull(context.deserializerInstance(null, new JsonDeserializer<Object>(Object.class)));

        // Test valid deserDef
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>(Object.class);
        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);

        // Test null ann and valid deserDef
        deserializer = new JsonDeserializer<Object>(Object.class);
        deser = context.deserializerInstance(null, deserializer);
        assertEquals(deserializer, deser);

        // Test valid ann and null deserDef
        deserializer = null;
        deser = context.deserializerInstance(ann, null);
        assertNull(deser);

        // Test valid ann and valid deserDef
        deserializer = new JsonDeserializer<Object>(Object.class);
        deser = context.deserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);

        // Test valid ann, null deserDef, and HandlerInstantiator
        mapper.setHandlerInstantiator(new HandlerInstantiator() {
            @Override
            public <T> T createInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
                return clazz.newInstance();
            }
        });
        deserializer = null;
        deser = context.deserializerInstance(ann, null);
        assertNull(deser);

        // Test valid ann, null deserDef, and JsonDeserializerFactory
        JsonDeserializerFactory factory = new JsonDeserializer.Provider();
        deserializer = factory.findValueDeserializer(Object.class, mapper.getFactory());
        deser = context.deserializerInstance(ann, null);
        assertEquals(deserializer, deser);

        // Test valid ann, null deserDef, and null HandlerInstantiator
        mapper.setHandlerInstantiator(null);
        deserializer = null;
        deser = context.deserializerInstance(ann, null);
        assertNull(deser);

        // Test valid ann, null deserDef, and JsonDeserializerFactory
        JsonDeserializerFactory factory = new JsonDeserializer.Provider();
        deserializer = factory.findValueDeserializer(Object.class, mapper.getFactory());
        deser = context.deserializerInstance(ann, null);
        assertEquals(deserializer, deser);

        // Test valid ann, valid deserDef, and null HandlerInstantiator
        mapper.setHandlerInstantiator(null);
        deserializer = new JsonDeserializer<Object>(Object.class);
        deser = context.deserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);

        // Test valid ann, valid deserDef, and HandlerInstantiator
        JsonDeserializer<Object> deserializer2 = new JsonDeserializer<Object>(Object.class);
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T createInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
                return clazz.newInstance();
            }
        };
        deser = context.deserializerInstance(ann, deserializer2);
        assertEquals(deserializer2, deser);
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Annotated ann = mapper.findConcreteAnnotated(KeyDeserializer.class, Object.class);

        // Test null deserDef
        assertNull(context.keyDeserializerInstance(ann, null));

        // Test null ann
        assertNull(context.keyDeserializerInstance(null, new KeyDeserializer<Object>(Object.class)));

        // Test valid deserDef
        KeyDeserializer<Object> deserializer = new KeyDeserializer<Object>(Object.class);
        KeyDeserializer<Object> deser = context.keyDeserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);

        // Test null ann and valid deserDef
        deserializer = new KeyDeserializer<Object>(Object.class);
        deser = context.keyDeserializerInstance(null, deserializer);
        assertEquals(deserializer, deser);

        // Test valid ann and null deserDef
        deserializer = null;
        deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);

        // Test valid ann and valid deserDef
        deserializer = new KeyDeserializer<Object>(Object.class);
        deser = context.keyDeserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);

        // Test valid ann, null deserDef, and HandlerInstantiator
        mapper.setHandlerInstantiator(new HandlerInstantiator() {
            @Override
            public <T> T createInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
                return clazz.newInstance();
            }
        });
        deserializer = null;
        deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);

        // Test valid ann, null deserDef, and JsonDeserializerFactory
        JsonDeserializerFactory factory = new JsonDeserializer.Provider();
        deserializer = factory.findValueDeserializer(KeyDeserializer.class, mapper.getFactory());
        deser = context.keyDeserializerInstance(ann, null);
        assertEquals(deserializer, deser);

        // Test valid ann, null deserDef, and null HandlerInstantiator
        mapper.setHandlerInstantiator(null);
        deserializer = null;
        deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);

        // Test valid ann, null deserDef, and JsonDeserializerFactory
        JsonDeserializerFactory factory = new JsonDeserializer.Provider();
        deserializer = factory.findValueDeserializer(KeyDeserializer.class, mapper.getFactory());
        deser = context.keyDeserializerInstance(ann, null);
        assertEquals(deserializer, deser);

        // Test valid ann, valid deserDef, and null HandlerInstantiator
        mapper.setHandlerInstantiator(null);
        deserializer = new KeyDeserializer<Object>(Object.class);
        deser = context.keyDeserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);

        // Test valid ann, valid deserDef, and HandlerInstantiator
        KeyDeserializer<Object> deserializer2 = new KeyDeserializer<Object>(Object.class);
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T createInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
                return clazz.newInstance();
            }
        };
        deser = context.keyDeserializerInstance(ann, deserializer2);
        assertEquals(deserializer2, deser);
    }
}