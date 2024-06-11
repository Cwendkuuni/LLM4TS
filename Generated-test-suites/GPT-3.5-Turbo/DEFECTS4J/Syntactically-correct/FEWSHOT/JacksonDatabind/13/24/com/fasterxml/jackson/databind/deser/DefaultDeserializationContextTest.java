import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDeserializationContextTest {
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl ctx = new DefaultDeserializationContext.Impl(null);
        
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey("id", Object.class, null, null);
        ReadableObjectId objectId = new ReadableObjectId(key);
        
        ctx._objectIds = new LinkedHashMap<>();
        ctx._objectIds.put(key, objectId);
        
        assertEquals(objectId, ctx.findObjectId("id", null, null));
    }
    
    @Test
    public void testFindObjectIdWithNullResolver() {
        DefaultDeserializationContext.Impl ctx = new DefaultDeserializationContext.Impl(null);
        
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey("id", Object.class, null, null);
        ReadableObjectId objectId = new ReadableObjectId(key);
        
        ctx._objectIds = new LinkedHashMap<>();
        ctx._objectIds.put(key, objectId);
        
        assertNull(ctx.findObjectId("id", null));
    }
    
    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        DefaultDeserializationContext.Impl ctx = new DefaultDeserializationContext.Impl(null);
        ctx.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);
        
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey("id", Object.class, null, null);
        ReadableObjectId objectId = new ReadableObjectId(key);
        
        objectId.getReferringProperties();
        
        ctx._objectIds = new LinkedHashMap<>();
        ctx._objectIds.put(key, objectId);
        
        try {
            ctx.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: ", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl ctx = new DefaultDeserializationContext.Impl(null);
        
        JsonDeserializer<Object> deser = ctx.deserializerInstance(null, null);
        assertNull(deser);
    }
    
    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl ctx = new DefaultDeserializationContext.Impl(null);
        
        KeyDeserializer deser = ctx.keyDeserializerInstance(null, null);
        assertNull(deser);
    }
    
    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl ctx = new DefaultDeserializationContext.Impl(null);
        
        DefaultDeserializationContext.Impl newContext = ctx.with(null);
        
        assertEquals(ctx, newContext);
    }
    
    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl ctx = new DefaultDeserializationContext.Impl(null);
        
        DefaultDeserializationContext.Impl newContext = ctx.createInstance(null, null, null);
        
        assertEquals(ctx, newContext);
    }
}