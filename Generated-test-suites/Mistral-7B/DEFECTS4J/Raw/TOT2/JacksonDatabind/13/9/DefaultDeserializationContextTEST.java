package com.fasterxml.jackson.databind.deser.test;

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
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for DefaultDeserializationContext
 */
public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private DefaultDeserializationContext contextWithEmptyCache;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
        contextWithEmptyCache = new DefaultDeserializationContext(context, null, null, null);
    }

    // Test findObjectId method

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator idGenerator = new IdKey("testKey");
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        Object id = new Object();

        // Test with existing objectId in the cache
        context.findObjectId(id, idGenerator, idResolver);
        context.checkUnresolvedObjectId();

        // Test with new objectId
        Object newId = new Object();
        ReadableObjectId objectId = context.findObjectId(newId, idGenerator, idResolver);
        assertEquals(idGenerator.key(newId), objectId.getKey().key());
        assertNull(objectId.getResolver());

        // Test with null id
        objectId = context.findObjectId(null, idGenerator, idResolver);
        assertNull(objectId);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testFindObjectIdWithUnresolvedForwardReference() {
        ObjectIdGenerator idGenerator = new IdKey("testKey");
        Object id = new Object();
        context.findObjectId(id, idGenerator, null);
        context.checkUnresolvedObjectId();
    }

    // Test copy method

    @Test
    public void testCopy() {
        assertNotSame(context, context.copy());
        assertEquals(context.getClass(), context.copy().getClass());

        contextWithEmptyCache.copy(); // should throw an exception
    }

    // Test deserializerInstance method

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        JsonParser parser = null;
        Annotated ann = null;
        Object deserDef = null;

        // Test with null deserDef
        JsonDeserializer deserializer = context.deserializerInstance(ann, deserDef);
        assertNull(deserializer);

        // Test with JsonDeserializer instance
        JsonDeserializer deserializer1 = new StubJsonDeserializer();
        deserializer = context.deserializerInstance(ann, deserializer1);
        assertEquals(deserializer1, deserializer);

        // Test with Class<JsonDeserializer>
        Class<JsonDeserializer> deserializerClass = JsonDeserializer.class;
        deserializer = context.deserializerInstance(ann, deserializerClass);
        assertTrue(JsonDeserializer.class.isAssignableFrom(deserializer.getClass()));

        // Test with custom HandlerInstantiator
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public JsonDeserializer<?> deserializerInstance(ObjectContext context, Annotated ann, Class<?> type) throws InstantiationException {
                return new StubJsonDeserializer();
            }
        };
        context.setHandlerInstantiator(handlerInstantiator);
        deserializer = context.deserializerInstance(ann, deserDef);
        assertEquals(StubJsonDeserializer.class, deserializer.getClass());
    }

    // Test keyDeserializerInstance method

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        Annotated ann = null;
        Object deserDef = null;

        // Test with null deserDef
        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNull(keyDeserializer);

        // Test with JsonDeserializer instance
        KeyDeserializer keyDeserializer1 = new StubKeyDeserializer();
        keyDeserializer = context.keyDeserializerInstance(ann, keyDeserializer1);
        assertEquals(keyDeserializer1, keyDeserializer);

        // Test with Class<KeyDeserializer>
        Class<KeyDeserializer> keyDeserializerClass = KeyDeserializer.class;
        keyDeserializer = context.keyDeserializerInstance(ann, keyDeserializerClass);
        assertTrue(KeyDeserializer.class.isAssignableFrom(keyDeserializer.getClass()));

        // Test with custom HandlerInstantiator
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public KeyDeserializer keyDeserializerInstance(ObjectContext context, Annotated ann, Class<?> type) throws InstantiationException {
                return new StubKeyDeserializer();
            }
        };
        context.setHandlerInstantiator(handlerInstantiator);
        keyDeserializer = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(StubKeyDeserializer.class, keyDeserializer.getClass());
    }

    // Test with method

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = null;
        InjectableValues values = null;

        // Test with null config
        assertNull(context.createInstance(null, jp, values));

        // Test with null jp
        assertNull(context.createInstance(config, null, values));

        // Test with null values
        assertNull(context.createInstance(config, jp, null));

        // Test with valid arguments
        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);
        assertNotSame(context, newContext);
        assertEquals(context.getClass(), newContext.getClass());
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = new StubDeserializerFactory();
        assertNotNull(context.with(factory));
        assertEquals(context.getClass(), context.with(factory).getClass());
    }

    // Helper classes

    static class StubJsonDeserializer extends JsonDeserializer<Object> {
    }

    static class StubKeyDeserializer extends KeyDeserializer {
    }

    static class StubDeserializerFactory implements DeserializerFactory {
        @Override
        public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated ann, Class<?> type) throws InstantiationException {
            return new StubJsonDeserializer();
        }

        @Override
        public KeyDeserializer createKeyDeserializer(DeserializationConfig config, Annotated ann, Class<?> type) throws InstantiationException {
            return new StubKeyDeserializer();
        }
    }
}