import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType type = new JavaType();
        
        // Test adding a mapping with valid superType and subType
        resolver.addMapping(Collection.class, ArrayList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(Collection.class)));

        // Test adding a mapping with superType and subType being the same
        try {
            resolver.addMapping(List.class, List.class);
            fail("IllegalArgumentException was not thrown when adding a mapping with the same superType and subType");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
        
        // Test adding a mapping with subType being not a subtype of superType
        try {
            resolver.addMapping(Collection.class, String.class);
            fail("IllegalArgumentException was not thrown when adding a mapping with a subType that is not a subtype of superType");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class " + Collection.class.getName() + " to " + String.class.getName() + ", as latter is not a subtype of former", e.getMessage());
        }
        
        // Test adding a mapping with non-abstract superType
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("IllegalArgumentException was not thrown when adding a mapping with a non-abstract superType");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class " + ArrayList.class.getName() + " since it is not abstract", e.getMessage());
        }
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType();
        
        // Test finding a known type mapping
        resolver.addMapping(Collection.class, ArrayList.class);
        type.setRawClass(Collection.class);
        JavaType result = resolver.findTypeMapping(config, type);
        assertEquals(ArrayList.class, result.getRawClass());
        
        // Test finding an unknown type mapping
        type.setRawClass(List.class);
        result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType();
        
        // Test resolving an abstract type
        type.setRawClass(Collection.class);
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }
}