import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        
        // Test with valid parameters
        Object id = "12345";
        ObjectIdGenerator<?> gen = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        
        ReadableObjectId result = context.findObjectId(id, gen, resolverType);
        
        assertNotNull(result);
        assertEquals(id, result.getKey().key);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        
        // Test when there are no unresolved object ids
        context.checkUnresolvedObjectId();
        
        // Add unresolved object id
        ReadableObjectId unresolvedId = new ReadableObjectId(new ObjectIdGenerator.IdKey("key", null));
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey("key", null), unresolvedId);
        
        // Test when there are unresolved object ids
        try {
            context.checkUnresolvedObjectId();
            fail("UnresolvedForwardReference exception should have been thrown");
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

}