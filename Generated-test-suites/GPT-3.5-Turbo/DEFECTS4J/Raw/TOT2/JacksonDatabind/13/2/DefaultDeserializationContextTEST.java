import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<ObjectIdGenerator.IdKey>() {
            @Override
            public IdKey key(Object key) {
                return new IdKey(getClass(), null, key);
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId result = context.findObjectId(id, gen, resolver);
        
        assertNotNull(result);
        assertEquals(id, result.getKey().key);
        assertEquals(gen, result.getResolver().getGenerator());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("Exception should not have been thrown");
        }
        
        context.findObjectId(new Object(), new ObjectIdGenerator<ObjectIdGenerator.IdKey>() {
            @Override
            public IdKey key(Object key) {
                return new IdKey(getClass(), null, key);
            }
        }, new SimpleObjectIdResolver());
        
        try {
            context.checkUnresolvedObjectId();
            fail("Exception should have been thrown");
        } catch (UnresolvedForwardReference e) {
            // Expected exception
        }
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        try {
            JsonDeserializer<Object> deser = context.deserializerInstance(null, null);
            assertNull(deser);
        } catch (JsonMappingException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        try {
            KeyDeserializer deser = context.keyDeserializerInstance(null, null);
            assertNull(deser);
        } catch (JsonMappingException e) {
            fail("Exception should not have been thrown");
        }
    }
}