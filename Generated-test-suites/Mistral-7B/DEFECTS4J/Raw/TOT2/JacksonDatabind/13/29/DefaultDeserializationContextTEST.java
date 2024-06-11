package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    private static final ObjectMapper mapper = new ObjectMapper();

    // Test for findObjectId method
    @org.junit.Test
    public void testFindObjectId() throws IOException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig());

        Object Id1 = new Object();
        Object Id2 = new Object();

        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return id.getClass().getName();
                    }
                };
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId idResolve(SerializerProvider serializers, DeserializationContext ctxt, Object id) throws IOException {
                return new ReadableObjectId(ctxt.getConfig().findValueIds().get(0));
            }
        };

        ReadableObjectId id1 = context.findObjectId(Id1, idGenerator, resolver);
        ReadableObjectId id2 = context.findObjectId(Id2, idGenerator, resolver);

        // Basic functionality test
        assertEquals(Id1.getClass().getName(), id1.getKey().id());
        assertEquals(Id2.getClass().getName(), id2.getKey().id());

        // Edge cases
        Object nullId = null;
        ObjectIdGenerator nullIdGenerator = null;
        ObjectIdResolver nullResolver = null;
        try {
            context.findObjectId(nullId, nullIdGenerator, nullResolver);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Object noIdGenerator = new Object();
        ObjectIdResolver noResolver2 = new ObjectIdResolver() {
            @Override
            public ObjectId idResolve(SerializerProvider serializers, DeserializationContext ctxt, Object id) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        try {
            context.findObjectId(Id1, noIdGenerator, noResolver2);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    // Test for checkUnresolvedObjectId method
    @org.junit.Test
    public void testCheckUnresolvedObjectId() throws IOException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig());

        List<Object> list = new ArrayList<>();
        list.add(new Object());
        list.add(new Object());

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new IdKey() {
            @Override
            public String id() {
                return Object.class.getName();
            }
        }, new ReadableObjectId(new ObjectIdGenerator.IdKey() {
            @Override
            public String id() {
                return Object.class.getName();
            }
        }));

        try {
            context.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            // Expected
            assertEquals(2, e.getUnresolvedIds().size());
            for (Entry<Object, Class<?>> entry : e.getUnresolvedIds().entrySet()) {
                assertEquals(Object.class, entry.getValue());
            }
        }

        // Edge cases
        context._objectIds = null;
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            //Expected
        }

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new IdKey() {
            @Override
            public String id() {
                return String.class.getName();
            }
        }, new ReadableObjectId(new ObjectIdGenerator.IdKey() {
            @Override
            public String id() {
                return Object.class.getName();
            }
        }));
        try {
            context.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            //Expected
            assertTrue(e.getMessage().contains("String"));
            assertTrue(e.getMessage().contains("Object"));
        }
    }

    // Test for deserializerInstance method
    @org.junit.Test
    public void testDeserializerInstance() throws IOException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig());

        JsonDeserializer<String> deserializer = new StringDeserializer();

        Annotated annotated = mapper.findValueSerializer(String.class);
        Object deserDef = annotated.getClass();

        JsonDeserializer<String> deser = context.deserializerInstance(annotated, deserDef);

        // Basic functionality test
        assertEquals(StringDeserializer.class, deser.getClass());

        // Edge cases
        Object nullDeserDef = null;
        try {
            deser = context.deserializerInstance(annotated, nullDeserDef);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        Annotated nullAnnotated = null;
        try {
            deser = context.deserializerInstance(nullAnnotated, deserDef);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        Class<JsonDeserializer<String>> nullDeserClass = null;
        try {
            deser = context.deserializerInstance(annotated, nullDeserClass);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Test for keyDeserializerInstance method
    @org.junit.Test
    public void testKeyDeserializerInstance() throws IOException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig());

        KeyDeserializer deserializer = new StringKeyDeserializer();

        Annotated annotated = mapper.findValueSerializer(String.class);
        Object deserDef = annotated.getClass();

        KeyDeserializer deser = context.keyDeserializerInstance(annotated, deserDef);

        // Basic functionality test
        assertEquals(StringKeyDeserializer.class, deser.getClass());

        // Edge cases
        Object nullDeserDef = null;
        try {
            deser = context.keyDeserializerInstance(annotated, nullDeserDef);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        Annotated nullAnnotated = null;
        try {
            deser = context.keyDeserializerInstance(nullAnnotated, deserDef);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        Class<KeyDeserializer<String>> nullDeserClass = null;
        try {
            deser = context.keyDeserializerInstance(annotated, nullDeserClass);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Test for with, createInstance and copy methods
    @org.junit.Test
    public void testConstructorMethods() throws IOException {
        DefaultDeserializationContext context1 = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig());
        DefaultDeserializationContext context2 = context1.with(mapper.getDeserializationConfig().getFactory());

        // Basic functionality test
        assertNotSame(context1, context2);

        DefaultDeserializationContext context3 = context1.copy();
        assertSame(context1, context3);

        DefaultDeserializationContext context4 = context1.createInstance(mapper.getDeserializationConfig(), null, null);
        assertNotSame(context1, context4);
    }

    // Test for toString method
    @org.junit.Test
    public void testToString() throws IOException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig());

        String toString = context.toString();

        // Basic functionality test
        assertNotNull(toString);

        // Edge cases
        context._objectIds = null;
        toString = context.toString();
        assertTrue(toString.contains("DefaultDeserializationContext"));
        assertTrue(toString.contains("no objectIdResolver"));

        context._objectIdResolvers = null;
        toString = context.toString();
        assertTrue(toString.contains("DefaultDeserializationContext"));
        assertTrue(toString.contains("no objectIdResolvers"));
    }

    // Inner class StringDeserializer
    static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    // Inner class StringKeyDeserializer
    static class StringKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}