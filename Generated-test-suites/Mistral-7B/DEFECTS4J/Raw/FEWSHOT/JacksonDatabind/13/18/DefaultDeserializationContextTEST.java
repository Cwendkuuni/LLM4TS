package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.util.*;

public class DefaultDeserializationContextTest extends TestBase {

    @Test
    public void testFindObjectId() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext();

        // Test with null id
        assertNull(ctxt.findObjectId(null, null));

        // Test with simple id
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.PROPERTY;
            }
        };
        Object id = new Object();
        ReadableObjectId idResult = ctxt.findObjectId(id, idGen, null);
        assertNotNull(idResult);
        assertEquals(id, idResult.getId());

        // Test with multiple id resolvers
        List<ObjectIdResolver> resolvers = new ArrayList<>();
        resolvers.add(new SimpleObjectIdResolver());
        resolvers.add(new SimpleObjectIdResolver());
        ctxt = new DefaultDeserializationContext(ctxt, null, null, null, resolvers);
        idResult = ctxt.findObjectId(id, idGen, null);
        assertNotNull(idResult);
        assertEquals(id, idResult.getId());

        // Test with custom id resolver
        ObjectIdResolver customResolver = new ObjectIdResolver() {
            @Override
            public ObjectId idFromObject(Object id, ObjectIdGenerator generator) {
                return new ObjectId("customId-" + id.hashCode());
            }
        };
        resolvers = new ArrayList<>();
        resolvers.add(customResolver);
        ctxt = new DefaultDeserializationContext(ctxt, null, null, null, resolvers);
        idResult = ctxt.findObjectId(id, idGen, null);
        assertNotNull(idResult);
        assertNotEquals(id, idResult.getId());
        assertEquals("customId-" + id.hashCode(), idResult.getId().toString());

        // Test with missing id and failing on unresolved id
        ctxt = new DefaultDeserializationContext(ctxt, null, null, null);
        try {
            ctxt.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }

        // Test with missing id and not failing on unresolved id
        ctxt = new DefaultDeserializationContext(ctxt, null, null, null);
        ctxt.with(new DeserializationFeature.FailOnEmptyOrUnresolvedObjectId(false));
        assertDoesNotThrow(() -> ctxt.checkUnresolvedObjectId());
    }

    @Test
    public void testDeserializerInstance() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext();

        // Test with null definition
        assertNull(ctxt.deserializerInstance(null, null));

        // Test with simple deserializer
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {};
        JsonDeserializer<String> deserializerResult = ctxt.deserializerInstance(null, deserializer);
        assertEquals(deserializer, deserializerResult);

        // Test with class definition
        JsonDeserializer<String> deserializerClass = JsonDeserializer.None.class;
        JsonDeserializer<String> deserializerResultClass = ctxt.deserializerInstance(null, deserializerClass);
        assertNull(deserializerResultClass);

        // Test with custom deserializer factory
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated ann, Class<?> deserClass) throws JsonMappingException {
                return new JsonDeserializer<String>() {};
            }
        };
        ctxt = new DefaultDeserializationContext(ctxt, handlerInstantiator);
        deserializerResult = ctxt.deserializerInstance(null, deserializerClass);
        assertEquals(deserializer, deserializerResult);

        // Test with missing deserializer definition
        ctxt = new DefaultDeserializationContext(ctxt, null);
        try {
            ctxt.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext();

        // Test with null definition
        assertNull(ctxt.keyDeserializerInstance(null, null));

        // Test with simple key deserializer
        KeyDeserializer keyDeserializer = new KeyDeserializer() {};
        KeyDeserializer keyDeserializerResult = ctxt.keyDeserializerInstance(null, keyDeserializer);
        assertEquals(keyDeserializer, keyDeserializerResult);

        // Test with class definition
        KeyDeserializer keyDeserializerClass = KeyDeserializer.None.class;
        KeyDeserializer keyDeserializerResultClass = ctxt.keyDeserializerInstance(null, keyDeserializerClass);
        assertNull(keyDeserializerResultClass);

        // Test with custom key deserializer factory
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated ann, Class<?> keyDeserializerClass) throws JsonMappingException {
                return new KeyDeserializer() {};
            }
        };
        ctxt = new DefaultDeserializationContext(ctxt, handlerInstantiator);
        keyDeserializerResult = ctxt.keyDeserializerInstance(null, keyDeserializerClass);
        assertEquals(keyDeserializer, keyDeserializerResult);

        // Test with missing key deserializer definition
        ctxt = new DefaultDeserializationContext(ctxt, null);
        try {
            ctxt.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }
}