package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
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

public class DefaultDeserializationContextTest {

    @Test
    public void testEmptyContext() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertNull(context._objectIds);
        assertNull(context._objectIdResolvers);
    }

    @Test
    public void testCreateContext() {
        DeserializerFactory df = new SimpleDeserializerProvider();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(df, null);
        assertNotNull(context._objectIds);
        assertNull(context._objectIdResolvers);
    }

    @Test
    public void testCreateContextWithResolvers() {
        DeserializerFactory df = new SimpleDeserializerProvider();
        DefaultDeserializationContext src = new DefaultDeserializationContext.Impl(df, null);
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(src, null, null, null);
        assertNotNull(context._objectIds);
        assertNotNull(context._objectIdResolvers);
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator idGen = new KeyValueObjectIdGenerator();
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ReadableObjectId objectId = context.findObjectId(new Object(), idGen, idResolver);
        assertNull(objectId);

        _setUpObjectIdsAndResolvers(context, idGen, idResolver);
        objectId = context.findObjectId(new Object(), idGen, idResolver);
        assertNotNull(objectId);
    }

    @Test
    public void testFindObjectIdUsingSimpleResolver() {
        ObjectIdGenerator idGen = new KeyValueObjectIdGenerator();
        SimpleObjectIdResolver idResolver = new SimpleObjectIdResolver();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        _setUpObjectIdsAndResolvers(context, idGen, idResolver);

        ObjectIdResolver resolver = context._objectIdResolvers.get(0);
        assertNull(resolver);

        Object id = new Object();
        ReadableObjectId objectId = context.findObjectId(id, idGen);
        assertNotNull(objectId);
        assertEquals(id, objectId.getId());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        ObjectIdGenerator idGen = new KeyValueObjectIdGenerator();
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        _setUpObjectIdsAndResolvers(context, idGen, idResolver);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        ObjectMapper mapper = new ObjectMapper();
        Annotated ann = mapper.findAnnotation(JsonDeserializer.class, String.class);
        JsonDeserializer<String> deserializer = mapper.deserializationConfig().getDefaultDeserializerFactory()
                .createValueDeserializer(ann, String.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig()
                .getDefaultDeserializerFactory());
        JsonDeserializer<?> deser = context.deserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);
    }

    @Test
    public void testKeyDeserializerInstance() {
        ObjectMapper mapper = new ObjectMapper();
        Annotated ann = mapper.findAnnotation(KeyDeserializer.class, String.class);
        KeyDeserializer deserializer = mapper.deserializationConfig().getDefaultDeserializerFactory()
                .createKeyDeserializer(ann, String.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig()
                .getDefaultDeserializerFactory());
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);
    }

    @Test
    public void testCreateInstance() {
        ObjectMapper mapper = new ObjectMapper();
        _setUpObjectIdsAndResolvers(mapper.getDeserializationConfig().getDefaultDeserializerFactory(),
                new KeyValueObjectIdGenerator(), new SimpleObjectIdResolver());
        DefaultDeserializationContext context = mapper.getDeserializationConfig().getDefaultDeserializerFactory()
                .createContextInstance(mapper.getDeserializationConfig(), null, null, null);
        assertNotNull(context._objectIds);
        assertNotNull(context._objectIdResolvers);
    }

    @Test
    public void testWith() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationConfig().getDefaultDeserializerFactory();
        DefaultDeserializationContext newContext = context.with(new SimpleDeserializerProvider());
        assertNotNull(newContext);
        assertNotSame(context, newContext);
    }

    private void _setUpObjectIdsAndResolvers(DefaultDeserializationContext context, ObjectIdGenerator idGen,
            ObjectIdResolver idResolver) {
        if (context._objectIds == null) {
            context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        }
        if (context._objectIdResolvers == null) {
            context._objectIdResolvers = new ArrayList<ObjectIdResolver>(8);
        }

        ObjectIdResolver resolver = idResolver;
        ObjectIdGenerator idGenerator = idGen;

        Object id = new Object();
        ObjectIdGenerator.IdKey key = idGenerator.key(id);
        ReadableObjectId objectId = new ReadableObjectId(key);
        objectId.setResolver(resolver);
        context._objectIds.put(key, objectId);
    }

    private static class SimpleDeserializerProvider implements DeserializerFactory {
        @Override
        public JsonDeserializer<?> createValueDeserializer(Annotated paramAnnotated, Class<?> paramClass)
                throws JsonMappingException {
            return null;
        }

        @Override
        public KeyDeserializer createKeyDeserializer(Annotated paramAnnotated, Class<?> paramClass)
                throws JsonMappingException {
            return null;
        }
    }

    private static class KeyValueObjectIdGenerator implements ObjectIdGenerator {

        @Override
        public ObjectIdGenerator.IdKey key(Object paramObject) {
            return new IdKey() {
                @Override
                public String toString() {
                    return "id:" + paramObject.hashCode();
                }
            };
        }
    }
}