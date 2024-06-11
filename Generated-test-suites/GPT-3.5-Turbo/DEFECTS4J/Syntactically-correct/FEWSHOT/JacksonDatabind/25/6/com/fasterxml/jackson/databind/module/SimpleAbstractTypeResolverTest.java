import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test adding valid mappings
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        // Test adding mapping from class to itself
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
        
        // Test adding mapping with invalid subtype
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to class java.util.ArrayList, as latter is not a subtype of former", e.getMessage());
        }
        
        // Test adding mapping with non-abstract superType
        try {
            resolver.addMapping(LinkedList.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.LinkedList since it is not abstract", e.getMessage());
        }
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType type1 = config.constructType(Collection.class);
        JavaType type2 = config.constructType(List.class);
        JavaType type3 = config.constructType(LinkedList.class);
        JavaType type4 = config.constructType(ArrayList.class);
        
        assertEquals(type3, resolver.findTypeMapping(config, type1));
        assertEquals(type3, resolver.findTypeMapping(config, type2));
        assertNull(resolver.findTypeMapping(config, type3));
        assertNull(resolver.findTypeMapping(config, type4));
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(Collection.class);
        
        assertNull(resolver.resolveAbstractType(config, type));
    }
}