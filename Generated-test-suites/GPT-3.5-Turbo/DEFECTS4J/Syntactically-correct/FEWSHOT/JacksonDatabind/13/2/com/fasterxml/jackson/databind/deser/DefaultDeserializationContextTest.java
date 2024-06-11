package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
    
    String[] EMPTY_STRING_ARRAY = {};

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl dc = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        
        ObjectIdGenerator<?> idGen = new ObjectIdGenerator<ObjectIdGenerator.IdKey>() {
            @Override
            public IdKey key(Object id) {
                return new IdKey(null, id);
            }
        };
        
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId roid = dc.findObjectId("1", idGen, resolver);
        assertNull(roid.referringProperties());
        
        roid.setResolver(resolver);
        Map<IdKey, ReadableObjectId> objectIds = new LinkedHashMap<IdKey, ReadableObjectId>();
        objectIds.put(roid.getKey(), roid);
        
        dc._objectIds = objectIds;
        
        ReadableObjectId found = dc.findObjectId("1", idGen, resolver);
        assertEquals(roid, found);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        DefaultDeserializationContext.Impl dc = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId roid = new ReadableObjectId(new ObjectIdGenerator.IdKey(null, "1"));
        roid.setResolver(resolver);
        
        roid.appendReferring(new Referring(dc, null, null, null));
        Entry<DefaultDeserializationContext.Impl.ObjectIdGenerator.IdKey, ReadableObjectId> entry = 
                new Entry<DefaultDeserializationContext.Impl.ObjectIdGenerator.IdKey, ReadableObjectId>() {
            @Override
            public DefaultDeserializationContext.Impl.ObjectIdGenerator.IdKey getKey() {
                return new DefaultDeserializationContext.Impl.ObjectIdGenerator.IdKey(null, "1");
            }

            @Override
            public ReadableObjectId getValue() {
                return roid;
            }

            @Override
            public ReadableObjectId setValue(ReadableObjectId value) {
                return roid;
            }
        };
        
        Map<DefaultDeserializationContext.Impl.ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = 
                new LinkedHashMap<DefaultDeserializationContext.Impl.ObjectIdGenerator.IdKey, ReadableObjectId>();
        objectIds.put(entry.getKey(), entry.getValue());
        
        dc._objectIds = objectIds;
        
        try {
            dc.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: ", e.getMessage());
            List<String> unresolvedIds = e.getUnresolvedIds();
            assertEquals(1, unresolvedIds.size());
            assertEquals("1", unresolvedIds.get(0));
        }
    }
    
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl dc = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = null;
        
        assertNull(dc.deserializerInstance(ann, deserDef));
        
        deserDef = ArrayList.class;
        JsonDeserializer<?> deser = dc.deserializerInstance(ann, deserDef);
        assertEquals(ArrayList.class, deser.getClass());
    }
    
    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl dc = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = null;
        
        assertNull(dc.keyDeserializerInstance(ann, deserDef));
        
        deserDef = ArrayList.class;
        KeyDeserializer deser = dc.keyDeserializerInstance(ann, deserDef);
        assertEquals(ArrayList.class, deser.getClass());
    }
    
    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl dc = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        DeserializerFactory factory = new DeserializerFactory();
        
        DefaultDeserializationContext.Impl newContext = dc.with(factory);
        assertEquals(DefaultDeserializationContext.Impl.class, newContext.getClass());
        assertEquals(factory, newContext._factory);
    }
    
    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl dc = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        DeserializationConfig config = new DeserializationConfig(null, null);
        JsonParser jp = null;
        InjectableValues values = null;
        
        DefaultDeserializationContext.Impl newContext = dc.createInstance(config, jp, values);
        assertEquals(DefaultDeserializationContext.Impl.class, newContext.getClass());
        assertEquals(config, newContext._config);
        assertEquals(jp, newContext._parser);
        assertEquals(values, newContext._injectableValues);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCopy() {
        DefaultDeserializationContext.Impl dc = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        dc.copy();
    }

}