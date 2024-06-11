package com.fasterxml.jackson.databind.deser;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.Map.Entry;

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
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        ReadableObjectId result = context.findObjectId(id, gen, resolverType);
        Assert.assertNull(result);
    }

    @Test
    public void testFindObjectIdWithExistingEntry() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        
        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        IdKey key = new IdKey(gen, id);
        ReadableObjectId entry = new ReadableObjectId(key);
        context._objectIds.put(key, entry);

        ReadableObjectId result = context.findObjectId(id, gen, resolverType);
        Assert.assertEquals(entry, result);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);

        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            Assert.fail("Should not throw an exception");
        }
    }

    @Test
    public void testCheckUnresolvedObjectIdWithException() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);

        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        ReadableObjectId entry = new ReadableObjectId(new IdKey(null, null));
        entry.setResolver(new SimpleObjectIdResolver());
        entry.appendReferring(new Referring() {
            @Override
            public void handleResolvedForwardReference(Object id, Object value) {                
            }
            @Override
            public IdKey getKey() { return null; }
            @Override
            public Object getBean() { return null; }
            @Override
            public void setBean(Object bean) { }
            @Override
            public void setNext(Referring next) { }
            @Override
            public Referring next() { return null; }            
        });
        context._objectIds.put(new IdKey(null, null), entry);

        try {
            context.checkUnresolvedObjectId();
            Assert.fail("Should throw an exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = null;
        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        Assert.assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithIllegalArgumentException() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = new Object();
        context.deserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithClassCastException() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = new ArrayList<String>();
        context.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testDeserializerInstanceWithResolvableDeserializer() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = new ResolvableDeserializer() {
            @Override
            public void resolve(DeserializationContext ctxt) throws JsonMappingException {
            }
        };
        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        Assert.assertNotNull(result);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = null;
        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        Assert.assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithIllegalArgumentException() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = new Object();
        context.keyDeserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithClassCastException() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = new ArrayList<String>();
        context.keyDeserializerInstance(ann, deserDef);
    }

    @Test
    public void testKeyDeserializerInstanceWithResolvableDeserializer() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
        Annotated ann = null;
        Object deserDef = new ResolvableDeserializer() {
            @Override
            public void resolve(DeserializationContext ctxt) throws JsonMappingException {
            }
        };
        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        Assert.assertNotNull(result);
    }
}