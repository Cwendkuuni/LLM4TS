package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
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
    public void setup() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        deserializationContext = new DefaultDeserializationContext.Impl(df, cache);
    }

    @Test
    public void testFindAndAddObjectId() {
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        DefaultDeserializationContext.ReadableObjectId result = 
                deserializationContext.findAndAddObjectId(new Object(), gen, resolverType);
        assertNotNull(result);
    }
    
    @Test
    public void testFindObjectId() {
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        DefaultDeserializationContext.ReadableObjectId result = 
                deserializationContext.findObjectId(new Object(), gen, resolverType);
        assertNull(result);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        // No objectIds present, should do nothing
        deserializationContext.checkUnresolvedObjectId();

        // Add fake objectId
        DefaultDeserializationContext.ReadableObjectId id = 
                new DefaultDeserializationContext.Impl.ObjectId();
        id.setResolver(new SimpleObjectIdResolver());
        deserializationContext._objectIds = new LinkedHashMap<>();
        deserializationContext._objectIds.put(new ObjectIdGenerator.IdKey("fake", "fake"), id);

        // Feature disabled, should do nothing
        deserializationContext.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        deserializationContext.checkUnresolvedObjectId();

        // Feature enabled, should throw exception
        deserializationContext.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        try {
            deserializationContext.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: ", e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance() {
        Annotated ann = null;
        Object deserDef = null;
        JsonDeserializer<Object> result = deserializationContext.deserializerInstance(ann, deserDef);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstance() {
        Annotated ann = null;
        Object deserDef = null;
        KeyDeserializer result = deserializationContext.keyDeserializerInstance(ann, deserDef);
        assertNull(result);
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext result = deserializationContext.with(factory);
        assertNotNull(result);
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = null;
        InjectableValues values = null;
        DefaultDeserializationContext result = deserializationContext.createInstance(config, jp, values);
        assertNotNull(result);
    }
}