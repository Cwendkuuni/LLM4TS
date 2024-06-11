package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public static class TestDefaultDeserializationContext extends DefaultDeserializationContext.Impl {
        private static final long serialVersionUID = 1L;

        public TestDefaultDeserializationContext(DeserializerFactory df) {
            super(df);
        }

        @Override
        public DefaultDeserializationContext copy() {
            return new TestDefaultDeserializationContext(this);
        }

        @Override
        public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp, InjectableValues values) {
            return new TestDefaultDeserializationContext(this, config, jp, values);
        }

        @Override
        public DefaultDeserializationContext with(DeserializerFactory factory) {
            return new TestDefaultDeserializationContext(this, factory);
        }
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.CLASS;
            }
        };

        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        Object id = new Object();

        ReadableObjectId id1 = ctxt.findObjectId(id, generator, null);
        assertNull(id1);

        id1 = ctxt.findObjectId(id, generator, new SimpleObjectIdResolver());
        assertNotNull(id1);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        ctxt.checkUnresolvedObjectId();

        Map<ObjectIdGenerator.IdKey, ReadableObjectId> map = new LinkedHashMap<>();
        ctxt._objectIds = map;

        ReadableObjectId roid = new ReadableObjectId(IdKey.CLASS, Object.class);
        roid.setResolver(null);
        map.put(IdKey.CLASS, roid);

        try {
            ctxt.checkUnresolvedObjectId();
            fail("should throw UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Annotated ann = null;

        JsonDeserializer<Object> deserInst = ctxt.deserializerInstance(ann, deser);
        assertNotNull(deserInst);
        assertEquals(deser, deserInst);

        deserInst = ctxt.deserializerInstance(ann, (Class<JsonDeserializer<Object>>) (Class<?>) deser.getClass());
        assertNotNull(deserInst);
        assertEquals(deser, deserInst);

        HandlerInstantiator hi = null;
        JsonDeserializer<Object> deserInst1 = ctxt.deserializerInstance(ann, deser.getClass());
        assertNotNull(deserInst1);
        assertEquals(deser, deserInst1);

        if (hi != null) {
            deserInst1 = hi.deserializerInstance(null, ann, deser.getClass());
            assertNotNull(deserInst1);
            assertEquals(deser, deserInst1);
        }

        // test null cases
        deserInst = ctxt.deserializerInstance(ann, null);
        assertNull(deserInst);

        deserInst = ctxt.deserializerInstance(ann, (Class<?>) null);
        assertNull(deserInst);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        KeyDeserializer deser = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Annotated ann = null;

        KeyDeserializer deserInst = ctxt.keyDeserializerInstance(ann, deser);
        assertNotNull(deserInst);
        assertEquals(deser, deserInst);

        Class<KeyDeserializer> keyDeserClass = KeyDeserializer.class;
        deserInst = ctxt.keyDeserializerInstance(ann, keyDeserClass);
        assertNotNull(deserInst);
        assertEquals(deser, deserInst);

        HandlerInstantiator hi = null;
        KeyDeserializer deserInst1 = ctxt.keyDeserializerInstance(ann, keyDeserClass);
        assertNotNull(deserInst1);
        assertEquals(deser, deserInst1);

        if (hi != null) {
            deserInst1 = hi.keyDeserializerInstance(null, ann, keyDeserClass);
            assertNotNull(deserInst1);
            assertEquals(deser, deserInst1);
        }

        // test null cases
        deserInst = ctxt.keyDeserializerInstance(ann, null);
        assertNull(deserInst);

        deserInst = ctxt.keyDeserializerInstance(ann, (Class<?>) null);
        assertNull(deserInst);
    }

    @Test
    public void testWith() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        DeserializerFactory df = null;
        TestDefaultDeserializationContext ctxt1 = ctxt.with(df);
        assertSame(ctxt, ctxt1);

        df = new TestDeserializerFactory();
        ctxt1 = ctxt.with(df);
        assertNotSame(ctxt, ctxt1);

        TestDefaultDeserializationContext ctxt2 = new TestDefaultDeserializationContext(df);
        assertSame(ctxt2, ctxt1);
    }

    @Test
    public void testCreateInstance() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        DeserializationConfig config = null;
        JsonParser jp = null;
        InjectableValues values = null;
        TestDefaultDeserializationContext ctxt1 = ctxt.createInstance(config, jp, values);
        assertSame(ctxt, ctxt1);

        config = new DeserializationConfig();
        jp = createContextualParser();
        values = createContextualInjectableValues();
        ctxt1 = ctxt.createInstance(config, jp, values);
        assertNotSame(ctxt, ctxt1);

        TestDefaultDeserializationContext ctxt2 = new TestDefaultDeserializationContext(config, jp, values);
        assertSame(ctxt2, ctxt1);
    }

    @Test
    public void testFindObjectIdWithObjectIdResolver() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ReadableObjectId idFromObject(Object obj, DeserializationContext ctxt) {
                return null;
            }
        };

        Object id = new Object();

        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.CLASS;
            }
        };

        ReadableObjectId id1 = ctxt.findObjectId(id, generator, resolver);
        assertNull(id1);

        // test null cases
        resolver = null;
        id1 = ctxt.findObjectId(id, generator, resolver);
        assertNull(id1);

        generator = null;
        id1 = ctxt.findObjectId(id, generator, resolver);
        assertNull(id1);
    }

    @Test
    public void testCheckUnresolvedObjectIdWithObjectIdResolver() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.CLASS;
            }
        };

        Map<ObjectIdGenerator.IdKey, ReadableObjectId> map = new LinkedHashMap<>();
        ctxt._objectIds = map;

        ReadableObjectId roid = new ReadableObjectId(IdKey.CLASS, Object.class);
        roid.setResolver(null);
        map.put(IdKey.CLASS, roid);

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ReadableObjectId idFromObject(Object obj, DeserializationContext ctxt) {
                return null;
            }
        };

        ctxt.checkUnresolvedObjectId(generator, null);

        try {
            ctxt.checkUnresolvedObjectId(generator, resolver);
            fail("should throw UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        try {
            ctxt.checkUnresolvedObjectId(generator, resolver, Collections.singletonList(roid));
            fail("should throw UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testCreateInstanceWithObjectIdResolver() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.CLASS;
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ReadableObjectId idFromObject(Object obj, DeserializationContext ctxt) {
                return null;
            }
        };

        DeserializationConfig config = null;
        JsonParser jp = null;
        InjectableValues values = null;
        TestDefaultDeserializationContext ctxt1 = ctxt.createInstance(config, jp, values, generator, resolver);
        assertSame(ctxt, ctxt1);

        config = new DeserializationConfig();
        jp = createContextualParser();
        values = createContextualInjectableValues();
        TestDefaultDeserializationContext ctxt2 = ctxt.createInstance(config, jp, values, generator, resolver);
        assertNotSame(ctxt, ctxt2);
    }

    @Test
    public void testCreateInstanceWithObjectIdResolver_withObjectIds() throws Exception {
        TestDefaultDeserializationContext ctxt = new TestDefaultDeserializationContext(null);

        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.CLASS;
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ReadableObjectId idFromObject(Object obj, DeserializationContext ctxt) {
                return null;
            }
        };

        Map<ObjectIdGenerator.IdKey, ReadableObjectId> map = new LinkedHashMap<>();
        ctxt._objectIds = map;

        ReadableObjectId roid = new ReadableObjectId(IdKey.CLASS, Object.class);
        roid.setResolver(null);
        map.put(IdKey.CLASS, roid);

        DeserializationConfig config = null;
        JsonParser jp = null;
        InjectableValues values = null;

        TestDefaultDeserializationContext ctxt1 = ctxt.createInstance(config, jp, values, generator, resolver);
        assertSame(ctxt, ctxt1);

        config = new DeserializationConfig();
        jp = createContextualParser();
        values = createContextualInjectableValues();

        TestDefaultDeserializationContext ctxt2 = ctxt.createInstance(config, jp, values, generator, resolver);
        assertNotSame(ctxt, ctxt2);

        TestDefaultDeserializationContext ctxt3 = new TestDefaultDeserializationContext(config, jp, values, generator, resolver);
        assertSame(ctxt3, ctxt2);
    }

    static class TestDeserializerFactory implements DeserializerFactory {
        @Override
        public JsonDeserializer<?> createValueDeserializer(Class<?> valueType, JsonDeserializer<?> valueDeserializer, JsonDeserializerProvider deserializers) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> createValueDeserializer(Class<?> valueType, JsonFactory jf, JsonDeserializer<?> contextualDeserializer) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> createValueDeserializer(Class<?> valueType, JsonParser p, DeserializationContext ctxt) throws JsonMappingException {
            return null;
        }

        @Override
        public KeyDeserializer createKeyDeserializer(Class<?> keyType, JsonDeserializer<?> valueDeserializer, JsonDeserializerProvider deserializers) throws JsonMappingException {
            return null;
        }

        @Override
        public KeyDeserializer createKeyDeserializer(Class<?> keyType, JsonFactory jf, JsonDeserializer<?> contextualDeserializer) throws JsonMappingException {
            return null;
        }

        @Override
        public KeyDeserializer createKeyDeserializer(Class<?> keyType, JsonParser p, DeserializationContext ctxt) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> getValueDeserializer(Class<?> valueType, JsonDeserializerProvider deserializers) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> getValueDeserializer(Class<?> valueType, JsonFactory jf) throws JsonMappingException {
            return null;
        }
    }
}