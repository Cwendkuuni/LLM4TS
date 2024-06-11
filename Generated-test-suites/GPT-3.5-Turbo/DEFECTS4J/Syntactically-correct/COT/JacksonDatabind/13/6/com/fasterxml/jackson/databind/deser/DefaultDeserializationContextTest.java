
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {
    
    @Test
    public void testFindObjectId() {
        // Test basic functionality
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<ObjectIdResolverTest.Id>(ObjectIdResolverTest.Id.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId objectId = context.findObjectId(id, gen, resolver);
        assertNotNull(objectId);
        
        // Test same object id returns the same ReadableObjectId
        ReadableObjectId sameObjectId = context.findObjectId(id, gen, resolver);
        assertSame(objectId, sameObjectId);
    }
    
    @Test
    public void testFindObjectIdWithNull() {
        // Test null object id
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<ObjectIdResolverTest.Id>(ObjectIdResolverTest.Id.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId objectId = context.findObjectId(null, gen, resolver);
        assertNotNull(objectId);
        
        // Test same object id returns the same ReadableObjectId
        ReadableObjectId sameObjectId = context.findObjectId(null, gen, resolver);
        assertSame(objectId, sameObjectId);
    }
    
    @Test
    public void testFindObjectIdWithNullResolver() {
        // Test null resolver
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<ObjectIdResolverTest.Id>(ObjectIdResolverTest.Id.class);

        ReadableObjectId objectId = context.findObjectId(id, gen, null);
        assertNotNull(objectId);
        
        // Test same object id returns the same ReadableObjectId
        ReadableObjectId sameObjectId = context.findObjectId(id, gen, null);
        assertSame(objectId, sameObjectId);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        // Test no unresolved object ids
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.checkUnresolvedObjectId();
        
        // Test unresolved object ids
        context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<ObjectIdResolverTest.Id>(ObjectIdResolverTest.Id.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id = new Object();
        ReadableObjectId objectId = context.findObjectId(id, gen, resolver);
        
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception to be thrown");
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: ", e.getMessage());
            assertEquals(id, e.getUnresolvedIds().get(0));
        }
    }
    
    @Test
    public void testDeserializerInstance() {
        // Test null deserializer definition
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.deserializerInstance(null, null));
        
        // Test deserializer definition of type JsonDeserializer
        context = new DefaultDeserializationContext.Impl(null);
        Object deserDef = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        assertNotNull(context.deserializerInstance(null, deserDef));
        
        // Test deserializer definition of type Class
        context = new DefaultDeserializationContext.Impl(null);
        Class<?> deserClass = TestDeserializer.class;
        assertNotNull(context.deserializerInstance(null, deserClass));
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        // Test null key deserializer definition
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.keyDeserializerInstance(null, null));
        
        // Test key deserializer definition of type KeyDeserializer
        context = new DefaultDeserializationContext.Impl(null);
        Object deserDef = new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        assertNotNull(context.keyDeserializerInstance(null, deserDef));
        
        // Test key deserializer definition of type Class
        context = new DefaultDeserializationContext.Impl(null);
        Class<?> deserClass = TestKeyDeserializer.class;
        assertNotNull(context.keyDeserializerInstance(null, deserClass));
    }
    
    private static class TestDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }
    
    private static class TestKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }
}
