import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.type.ClassKey;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        // Test valid mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        JavaType expectedType = new JavaType(LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(null, new JavaType(listClass));
        assertEquals(expectedType, actualType);

        // Test invalid mapping
        try {
            resolver.addMapping(List.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test mapping with same class
        try {
            resolver.addMapping(ArrayList.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test mapping with non-abstract superclass
        try {
            Class<String> stringClass = String.class;
            resolver.addMapping(String.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFindTypeMappingWithNullResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType expectedType = new JavaType(LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(null, new JavaType(List.class));
        assertEquals(expectedType, actualType);

        JavaType nullType = null;
        assertNull(resolver.findTypeMapping(null, nullType));
    }

    @Test
    public void testFindTypeMappingWithNullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType expectedType = new JavaType(LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(null, new JavaType(List.class));
        assertEquals(expectedType, actualType);

        JavaType nullType = null;
        assertNull(resolver.findTypeMapping(null, nullType));
    }

    @Test
    public void testFindTypeMappingWithEmptyMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType expectedType = null;
        JavaType actualType = resolver.findTypeMapping(null, new JavaType(List.class));
        assertEquals(expectedType, actualType);
    }

    @Test(expected = NullPointerException.class)
    public void testFindTypeMappingWithNullMappingKey() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType nullMappingKey = null;
        JavaType type = new JavaType(List.class);
        JavaType expectedType = null;
        JavaType actualType = resolver.findTypeMapping(null, type);
        assertEquals(expectedType, actualType);

        JavaType actualType2 = resolver.findTypeMapping(null, nullMappingKey);
        fail("Expected NullPointerException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMappingWithInvalidMappingKey() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType invalidMappingKey = new JavaType(int[].class);
        JavaType type = new JavaType(List.class);
        JavaType expectedType = null;
        JavaType actualType = resolver.findTypeMapping(null, type);
        assertEquals(expectedType, actualType);

        JavaType actualType2 = resolver.findTypeMapping(null, invalidMappingKey);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testResolveAbstractTypeWithNullConfig() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType type = new JavaType(List.class);
        JavaType expectedType = null;
        JavaType actualType = resolver.resolveAbstractType(null, type);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testResolveAbstractTypeWithEmptyMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType type = new JavaType(List.class);
        JavaType expectedType = null;
        JavaType actualType = resolver.resolveAbstractType(null, type);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testResolveAbstractTypeWithValidMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        JavaType expectedType = new JavaType(LinkedList.class);
        JavaType actualType = resolver.resolveAbstractType(null, new JavaType(listClass));
        assertEquals(expectedType, actualType);
    }
}