import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    // Test Case 1
    @Test
    public void testConstruct() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        
        assertNotNull(collectionType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType().getRawClass());
    }
    
    // Test Case 2
    @Test
    public void testWithContentType() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = collectionType.withContentType(Integer.class);
        
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertEquals(Integer.class, modifiedType.getContentType().getRawClass());
    }
    
    // Test Case 3
    @Test
    public void testWithContentTypeHandler() {
        // Error scenario
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = collectionType.withContentTypeHandler("handler");
        
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertEquals(String.class, modifiedType.getContentType().getRawClass());
        assertNull(modifiedType.getContentType().getTypeHandler());
    }
    
    // Test Case 4
    @Test
    public void testWithStaticTyping() {
        // Edge case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = collectionType.withStaticTyping();
        
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertTrue(modifiedType.isStaticTyping());
    }
    
    // Test Case 5
    @Test
    public void testRefine() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        CollectionType refinedType = collectionType.refine(LinkedList.class, new TypeBindings(), null, null);
        
        assertNotNull(refinedType);
        assertEquals(LinkedList.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType().getRawClass());
    }
}