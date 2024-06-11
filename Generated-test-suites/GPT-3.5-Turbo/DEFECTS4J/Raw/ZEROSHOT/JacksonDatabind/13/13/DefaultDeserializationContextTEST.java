package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext defaultDeserializationContext;
    private DeserializerFactory deserializerFactory;
    private DeserializerCache deserializerCache;
    private DeserializationConfig deserializationConfig;
    private JsonParser jsonParser;
    private InjectableValues injectableValues;
    private Annotated annotated;
    private Object deserDef;

    @org.junit.Before
    public void setUp() {
        deserializerFactory = new DeserializerFactory();
        deserializerCache = new DeserializerCache();
        defaultDeserializationContext = new DefaultDeserializationContext(deserializerFactory, deserializerCache);
        defaultDeserializationContext = new DefaultDeserializationContext(defaultDeserializationContext, deserializationConfig, jsonParser, injectableValues);
        defaultDeserializationContext = new DefaultDeserializationContext(defaultDeserializationContext, deserializationConfig, jsonParser, injectableValues);
        defaultDeserializationContext = new DefaultDeserializationContext(defaultDeserializationContext);
        deserializationConfig = new DeserializationConfig();
    }

    @org.junit.Test
    public void testCopy() {
        org.junit.Assert.assertNotNull(defaultDeserializationContext);
    }

    @org.junit.Test
    public void testFindObjectId() {
        Object id = null;
        ObjectIdGenerator gen = null;
        defaultDeserializationContext.findObjectId(id, gen);
    }

    @org.junit.Test
    public void testFindObjectId2() {
        Object id = null;
        ObjectIdGenerator gen = null;
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        defaultDeserializationContext.findObjectId(id, gen, resolverType);
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectId() {
        defaultDeserializationContext.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDeserializerInstance() {
        JsonDeserializer<Object> res = null;
        try {
            res = defaultDeserializationContext.deserializerInstance(annotated, deserDef);
        } catch (Exception e) {
            org.junit.Assert.fail("testDeserializerInstance() failed: " + e.getMessage());
        }
        org.junit.Assert.assertNotNull(res);
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() {
        KeyDeserializer res = null;
        try {
            res = defaultDeserializationContext.keyDeserializerInstance(annotated, deserDef);
        } catch (Exception e) {
            org.junit.Assert.fail("testKeyDeserializerInstance() failed: " + e.getMessage());
        }
        org.junit.Assert.assertNotNull(res);
    }

    @org.junit.Test
    public void testWith() {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext res = null;
        res = defaultDeserializationContext.with(factory);
        org.junit.Assert.assertNotNull(res);
    }

    @org.junit.Test
    public void testCreateInstance() {
        DefaultDeserializationContext res = null;
        res = defaultDeserializationContext.createInstance(deserializationConfig, jsonParser, injectableValues);
        org.junit.Assert.assertNotNull(res);
    }

    @org.junit.Test
    public void testImpl() {
        defaultDeserializationContext.copy();
        DefaultDeserializationContext.Impl res = null;
        res = new DefaultDeserializationContext.Impl(deserializerFactory);
        res = new DefaultDeserializationContext.Impl(defaultDeserializationContext);
        res = new DefaultDeserializationContext.Impl(defaultDeserializationContext, deserializationConfig, jsonParser, injectableValues);
        res = new DefaultDeserializationContext.Impl(defaultDeserializationContext);
        res = new DefaultDeserializationContext.Impl(defaultDeserializationContext, deserializerFactory);
        org.junit.Assert.assertNotNull(res);
    }
}