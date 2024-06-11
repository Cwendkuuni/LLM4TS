package com.example.test;

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

import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());

        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Class<?> idType() {
                        return String.class;
                    }

                    @Override
                    public String toString() {
                        return "testIdKey";
                    }
                };
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver() {
            @Override
            public Object resolveId(ObjectIdGenerator.IdKey idKey, Object id) {
                return "testId:" + id;
            }
        };

        ReadableObjectId id = context.findObjectId(new Object(), idGenerator, resolver);
        assertNotNull(id);
        assertEquals("testIdKey", id.getKey().toString());
        assertEquals("testId:null", id.asString());

        String idString = "testId:123";
        ReadableObjectId existingId = new ReadableObjectId(new IdKey() {
            @Override
            public Class<?> idType() {
                return String.class;
            }

            @Override
            public String toString() {
                return "testIdKey";
            }
        });
        existingId.setResolver(resolver);
        context._objectIds.put(existingId.getKey(), existingId);
        ReadableObjectId newId = context.findObjectId(new Object(), idGenerator, resolver);
        assertEquals(existingId, newId);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(new IdKey() {
                @Override
                public Class<?> idType() {
                    return String.class;
                }

                @Override
                public String toString() {
                    return "testIdKey";
                }
            }, new ReadableObjectId(new IdKey() {
                @Override
                public Class<?> idType() {
                    return String.class;
                }

                @Override
                public String toString() {
                    return "testIdKey";
                }
            }));
        }};
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: testIdKey", e.getMessage());
            assertNotNull(e.getIdDeserializers());
            assertEquals(1, e.getIdDeserializers().size());
            JsonDeserializer<?> deserializer = e.getIdDeserializers().get(0);
            assertNotNull(deserializer);
            assertFalse(deserializer.getClass().isAssignableFrom(SimpleObjectIdResolver.class));
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        Annotated ann = mock(Annotated.class);
        Object deserDef = new SimpleJsonDeserializer();

        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(SimpleJsonDeserializer.class, deserializer.getClass());

        deserDef = new Class<SimpleJsonDeserializer>() {};
        deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(SimpleJsonDeserializer.class, deserializer.getClass());

        deserializer = context.deserializerInstance(ann, null);
        assertNull(deserializer);

        deserializer = context.deserializerInstance(ann, new Object());
        assertNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        Annotated ann = mock(Annotated.class);
        Object deserDef = new SimpleKeyDeserializer();

        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(SimpleKeyDeserializer.class, deserializer.getClass());

        deserDef = new Class<SimpleKeyDeserializer>() {};
        deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(SimpleKeyDeserializer.class, deserializer.getClass());

        deserializer = context.keyDeserializerInstance(ann, null);
        assertNull(deserializer);

        deserializer = context.keyDeserializerInstance(ann, new Object());
        assertNull(deserializer);
    }

    private static class SimpleJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class SimpleKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }
}