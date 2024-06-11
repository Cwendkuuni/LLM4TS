package com.example.junit; // replace with your package name

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        ObjectIdGenerator<Object> idGenerator = new StubObjectIdGenerator();
        ObjectIdResolver idResolver = new StubObjectIdResolver();

        ReadableObjectId objectId = context.findObjectId(new Object(), idGenerator, idResolver);

        Assert.assertNotNull(objectId);
    }

    @Test
    public void testFindObjectId_WithNullIdGenerator() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        ObjectIdGenerator<Object> idGenerator = null;
        ObjectIdResolver idResolver = new StubObjectIdResolver();

        try {
            context.findObjectId(new Object(), idGenerator, idResolver);
            Assert.fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference ufe) {
            // Expected
        }
    }

    @Test
    public void testFindObjectId_WithNullIdResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        ObjectIdGenerator<Object> idGenerator = new StubObjectIdGenerator();
        ObjectIdResolver idResolver = null;

        try {
            context.findObjectId(new Object(), idGenerator, idResolver);
            Assert.fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference ufe) {
            // Expected
        }
    }

    @Test
    public void testFindObjectId_WithEmptyObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        context.setObjectIds(Collections.emptyMap());

        ObjectIdGenerator<Object> idGenerator = new StubObjectIdGenerator();
        ObjectIdResolver idResolver = new StubObjectIdResolver();

        try {
            context.findObjectId(new Object(), idGenerator, idResolver);
            Assert.fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference ufe) {
            // Expected
        }
    }

    @Test
    public void testFindObjectId_WithExistingObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        ObjectIdGenerator<Object> idGenerator = new StubObjectIdGenerator();
        ObjectIdResolver idResolver = new StubObjectIdResolver();

        context.setObjectIds(Collections.singletonMap(new ObjectIdGenerator.IdKey(1), new ReadableObjectId(1)));

        Object object = new Object();

        ReadableObjectId objectId = context.findObjectId(object, idGenerator, idResolver);

        Assert.assertNotNull(objectId);
        Assert.assertEquals(1, objectId.getId());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        context.setObjectIds(Collections.singletonMap(new ObjectIdGenerator.IdKey(1), new ReadableObjectId(1)));

        try {
            context.checkUnresolvedObjectId();
            Assert.fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference ufe) {
            // Expected
        }

        context.setObjectIds(new LinkedHashMap<>());

        try {
            context.checkUnresolvedObjectId();
            Assert.fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference ufe) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationConfig config = objectMapper.getDeserializationConfig();

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        Annotated ann = new AnnotatedClass();
        Object deserDef = new JsonDeserializer<>(String.class);

        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, deserDef);

        Assert.assertNull(deserializer);
    }

    @Test
    public void testDeserializerInstance_WithJsonDeserializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationConfig config = objectMapper.getDeserializationConfig();

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        Annotated ann = new AnnotatedClass();
        JsonDeserializer<String> deserializer = new JsonDeserializer<>(String.class);

        JsonDeserializer<Object> deserializerInstance = context.deserializerInstance(ann, deserializer);

        Assert.assertEquals(deserializer.getClass(), deserializerInstance.getClass());
    }

    @Test
    public void testDeserializerInstance_WithClass() {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationConfig config = objectMapper.getDeserializationConfig();

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        Annotated ann = new AnnotatedClass();
        Class<JsonDeserializer<String>> deserializerClass = JsonDeserializer.class;

        JsonDeserializer<Object> deserializerInstance = context.deserializerInstance(ann, deserializerClass);

        Assert.assertNotNull(deserializerInstance);
        Assert.assertEquals(JsonDeserializer.class, deserializerInstance.getClass());
    }

    @Test
    public void testDeserializerInstance_WithDeserializerFactory() {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationConfig config = objectMapper.getDeserializationConfig();

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        Annotated ann = new AnnotatedClass();

        JsonDeserializer<Object> deserializerInstance = context.deserializerInstance(ann, new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return new JsonDeserializer<>(String.class);
            }
        });

        Assert.assertEquals(JsonDeserializer.class, deserializerInstance.getClass());
    }

    @Test
    public void testKeyDeserializerInstance() {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationConfig config = objectMapper.getDeserializationConfig();

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        Annotated ann = new AnnotatedClass();
        Object deserDef = new JsonDeserializer<>(String.class);

        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);

        Assert.assertNull(keyDeserializer);
    }

    @Test
    public void testKeyDeserializerInstance_WithJsonDeserializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationConfig config = objectMapper.getDeserializationConfig();

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        Annotated ann = new AnnotatedClass();
        JsonDeserializer<String> deserializer = new JsonDeserializer<>(String.class);

        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(ann, deserializer);

        Assert.assertEquals(deserializer.getClass(), keyDeserializerInstance.getClass());
    }

    @Test
    public void testKeyDeserializerInstance_WithClass() {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationConfig config = objectMapper.getDeserializationConfig();

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        Annotated ann = new AnnotatedClass();
        Class<KeyDeserializer> deserializerClass = KeyDeserializer.class;

        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(ann, deserializerClass);

        Assert.assertNotNull(keyDeserializerInstance);
        Assert.assertEquals(KeyDeserializer.class, keyDeserializerInstance.getClass());
    }

    @Test
    public void testKeyDeserializerInstance_WithDeserializerFactory() {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationConfig config = objectMapper.getDeserializationConfig();

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(Class<?> deserializerClass, DeserializationConfig config) {
                return null;
            }
        }, null);

        Annotated ann = new AnnotatedClass();

        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(ann, new DeserializerFactory() {
            @Override
            public KeyDeserializer createKeyDeserializer(Class<?> keyDeserializerClass, DeserializationConfig config) {
                return new JsonDeserializer<>(String.class);
            }
        });

        Assert.assertEquals(JsonDeserializer.class, keyDeserializerInstance.getClass());
    }

    private static class StubObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return new ObjectIdGenerator.IdKey(0);
        }
    }

    private static class StubObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
            return this;
        }

        @Override
        public ReadableObjectId idResolution(DefaultDeserializationContext ctxt, Object id) throws JsonMappingException {
            return new ReadableObjectId(0);
        }

        @Override
        public boolean canUseFor(Class<?> type) {
            return true;
        }
    }

    private static class AnnotatedClass {
        // dummy class for Annotated
    }
}