package com.example.jackson.test;

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

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for DefaultDeserializationContext.
 */
public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
    }

    /**
     * Test method for {@link DefaultDeserializationContext#DefaultDeserializationContext(DeserializerFactory, DeserializerCache)}.
     */
    @Test
    public void testDefaultDeserializationContextConstructor() {
        DeserializerFactory df = mapper.getDeserializerFactory();
        DefaultDeserializationContext newContext = new DefaultDeserializationContext(df, null);
        assertNotNull(newContext);
        assertEquals(context.getClass(), newContext.getClass());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#DefaultDeserializationContext(DefaultDeserializationContext, DeserializationConfig, JsonParser, InjectableValues)}.
     */
    @Test
    public void testDefaultDeserializationContextCopyConstructor() {
        DefaultDeserializationContext newContext = context.copy();
        assertNotNull(newContext);
        assertEquals(context.getClass(), newContext.getClass());
        assertNotSame(context, newContext);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#DefaultDeserializationContext(DefaultDeserializationContext)}.
     */
    @Test
    public void testDefaultDeserializationContextEmptyConstructor() {
        DefaultDeserializationContext newContext = new DefaultDeserializationContext(context);
        assertNotNull(newContext);
        assertEquals(context.getClass(), newContext.getClass());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#DefaultDeserializationContext(DefaultDeserializationContext, DeserializerFactory)}.
     */
    @Test
    public void testDefaultDeserializationContextFactoryConstructor() {
        DefaultDeserializationContext newContext = new DefaultDeserializationContext(context, mapper.getDeserializerFactory());
        assertNotNull(newContext);
        assertEquals(context.getClass(), newContext.getClass());
        assertNotSame(context, newContext);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#copy()}.
     */
    @Test(expected = IllegalStateException.class)
    public void testDefaultDeserializationContextCopyMethod() {
        context.copy();
    }

    /**
     * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator)}.
     */
    @Test
    public void testFindObjectId() {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String getIdKey() {
                        return "test";
                    }
                };
            }
        };
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        ReadableObjectId objectId1 = context.findObjectId(new Object(), idGenerator, idResolver);
        assertNotNull(objectId1);
        ObjectIdGenerator idGenerator2 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return objectId1.getKey();
            }
        };
        ReadableObjectId objectId2 = context.findObjectId(new Object(), idGenerator2, idResolver);
        assertSame(objectId1, objectId2);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator)}.
     */
    @Test
    public void testFindObjectIdNullId() {
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        ReadableObjectId objectId = context.findObjectId(null, new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String getIdKey() {
                        return "test";
                    }
                };
            };
        }, idResolver);
        assertNull(objectId);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#checkUnresolvedObjectId()}.
     */
    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String getIdKey() {
                        return "test";
                    }
                };
            }
        };
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        context.findObjectId(new Object(), idGenerator, idResolver);
        context.findObjectId(new Object(), idGenerator, idResolver);
        context.findObjectId(new Object(), idGenerator, idResolver);
        context.checkUnresolvedObjectId();
    }

    /**
     * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)}.
     */
    @Test
    public void testDeserializerInstance() throws JsonProcessingException {
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };
        Annotated annotated = mapper.findValueSerializer(String.class);
        JsonDeserializer<String> deserializerInstance = context.deserializerInstance(annotated, deserializer);
        assertEquals(deserializer.getClass(), deserializerInstance.getClass());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)}.
     */
    @Test
    public void testKeyDeserializerInstance() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };
        Annotated annotated = mapper.findValueSerializer(String.class);
        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(annotated, keyDeserializer);
        assertEquals(keyDeserializer.getClass(), keyDeserializerInstance.getClass());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#with(DeserializerFactory)}.
     */
    @Test
    public void testWith() {
        DeserializerFactory deserializerFactory = mapper.getDeserializerFactory();
        DefaultDeserializationContext newContext = context.with(deserializerFactory);
        assertNotSame(context, newContext);
        assertEquals(context.getClass(), newContext.getClass());
    }
}