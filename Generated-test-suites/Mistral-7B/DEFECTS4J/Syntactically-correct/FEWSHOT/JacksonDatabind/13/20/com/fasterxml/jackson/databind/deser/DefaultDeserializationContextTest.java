package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    public void testFindObjectId() throws Exception {
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return id.toString();
                    }
                };
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId resolveId(DeserializationContext ctxt, ObjectId id) {
                return new ReadableObjectId(id);
            }
        };

        DefaultDeserializationContext ctxt = new DefaultDeserializationContext(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated member, JsonDeserializer<?> deserializer) {
                return deserializer;
            }
        }, new DefaultDeserializationContext.DeserializerCache()) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen) {
                return findObjectId(id, gen, resolver);
            }
        };

        Object id1 = "id1";
        Object id2 = "id2";

        ReadableObjectId id1Object = ctxt.findObjectId(id1, gen, resolver);
        assertNotNull(id1Object);
        assertNotNull(id1Object.getId());
        assertEquals(id1, id1Object.getId());
        assertEquals(id1, id1Object.getIdKey().key);

        Object id3 = null;
        ReadableObjectId id3Object = ctxt.findObjectId(id3, gen, resolver);
        assertNull(id3Object);

        Object id4 = new Object() {
            @Override
            public String toString() {
                return "id4";
            }
        };
        ReadableObjectId id4Object = ctxt.findObjectId(id4, gen, resolver);
        assertNotNull(id4Object);
        assertNotNull(id4Object.getId());
        assertEquals(id4, id4Object.getId());
        assertEquals(id4.toString(), id4Object.getIdKey().key);

        Set<ObjectId> usedIds = new HashSet<>();
        ObjectIdResolver resolver2 = new ObjectIdResolver() {
            @Override
            public ObjectId resolveId(DeserializationContext ctxt, ObjectId id) {
                Object idObj = id.getId();
                if (usedIds.contains(idObj)) {
                    throw new IllegalStateException("Duplicate id: " + idObj);
                }
                usedIds.add(idObj);
                return new ReadableObjectId(id);
            }
        };
        ObjectIdGenerator gen2 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return id.toString();
                    }
                };
            }
        };
        for (int i = 0; i < 10; i++) {
            Object id = "id" + i;
            ReadableObjectId idObject = ctxt.findObjectId(id, gen2, resolver2);
            assertNotNull(idObject);
            assertNotNull(idObject.getId());
            assertEquals(id, idObject.getId());
            assertEquals(id, idObject.getIdKey().key);
        }
    }

    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return id.toString();
                    }
                };
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId resolveId(DeserializationContext ctxt, ObjectId id) {
                return new ReadableObjectId(id);
            }
        };

        DefaultDeserializationContext ctxt = new DefaultDeserializationContext(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated member, JsonDeserializer<?> deserializer) {
                return deserializer;
            }
        }, new DefaultDeserializationContext.DeserializerCache()) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen) {
                return findObjectId(id, gen, resolver);
            }
        };

        Object id1 = "id1";
        Object id2 = "id2";
        ctxt.findObjectId(id1, gen, resolver);
        ctxt.findObjectId(id2, gen, resolver);

        // Check no error when no unresolved references
        ctxt.checkUnresolvedObjectId();

        Object id3 = "id3";
        Object id4 = "id4";

        // Add unresolved reference
        ObjectId id3ObjectId = ctxt.findObjectId(id3, gen, resolver);
        assertNotNull(id3ObjectId);
        ctxt.findObjectId(id4, gen, resolver);

        try {
            ctxt.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            List<ObjectId> ids = Arrays.asList(id3ObjectId.getId());
            Set<Class<?>> types = new HashSet<>();
            for (Referring r : id3ObjectId.referringProperties()) {
                types.add(r.getBeanType());
            }
            assertEquals(ids, e.getUnresolvedIds().keySet());
            assertEquals(types, e.getUnresolvedIds().values());
        }

        // Clear unresolved reference
        ctxt.findObjectId(id3, gen, resolver);
        ctxt.checkUnresolvedObjectId();
    }

    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdGenerator gen = mapper.getConfig().getObjectMapperConfig().getObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };

        JsonDeserializer<Number> numberDeserializer = new JsonDeserializer<Number>() {
            @Override
            public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsNumber();
            }
        };

        DefaultDeserializationContext ctxt = new DefaultDeserializationContext(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated member, JsonDeserializer<?> deserializer) {
                return deserializer;
            }
        }, new DefaultDeserializationContext.DeserializerCache()) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen) {
                return findObjectId(id, gen, resolver);
            }
        };

        JsonDeserializer<String> stringDeserializerInstance = ctxt.deserializerInstance(createAnnotated(String.class), stringDeserializer);
        assertEquals(stringDeserializer, stringDeserializerInstance);

        JsonDeserializer<Number> numberDeserializerInstance = ctxt.deserializerInstance(createAnnotated(Number.class), numberDeserializer);
        assertEquals(numberDeserializer, numberDeserializerInstance);

        try {
            ctxt.deserializerInstance(createAnnotated(String.class), null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            ctxt.deserializerInstance(createAnnotated(Number.class), new Class[0]);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdGenerator gen = mapper.getConfig().getObjectMapperConfig().getObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        KeyDeserializer keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };

        DefaultDeserializationContext ctxt = new DefaultDeserializationContext(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated member, JsonDeserializer<?> deserializer) {
                return deserializer;
            }
        }, new DefaultDeserializationContext.DeserializerCache()) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen) {
                return findObjectId(id, gen, resolver);
            }
        };

        KeyDeserializer keyDeserializerInstance = ctxt.keyDeserializerInstance(createAnnotated(String.class), keyDeserializer);
        assertEquals(keyDeserializer, keyDeserializerInstance);
    }

    private ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolver) {
        return ctxt.findObjectId(id, gen, resolver);
    }

    private Annotated createAnnotated(Class<?> type) {
        return mock(Annotated.class);
    }
}