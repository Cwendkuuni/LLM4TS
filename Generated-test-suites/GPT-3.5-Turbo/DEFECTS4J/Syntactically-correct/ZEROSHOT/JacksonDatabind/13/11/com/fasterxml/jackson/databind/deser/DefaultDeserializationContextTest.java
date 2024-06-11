package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext deserializationContext;

    @Before
    public void setup() {
        DeserializerFactory factory = new DeserializerFactory();
        deserializationContext = new DefaultDeserializationContext.Impl(factory);
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator<String> gen = new ObjectIdGenerator<>(String.class, new SimpleObjectIdResolver());
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        String id = "123";
        ReadableObjectId result = deserializationContext.findObjectId(id, gen, resolverType);
        assertNotNull(result);
    }

    @Test
    public void testFindObjectIdWithNullValue() {
        ObjectIdGenerator<String> gen = new ObjectIdGenerator<>(String.class, new SimpleObjectIdResolver());
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        String id = "123";
        deserializationContext.findObjectId(id, gen, resolverType);
        ReadableObjectId result = deserializationContext.findObjectId(id, gen, resolverType);
        assertNotNull(result);
    }

    @Test
    public void testFindObjectIdWithNullResolverType() {
        ObjectIdGenerator<String> gen = new ObjectIdGenerator<>(String.class, new SimpleObjectIdResolver());
        String id = "123";
        ReadableObjectId result = deserializationContext.findObjectId(id, gen);
        assertNotNull(result);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DeserializationConfig config = new DeserializationConfig();
        deserializationContext.configure(config);
        deserializationContext.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        Annotated ann = new Annotated();
        Object deserDef = new Object();
        JsonDeserializer<Object> result = deserializationContext.deserializerInstance(ann, deserDef);
        assertNull(result);
    }

    @Test
    public void testDeserializerInstanceWithJsonDeserializer() {
        Annotated ann = new Annotated();
        Object deserDef = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) {
                return null;
            }
        };
        JsonDeserializer<Object> result = deserializationContext.deserializerInstance(ann, deserDef);
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithJsonDeserializerAndInvalidDef() {
        Annotated ann = new Annotated();
        Object deserDef = new Object();
        deserializationContext.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testDeserializerInstanceWithResolver() {
        Annotated ann = new Annotated();
        Class<?> deserClass = SimpleObjectIdResolver.class;
        KeyDeserializer result = deserializationContext.keyDeserializerInstance(ann, deserClass);
        assertNotNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceWithClass() {
        Annotated ann = new Annotated();
        Object deserDef = SimpleObjectIdResolver.class;
        KeyDeserializer result = deserializationContext.keyDeserializerInstance(ann, deserDef);
        assertNotNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceWithJsonDeserializer() {
        Annotated ann = new Annotated();
        Object deserDef = new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) {
                return null;
            }
        };
        KeyDeserializer result = deserializationContext.keyDeserializerInstance(ann, deserDef);
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithJsonDeserializerAndInvalidDef() {
        Annotated ann = new Annotated();
        Object deserDef = new Object();
        deserializationContext.keyDeserializerInstance(ann, deserDef);
    }
}