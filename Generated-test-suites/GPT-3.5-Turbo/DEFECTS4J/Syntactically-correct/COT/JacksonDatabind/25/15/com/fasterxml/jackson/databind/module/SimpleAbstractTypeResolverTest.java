import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Positive test case
        resolver.addMapping(Collection.class, LinkedList.class);
        
        // Negative test case: Adding mapping from class to itself
        try {
            resolver.addMapping(LinkedList.class, LinkedList.class);
            fail("Expected IllegalArgumentException when adding mapping from class to itself");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
        
        // Negative test case: Adding mapping where subtype is not a subtype of the superclass
        try {
            resolver.addMapping(Collection.class, HashMap.class);
            fail("Expected IllegalArgumentException when adding mapping where subtype is not a subtype of the superclass");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to java.util.HashMap, as latter is not a subtype of former", e.getMessage());
        }
        
        // Negative test case: Adding mapping for a non-abstract superclass
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Expected IllegalArgumentException when adding mapping for a non-abstract superclass");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        DeserializationConfig config = new DeserializationConfig();
        
        // Positive test case
        JavaType type = config.constructType(Collection.class);
        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
        
        // Negative test case: Mapping not found
        type = config.constructType(List.class);
        resolvedType = resolver.findTypeMapping(config, type);
        assertNull(resolvedType);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        DeserializationConfig config = new DeserializationConfig();
        
        // Positive test case
        JavaType type = config.constructType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }
}