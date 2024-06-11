package com.fasterxml.jackson.databind.deser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {
    
    private DefaultDeserializationContext deserializationContext;
    
    @Before
    public void setUp() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        deserializationContext = new DefaultDeserializationContext.Impl(df, cache);
    }
    
    @Test
    public void testFindObjectId() {
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey();
        ReadableObjectId roid = deserializationContext.findObjectId(null, null, null);
        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        objectIds.put(key, roid);
        assertEquals(roid, objectIds.get(key));
    }
    
    @Test
    public void testCopy() {
        DefaultDeserializationContext copy = deserializationContext.copy();
        assertTrue(copy instanceof DefaultDeserializationContext.Impl);
    }
    
    @Test
    public void testDeserializerInstance() {
        Annotated ann = new Annotated();
        Object deserDef = new Object();
        JsonDeserializer<Object> deser = deserializationContext.deserializerInstance(ann, deserDef);
        assertNull(deser);
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        Annotated ann = new Annotated();
        Object deserDef = new Object();
        KeyDeserializer keyDeser = deserializationContext.keyDeserializerInstance(ann, deserDef);
        assertNull(keyDeser);
    }
    
    @Test
    public void testWith() {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext withContext = deserializationContext.with(factory);
        assertTrue(withContext instanceof DefaultDeserializationContext.Impl);
    }
    
    @Test
    public void testCreateInstance() {
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = new JsonParser();
        InjectableValues values = new InjectableValues();
        DefaultDeserializationContext instance = deserializationContext.createInstance(config, jp, values);
        assertTrue(instance instanceof DefaultDeserializationContext.Impl);
    }
    
    @Test
    public void testImpl() {
        DeserializerFactory df = new DeserializerFactory();
        DefaultDeserializationContext.Impl impl = new DefaultDeserializationContext.Impl(df);
        assertTrue(impl instanceof DefaultDeserializationContext.Impl);
    }
}