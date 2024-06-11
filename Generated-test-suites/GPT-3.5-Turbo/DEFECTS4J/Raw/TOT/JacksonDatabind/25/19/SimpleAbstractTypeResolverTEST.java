import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Typical use case
        resolver.addMapping(Collection.class, ArrayList.class);
        
        // Edge case: class and subclass are the same
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
        
        // Edge case: subclass is not a subtype of superclass
        try {
            resolver.addMapping(List.class, LinkedList.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.List to java.util.LinkedList, as latter is not a subtype of former", e.getMessage());
        }
        
        // Edge case: superclass is not abstract
        try {
            resolver.addMapping(ArrayList.class, List.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
        
        // Error scenario: Mapping should be added successfully
        assertTrue(resolver._mappings.containsKey(new ClassKey(Collection.class)));
        assertEquals(ArrayList.class, resolver._mappings.get(new ClassKey(Collection.class)));
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Typical use case: Mapping exists for given raw class
        resolver.addMapping(Collection.class, ArrayList.class);
        JavaType type = resolver.findTypeMapping(null, new JavaType(Collection.class));
        assertEquals(ArrayList.class, type.getRawClass());
        
        // Typical use case: Mapping does not exist for given raw class
        type = resolver.findTypeMapping(null, new JavaType(Map.class));
        assertNull(type);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Error scenario: Always returns null
        JavaType type = resolver.resolveAbstractType(null, new JavaType(Collection.class));
        assertNull(type);
    }
}