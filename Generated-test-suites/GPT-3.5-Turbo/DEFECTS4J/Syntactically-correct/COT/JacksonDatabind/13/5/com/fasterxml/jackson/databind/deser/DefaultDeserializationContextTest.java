
package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest {
    
    private DefaultDeserializationContext ctx;
    
    @Before
    public void setUp() {
        ctx = new DefaultDeserializationContext.Impl(null);
    }
    
    @Test
    public void testFindObjectId() {
        // Test id and gen are null
        assertNull(ctx.findObjectId(null, null));
        
        // Test objectIds is null
        assertNotNull(ctx.findObjectId("id", new ObjectIdGenerator<String>() {
            @Override
            public void bindItem(ObjectIdResolver resolver, String key) { }

            @Override
            public ObjectIdGenerator<String> forScope(Class<?> scope) {
                return null;
            }

            @Override
            public ObjectIdGenerator<String> newForSerialization(Object context) {
                return null;
            }

            @Override
            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }

            @Override
            public IdKey key(Object key) {
                return null;
            }
        }));
    }
    
    @Test
    public void testFindObjectIdDeprecated() {
        assertNull(ctx.findObjectId(null, null, null));
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        // objectIds is null
        ctx.checkUnresolvedObjectId();
        
        // objectIds is not null, should throw exception
        ctx._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        try {
            ctx.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference, but no exception was thrown.");
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: ", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        assertNull(ctx.deserializerInstance(null, null));
        
        // deserDef is an instance of JsonDeserializer
        assertNotNull(ctx.deserializerInstance(null, new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        }));
        
        // deserDef is an instance of Class
        assertNotNull(ctx.deserializerInstance(null, Object.class));
        
        // deserDef is an instance of Class that is not a JsonDeserializer
        try {
            ctx.deserializerInstance(null, Object.class);
            fail("Expected IllegalStateException, but no exception was thrown.");
        } catch (IllegalStateException e) {
            assertEquals("AnnotationIntrospector returned Class class java.lang.Object; expected Class<JsonDeserializer>", e.getMessage());
        }
    }
    
    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        assertNull(ctx.keyDeserializerInstance(null, null));
        
        // deserDef is an instance of KeyDeserializer
        assertNotNull(ctx.keyDeserializerInstance(null, new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        }));
        
        // deserDef is an instance of Class
        assertNotNull(ctx.keyDeserializerInstance(null, Object.class));
        
        // deserDef is an instance of Class that is not a KeyDeserializer
        try {
            ctx.keyDeserializerInstance(null, Object.class);
            fail("Expected IllegalStateException, but no exception was thrown.");
        } catch (IllegalStateException e) {
            assertEquals("AnnotationIntrospector returned Class class java.lang.Object; expected Class<KeyDeserializer>", e.getMessage());
        }
    }
    
    @Test
    public void testWith() {
        assertNotNull(ctx.with(null));
    }
    
    @Test
    public void testCreateInstance() {
        assertNotNull(ctx.createInstance(null, null, null));
    }
}
