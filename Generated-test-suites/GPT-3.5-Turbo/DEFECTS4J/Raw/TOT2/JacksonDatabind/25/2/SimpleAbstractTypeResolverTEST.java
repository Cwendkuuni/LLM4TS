import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test adding valid mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        assertEquals(resolver._mappings.size(), 1);
        
        // Test adding mapping from class to itself
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test adding mapping with subtype that is not a subtype of superType
        try {
            resolver.addMapping(List.class, HashSet.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test adding mapping for non-abstract superType
        try {
            resolver.addMapping(LinkedList.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test finding type mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType result = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Collection.class));
        assertNotNull(result);
        assertEquals(result.getRawClass(), LinkedList.class);
        
        // Test finding non-existing type mapping
        JavaType result2 = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(ArrayList.class));
        assertNull(result2);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test resolving abstract type
        JavaType result = resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Collection.class));
        assertNull(result);
    }
}