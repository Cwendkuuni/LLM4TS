package com.example.test.jackson;

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
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("test");
            }
        };
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext cxt) {
                return null;
            }

            @Override
            public Object idFromObject(Object id) {
                return id;
            }
        };

        ReadableObjectId result = context.findObjectId(new Object(), idGen, resolver);
        assertNull(result);

        _objectIdsSet(context, new IdKey("test"), new ReadableObjectId());
        result = context.findObjectId(new Object(), idGen, resolver);
        assertNotNull(result);
        assertEquals("test", result.getIdKey().key);
    }

    @Test
    public void testFindObjectIdWithResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("test");
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext cxt) {
                return null;
            }

            @Override
            public Object idFromObject(Object id) {
                return id;
            }

            @Override
            public Object resolveId(ObjectId id) {
                return new Object();
            }
        };
        Object id = new Object();
        ReadableObjectId result = context.findObjectId(id, idGen, resolver);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        _objectIdsSet(context, new IdKey("test"), new ReadableObjectId());
        context.checkUnresolvedObjectId();
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithReferringProperties() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        _objectIdsSet(context, new IdKey("test"), new ReadableObjectId());
        context.checkUnresolvedObjectId();
        context.findObjectId(new Object(), new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("test2");
            }
        }, new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext cxt) {
                return null;
            }

            @Override
            public Object idFromObject(Object id) {
                return id;
            }

            @Override
            public Object resolveId(ObjectId id) {
                return new Object();
            }

            @Override
            public List<Referring> referringProperties() {
                return Arrays.asList(new Referring() {
                    @Override
                    public ObjectId getObjectId() {
                        return new ObjectId();
                    }

                    @Override
                    public Class<?> getBeanType() {
                        return Object.class;
                    }

                    @Override
                    public int getLocation() {
                        return 0;
                    }
                });
            }
        });
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        JsonDeserializer deserializer = new JsonDeserializer() {};
        assertEquals(deserializer, context.deserializerInstance(null, deserializer));
    }

    @Test
    public void testDeserializerInstanceWithClass() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        JsonDeserializer deserializer = new JsonDeserializer() {};
        assertEquals(deserializer, context.deserializerInstance(null, JsonDeserializer.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializerInstanceWithInvalidClass() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        context.deserializerInstance(null, String.class);
    }

    @Test
    public void testDeserializerInstanceWithHandlerInstantiator() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T createInstance(Class<?> cls) {
                return null;
            }
        };
        JsonDeserializer deserializer = new JsonDeserializer() {};
        assertEquals(deserializer, context.deserializerInstance(null, JsonDeserializer.class, handlerInstantiator));
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        KeyDeserializer keyDeserializer = new KeyDeserializer() {};
        assertEquals(keyDeserializer, context.keyDeserializerInstance(null, keyDeserializer));
    }

    @Test
    public void testKeyDeserializerInstanceWithClass() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        KeyDeserializer keyDeserializer = new KeyDeserializer() {};
        assertEquals(keyDeserializer, context.keyDeserializerInstance(null, KeyDeserializer.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testKeyDeserializerInstanceWithInvalidClass() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        context.keyDeserializerInstance(null, String.class);
    }

    @Test
    public void testKeyDeserializerInstanceWithHandlerInstantiator() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T createInstance(Class<?> cls) {
                return null;
            }
        };
        KeyDeserializer keyDeserializer = new KeyDeserializer() {};
        assertEquals(keyDeserializer, context.keyDeserializerInstance(null, KeyDeserializer.class, handlerInstantiator));
    }

    private void _objectIdsSet(DefaultDeserializationContext context, IdKey key, ReadableObjectId objectId) {
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(key, objectId);
    }

}