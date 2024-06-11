package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
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
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private JsonParser parser;
    private ObjectMapper mapper;

    @Before
    public void setup() throws JsonProcessingException {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("myKey");
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context = new DefaultDeserializationContext(new DefaultDeserializerFactory(), null) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolverType) {
                return super.findObjectId(id, gen, resolverType);
            }

            @Override
            public JsonDeserializer<Object> deserializerInstance(Annotated ann, Object deserDef)
                    throws JsonMappingException {
                return super.deserializerInstance(ann, deserDef);
            }

            @Override
            public KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef)
                    throws JsonMappingException {
                return super.keyDeserializerInstance(ann, deserDef);
            }

            @Override
            public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp,
                    InjectableValues values) {
                return new Impl(this);
            }

            @Override
            public DefaultDeserializationContext with(DeserializerFactory factory) {
                return new Impl(this, factory);
            }

            @Override
            public void checkUnresolvedObjectId() throws UnresolvedForwardReference {
                // Do nothing
            }
        };

        context.addHandler(generator);
        context.addHandler(resolver);
        mapper = new ObjectMapper();
        parser = mapper.getFactory().createParser("{}");
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("myKey");
            }
        };
        ReadableObjectId objectId = context.findObjectId(id, generator, null);
        assertNotNull(objectId);
    }

    @Test
    public void testFindObjectIdWithNullId() {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("myKey");
            }
        };
        ReadableObjectId objectId = context.findObjectId(null, generator, null);
        assertNull(objectId);
    }

    @Test
    public void testFindObjectIdWithNullGenerator() {
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId objectId = context.findObjectId(new Object(), null, resolver);
        assertNull(objectId);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws JsonMappingException {
        Map<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new HashMap<>();
        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> linkedObjectIds = new LinkedHashMap<>();
        context = new DefaultDeserializationContext(new DefaultDeserializerFactory(), null) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolverType) {
                ReadableObjectId objectId = super.findObjectId(id, gen, resolverType);
                if (objectId != null) {
                    objectIds.put(objectId.getKey(), objectId);
                    linkedObjectIds.put(objectId.getKey(), objectId);
                }
                return objectId;
            }

            @Override
            public void checkUnresolvedObjectId() throws UnresolvedForwardReference {
                super.checkUnresolvedObjectId();
            }
        };
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey("myKey");
            }
        };
        context.findObjectId(id, generator, null);
        assertNull(context.findUnresolvedObjectIds());

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
                return this;
            }

            @Override
            public ObjectId resolveId(DeserializationContext ctxt, ObjectId id) throws JsonMappingException {
                return null;
            }
        };
        context.addHandler(resolver);
        context.findObjectId(id, generator, resolver);
        List<UnresolvedObjectId> unresolvedObjectIds = context.findUnresolvedObjectIds();
        assertNull(unresolvedObjectIds);

        ReadableObjectId objectId1 = context.findObjectId(id, generator, null);
        assertNull(objectId1);

        ReadableObjectId objectId2 = context.findObjectId(new Object(), generator, resolver);
        assertNull(objectId2);

        context.checkUnresolvedObjectId();
        UnresolvedObjectId unresolvedObjectId = context.findUnresolvedObjectIds().get(0);
        assertNotNull(unresolvedObjectId.getId());
        assertNotNull(unresolvedObjectId.getPropertyName());
        assertNotNull(unresolvedObjectId.getLocation());
        assertNotNull(unresolvedObjectId.getBeanType());
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "deserialized";
            }
        };
        Annotated annotated = mapper.getAnnotationIntrospector().findPropertyAnnotation(parser.getCurrentName(), String.class);
        JsonDeserializer<String> deserializerInstance = context.deserializerInstance(annotated, deserializer);
        assertNotNull(deserializerInstance);
        String result = deserializerInstance.deserialize(parser, context);
        assertEquals("deserialized", result);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        KeyDeserializer keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "deserialized";
            }
        };
        Annotated annotated = mapper.getAnnotationIntrospector().findPropertyAnnotation(parser.getCurrentName(), String.class);
        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(annotated, keyDeserializer);
        assertNotNull(keyDeserializerInstance);
        Object result = keyDeserializerInstance.deserializeKey(parser, context);
        assertEquals("deserialized", result);
    }

    @Test
    public void testCreateInstance() throws JsonMappingException {
        DefaultDeserializationContext newContext = context.createInstance(null, parser, null);
        assertNotNull(newContext);
    }

    @Test(expected = IllegalStateException.class)
    public void testCopyWithoutOverride() {
        context.copy();
    }

    @Test
    public void testCopyWithOverride() throws JsonMappingException {
        DefaultDeserializationContext newContext = context.copy();
        assertNotNull(newContext);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext newContext = context.with(new DefaultDeserializerFactory());
        assertNotNull(newContext);
        assertNotSame(context, newContext);
    }

}