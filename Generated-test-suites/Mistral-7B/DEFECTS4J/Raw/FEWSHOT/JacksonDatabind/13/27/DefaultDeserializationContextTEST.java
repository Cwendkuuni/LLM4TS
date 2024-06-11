package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator idGen = new CustomObjectIdGenerator();
        ObjectIdResolver resolver = new CustomObjectIdResolver();

        DefaultDeserializationContext context = new DefaultDeserializationContext(new DefaultDeserializerFactory(), null) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolverType) {
                return super.findObjectId(id, gen, resolverType);
            }
        };

        context._objectIds = new HashMap<>();
        context._objectIdResolvers = new ArrayList<>();

        ObjectIdKey key = idGen.key("testId");

        Object testObj = new Object();
        JsonDeserializer deserializer = new StubJsonDeserializer();

        ReadableObjectId id1 = context.findObjectId(testObj, idGen, resolver);
        assertNotNull(id1);
        assertEquals(id1.getKey(), key);
        assertEquals(id1.getObjectId(), testObj);
        assertEquals(id1.getResolver(), resolver);

        ReadableObjectId id2 = context.findObjectId(testObj, idGen);
        assertNotNull(id2);
        assertEquals(id2.getKey(), key);
        assertEquals(id2.getObjectId(), testObj);
        assertNull(id2.getResolver());

        ObjectIdGenerator.IdKey key2 = new IdKey("testId2");
        Object testObj2 = new Object();
        ReadableObjectId id3 = context.findObjectId(testObj2, idGen, resolver);
        assertNull(id3);

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator idGen = new CustomObjectIdGenerator();
        ObjectIdResolver resolver = new CustomObjectIdResolver();

        DefaultDeserializationContext context = new DefaultDeserializationContext(new DefaultDeserializerFactory(), null) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolverType) {
                return super.findObjectId(id, gen, resolverType);
            }
        };

        context._objectIds = new HashMap<>();
        context._objectIdResolvers = new ArrayList<>();

        ObjectIdKey key = idGen.key("testId");
        Object testObj = new Object();
        JsonDeserializer deserializer = new StubJsonDeserializer();

        ReadableObjectId id1 = context.findObjectId(testObj, idGen, resolver);
        assertEquals(id1.getKey().key, "testId");
        assertEquals(id1.getObjectId(), testObj);
        assertEquals(id1.getResolver(), resolver);

        context._objectIds.put(key, id1);

        Object testObj2 = new Object();
        ReadableObjectId id2 = context.findObjectId(testObj2, idGen, resolver);
        assertNull(id2);

        context.checkUnresolvedObjectId();

        context._objectIds.put(key, null);
        context._objectIds.put(new IdKey("testId2"), new ReadableObjectId(new IdKey("testId2")));

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(new DefaultDeserializerFactory(), null) {
            @Override
            public JsonDeserializer deserializerInstance(Annotated ann, Object deserDef)
                    throws JsonMappingException {
                return super.deserializerInstance(ann, deserDef);
            }
        };

        Annotated ann = mock(Annotated.class);
        Object deserDef = new CustomJsonDeserializer();
        JsonDeserializer deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(deserializer.getClass(), CustomJsonDeserializer.class);

        ann = mock(Annotated.class);
        deserDef = CustomJsonDeserializer.class;
        deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(deserializer.getClass(), CustomJsonDeserializer.class);

        ann = mock(Annotated.class);
        deserDef = new Class<?>[] { CustomJsonDeserializer.class };
        deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(deserializer.getClass(), CustomJsonDeserializer.class);

        ann = mock(Annotated.class);
        deserDef = null;
        deserializer = context.deserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(new DefaultDeserializerFactory(), null) {
            @Override
            public KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef)
                    throws JsonMappingException {
                return super.keyDeserializerInstance(ann, deserDef);
            }
        };

        Annotated ann = mock(Annotated.class);
        Object deserDef = new CustomKeyDeserializer();
        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(deserializer.getClass(), CustomKeyDeserializer.class);

        ann = mock(Annotated.class);
        deserDef = CustomKeyDeserializer.class;
        deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(deserializer.getClass(), CustomKeyDeserializer.class);

        ann = mock(Annotated.class);
        deserDef = new Class<?>[] { CustomKeyDeserializer.class };
        deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(deserializer.getClass(), CustomKeyDeserializer.class);

        ann = mock(Annotated.class);
        deserDef = null;
        deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @Test
    public void testWith() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(new DefaultDeserializerFactory(), null) {
            @Override
            public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp,
                    InjectableValues values) {
                return super.createInstance(config, jp, values);
            }
        };

        DefaultDeserializationContext newContext = context.with(new DefaultDeserializerFactory());
        assertNotNull(newContext);
        assertNotSame(context, newContext);
        assertEquals(newContext.getFactory(), new DefaultDeserializerFactory());
        assertEquals(newContext.getFactoryClass(), DefaultDeserializerFactory.class);
        assertNull(newContext._objectIds);
        assertNull(newContext._objectIdResolvers);
    }

    @Test
    public void testCreateInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(new DefaultDeserializerFactory(), null) {
            @Override
            public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp,
                    InjectableValues values) {
                return super.createInstance(config, jp, values);
            }
        };

        DeserializationConfig config = mock(DeserializationConfig.class);
        JsonParser jp = mock(JsonParser.class);
        InjectableValues values = mock(InjectableValues.class);

        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);
        assertNotNull(newContext);
        assertNotSame(context, newContext);
        assertEquals(newContext.getFactory(), context.getFactory());
        assertEquals(newContext.getFactoryClass(), context.getFactoryClass());
        assertNull(newContext._objectIds);
        assertNull(newContext._objectIdResolvers);
    }

    private static class CustomObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return new IdKey("testId");
        }
    }

    private static class CustomObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
            return this;
        }
    }

    private static class CustomJsonDeserializer implements JsonDeserializer<Object> {}

    private static class CustomKeyDeserializer implements KeyDeserializer {}

}