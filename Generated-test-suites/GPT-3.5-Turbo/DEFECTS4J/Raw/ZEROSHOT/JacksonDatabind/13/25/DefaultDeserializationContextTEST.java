package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext deserializationContext;

    @Before
    public void setUp() {
        deserializationContext = new DefaultDeserializationContext.Impl(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createMapDeserializer(DeserializationContext ctxt, MapType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createKeyDeserializer(DeserializationContext ctxt, JavaType type) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createCollectionDeserializer(DeserializationContext ctxt, CollectionType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createReferenceDeserializer(DeserializationContext ctxt, ReferenceType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, JavaType nodeType, BeanDescription beanDesc) {
                return null;
            }
        });
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<IdKey>(IdKey.class) {
            @Override
            public IdKey key(Object id) {
                return null;
            }

            @Override
            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }

            @Override
            public ObjectIdGenerator.Using<?> forScope(Class<?> scope) {
                return null;
            }

            @Override
            public JsonDeserializer<Object> newForDeserialization(Object context) {
                return null;
            }

            @Override
            public Object generateId(Object forPojo) {
                return null;
            }
        };

        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        ReadableObjectId readableObjectId = deserializationContext.findObjectId("", generator, resolverType);
        Assert.assertNotNull(readableObjectId);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        deserializationContext.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        ObjectMapper objectMapper = new ObjectMapper();
        Object object = deserializationContext.deserializerInstance(null, objectMapper);
        Assert.assertNull(object);
    }

    @Test
    public void testKeyDeserializerInstance() {
        ObjectMapper objectMapper = new ObjectMapper();
        KeyDeserializer keyDeserializer = deserializationContext.keyDeserializerInstance(null, objectMapper);
        Assert.assertNull(keyDeserializer);
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createMapDeserializer(DeserializationContext ctxt, MapType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createKeyDeserializer(DeserializationContext ctxt, JavaType type) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createCollectionDeserializer(DeserializationContext ctxt, CollectionType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createReferenceDeserializer(DeserializationContext ctxt, ReferenceType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, JavaType nodeType, BeanDescription beanDesc) {
                return null;
            }
        };

        DefaultDeserializationContext defaultDeserializationContext = deserializationContext.with(factory);
        Assert.assertNotNull(defaultDeserializationContext);
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = new DeserializationConfig(null, null);
        JsonParser jp = new JsonParser(null);
        InjectableValues values = new InjectableValues.Std();
        DefaultDeserializationContext defaultDeserializationContext = deserializationContext.createInstance(config, jp, values);
        Assert.assertNotNull(defaultDeserializationContext);
    }

    @Test
    public void testImpl() {
        DeserializerFactory factory = new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createMapDeserializer(DeserializationContext ctxt, MapType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createKeyDeserializer(DeserializationContext ctxt, JavaType type) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createCollectionDeserializer(DeserializationContext ctxt, CollectionType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createReferenceDeserializer(DeserializationContext ctxt, ReferenceType type, BeanDescription beanDesc) {
                return null;
            }

            @Override
            public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, JavaType nodeType, BeanDescription beanDesc) {
                return null;
            }
        };

        DefaultDeserializationContext.Impl impl = new DefaultDeserializationContext.Impl(factory);
        DefaultDeserializationContext.Impl copy = impl.copy();
        Assert.assertNotNull(copy);
        DefaultDeserializationContext.Impl deserializationContext = impl.createInstance(null, null, null);
        Assert.assertNotNull(deserializationContext);
        DefaultDeserializationContext.Impl with = impl.with(factory);
        Assert.assertNotNull(with);
    }
}