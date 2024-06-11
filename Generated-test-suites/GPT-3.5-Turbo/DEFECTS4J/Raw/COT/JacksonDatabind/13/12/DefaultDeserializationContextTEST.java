
package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.ObjectIdGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext defaultDeserializationContext;

    public void setUp() {
        defaultDeserializationContext = new DefaultDeserializationContext.Impl(null, null);
    }

    public void tearDown() {
        defaultDeserializationContext = null;
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator objectIdGenerator = new ObjectIdGenerator() {
            @Override
            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }

            @Override
            public IdKey key(Object key) {
                return null;
            }

            @Override
            public Object generateId(Object forPojo) {
                return null;
            }

            @Override
            public ObjectIdGenerator<Object> forScope(Class<?> scope) {
                return null;
            }

            @Override
            public ObjectIdGenerator<Object> newForSerialization(Object context) {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getDeserializer() {
                return null;
            }
        };
        ObjectIdResolver objectIdResolver = new ObjectIdResolver() {
            @Override
            public void bindItem(IdKey idKey, Object pojo) {

            }

            @Override
            public Object resolveId(IdKey id) {
                return null;
            }

            @Override
            public ObjectIdResolver newForDeserialization(Object context) {
                return null;
            }

            @Override
            public boolean canUseFor(ObjectIdResolver resolverType) {
                return false;
            }
        };
        ReadableObjectId result = defaultDeserializationContext.findObjectId(id, objectIdGenerator, objectIdResolver);
        assertEquals(null, result);
    }

    @Test
    public void testFindObjectIdDeprecated() {
        Object id = new Object();
        ObjectIdGenerator objectIdGenerator = new ObjectIdGenerator() {
            @Override
            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }

            @Override
            public IdKey key(Object key) {
                return null;
            }

            @Override
            public Object generateId(Object forPojo) {
                return null;
            }

            @Override
            public ObjectIdGenerator<Object> forScope(Class<?> scope) {
                return null;
            }

            @Override
            public ObjectIdGenerator<Object> newForSerialization(Object context) {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getDeserializer() {
                return null;
            }
        };
        ReadableObjectId result = defaultDeserializationContext.findObjectId(id, objectIdGenerator);
        assertEquals(null, result);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        defaultDeserializationContext.checkUnresolvedObjectId();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        Annotated ann = null;
        Object deserDef = null;
        JsonDeserializer<Object> result = defaultDeserializationContext.deserializerInstance(ann, deserDef);
        assertEquals(null, result);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        Annotated ann = null;
        Object deserDef = null;
        KeyDeserializer result = defaultDeserializationContext.keyDeserializerInstance(ann, deserDef);
        assertEquals(null, result);
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = null;
        DefaultDeserializationContext result = defaultDeserializationContext.with(factory);
        assertEquals("", result);
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = null;
        JsonParser jp = null;
        InjectableValues values = null;
        DefaultDeserializationContext result = defaultDeserializationContext.createInstance(config, jp, values);
        assertEquals("", result);
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext result = defaultDeserializationContext.copy();
        assertEquals("", result);
    }

}
