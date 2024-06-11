package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultDeserializationContextTest {
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId entry = new ReadableObjectId(null);
        entry.setResolver(resolver);
        context._objectIds.put(new ObjectIdGenerator.IdKey("key"), entry);
        
        assertEquals(entry, context.findObjectId("key", null, resolver));
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey,ReadableObjectId>();
        context._objectIds.put(new ObjectIdGenerator.IdKey("key"), new ReadableObjectId(null));
        
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: ", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        assertNull(context.deserializerInstance(null, null));
        assertNull(context.deserializerInstance(null, JsonDeserializer.None.class));
        assertNull(context.deserializerInstance(null, getClass()));
        
        JsonDeserializer<Object> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        assertEquals(deser, context.deserializerInstance(null, deser));
        
        HandlerInstantiator hi = new HandlerInstantiator() {
            @Override
            public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> deserClass) {
                return new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                        return null;
                    }
                };
            }
      };
        context._config = new DeserializationConfig(null, null, hi);
        assertEquals(deser, context.deserializerInstance(null, getClass()));
    }
    
    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        assertNull(context.keyDeserializerInstance(null, null));
        assertNull(context.keyDeserializerInstance(null, KeyDeserializer.None.class));
        assertNull(context.keyDeserializerInstance(null, getClass()));
        
        KeyDeserializer deser = new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        assertEquals(deser, context.keyDeserializerInstance(null, deser));
        
        HandlerInstantiator hi = new HandlerInstantiator() {
            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> deserClass) {
                return new KeyDeserializer() {
                    @Override
                    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                        return null;
                    }
                };
            }
      };
        context._config = new DeserializationConfig(null, null, hi);
        assertEquals(deser, context.keyDeserializerInstance(null, getClass()));
    }
}