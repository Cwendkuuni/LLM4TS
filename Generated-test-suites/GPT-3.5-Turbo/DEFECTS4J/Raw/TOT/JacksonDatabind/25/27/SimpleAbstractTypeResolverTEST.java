import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test adding a valid mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        
        // Test adding a mapping from a class to itself
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
        
        // Test adding a mapping from subclass to superclass
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to class java.util.ArrayList, as latter is not a subtype of former", e.getMessage());
        }
        
        // Test adding a mapping for a non-abstract class
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Add a mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        
        // Test finding a type mapping for a mapped class
        JavaType rawType = new JavaType(Collection.class);
        JavaType resolvedType = resolver.findTypeMapping(null, rawType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
        
        // Test finding a type mapping for a non-mapped class
        rawType = new JavaType(List.class);
        resolvedType = resolver.findTypeMapping(null, rawType);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test resolving an abstract type
        JavaType rawType = new JavaType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(null, rawType);
        assertNull(resolvedType);
    }

}