package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DefaultDeserializationContextTest {
    
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = createContext();
        
        assertNull(context.findObjectId(null, null, null));
        
        Object id = new Object();
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolver = null;
        assertNull(context.findObjectId(id, gen, resolver));
        
        id = new Object();
        gen = new ObjectIdGenerator<Object>() {
            @Override
            public IdKey key(Object key) {
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
            public Object generateId(Object forPojo) {
                return null;
            }
            @Override
            public void setSerializer(ObjectIdSerializer se) {
                
            }
            @Override
            public void setResolver(ObjectIdResolver resolver) {
                
            }
        };
        resolver = new SimpleObjectIdResolver();
        assertTrue(context.findObjectId(id, gen, resolver) instanceof ReadableObjectId);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testFindObjectId_deprecated() {
        DefaultDeserializationContext.Impl context = createContext();
        
        assertNull(context.findObjectId(null, null));
        
        Object id = new Object();
        ObjectIdGenerator<?> gen = null;
        assertNull(context.findObjectId(id, gen));
        
        id = new Object();
        gen = new ObjectIdGenerator<Object>() {
            @Override
            public IdKey key(Object key) {
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
            public Object generateId(Object forPojo) {
                return null;
            }
            @Override
            public void setSerializer(ObjectIdSerializer se) {
                
            }
            @Override
            public void setResolver(ObjectIdResolver resolver) {
                
            }
        };
        assertTrue(context.findObjectId(id, gen) instanceof ReadableObjectId);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext.Impl context = createContext();
        
        context.checkUnresolvedObjectId();
    }
    
    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext.Impl context = createContext();
        assertNull(context.deserializerInstance(null, null));
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext.Impl context = createContext();
        assertNull(context.keyDeserializerInstance(null, null));
    }
    
    private DefaultDeserializationContext.Impl createContext() {
        return new DefaultDeserializationContext.Impl(new DeserializerFactory());
    }
}