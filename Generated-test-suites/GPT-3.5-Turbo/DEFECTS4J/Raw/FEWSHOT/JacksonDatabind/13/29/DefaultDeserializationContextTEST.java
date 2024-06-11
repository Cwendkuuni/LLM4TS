package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.apache.commons.cli.Util;
import org.junit.Test;

import java.util.*;

public class DefaultDeserializationContextTest {
    
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        assertNull(context.findObjectId(null, gen));
        
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        assertNotNull(context.findObjectId(id, gen, resolver));
        
        Exception exception = null;
        try {
            context.findObjectId(id, gen, null);
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.checkUnresolvedObjectId());
        
        context._objectIds = new LinkedHashMap<>();
        assertNull(context.checkUnresolvedObjectId());
        
        Map.Entry<ReadableObjectId.IdKey, ReadableObjectId> entry = new AbstractMap.SimpleEntry<>(new ReadableObjectId.IdKey(), new ReadableObjectId(new ReadableObjectId.IdKey()));
        context._objectIds.put(entry.getKey(), entry.getValue());
        
        UnresolvedForwardReference exception = null;
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.deserializerInstance(null, null));
        
        ObjectMapper objectMapper = new ObjectMapper();
        assertNull(context.deserializerInstance(objectMapper, null));
        
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize() throws JsonParseException {
                return null;
            }
        };
        
        assertNotNull(context.deserializerInstance(objectMapper, deserializer));
        
        Exception exception = null;
        try {
            assertTrue(context.deserializerInstance(objectMapper, String.class) != null);
        } catch (IllegalStateException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    
    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.keyDeserializerInstance(null, null));
        
        ObjectMapper objectMapper = new ObjectMapper();
        assertNull(context.keyDeserializerInstance(objectMapper, null));
        
        KeyDeserializer keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey() throws IOException, JsonProcessingException {
                return null;
            }
        };
        
        assertNotNull(context.keyDeserializerInstance(objectMapper, keyDeserializer));
        
        Exception exception = null;
        try {
             assertNotNull(context.keyDeserializerInstance(objectMapper, String.class));
        } catch (IllegalStateException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
}