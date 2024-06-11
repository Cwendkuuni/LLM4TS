import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test adding a valid mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(Collection.class)));
        
        // Test adding a mapping from a class to itself
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test adding a mapping with non-subtype relationship
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        // Test finding a type mapping
        JavaType result = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Collection.class));
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getRawClass());
        
        // Test finding a type mapping that does not exist
        JavaType result2 = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(String.class));
        assertNull(result2);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test resolving an abstract type
        JavaType result = resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Collection.class));
        assertNull(result);
    }

}