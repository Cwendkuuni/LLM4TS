import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
    
    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, IntegerType.instance);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertTrue(collectionType.isEmpty());
        assertEquals(IntegerType.instance, collectionType.getContentType());
    }
    
    @Test
    public void testWithContentType() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, IntegerType.instance);
        CollectionType modifiedType = collectionType.withContentType(StringType.instance);
        assertFalse(modifiedType.isEmpty());
        assertEquals(StringType.instance, modifiedType.getContentType());
    }
    
    // TODO: Add more test cases for other methods
    
}