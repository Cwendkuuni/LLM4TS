package com.fasterxml.jackson.databind.deser;

import org.junit.Assert;
import org.junit.Test;

public class DefaultDeserializationContextTest {
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null, null);
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator<>(Object.class, "id");
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId objectId = context.findObjectId(id, generator, resolver);
        
        Assert.assertNotNull(objectId);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null, null);
        
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null, null);
        JsonDeserializer<Object> deserializer = null;
        Annotated annotated = null;
        Object deserDef = null;
        
        deserializer = context.deserializerInstance(annotated, deserDef);
        
        Assert.assertNull(deserializer);
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null, null);
        KeyDeserializer keyDeserializer = null;
        Annotated annotated = null;
        Object deserDef = null;
        
        keyDeserializer = context.keyDeserializerInstance(annotated, deserDef);
        
        Assert.assertNull(keyDeserializer);
    }
    
    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null, null);
        DeserializerFactory factory = null;
        
        DefaultDeserializationContext newContext = context.with(factory);
        
        Assert.assertNotNull(newContext);
    }
    
    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null, null);
        DeserializationConfig config = null;
        JsonParser jp = null;
        InjectableValues values = null;
        
        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);
        
        Assert.assertNotNull(newContext);
    }
    
    @Test
    public void testCopy() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null, null);
        
        DefaultDeserializationContext copiedContext = context.copy();
        
        Assert.assertNotNull(copiedContext);
    }
}