import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {
    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        JavaType inputType = JavaType.constructCollectionType(ArrayList.class, String.class);
        JavaType expectedType = JavaType.constructCollectionType(LinkedList.class, String.class);
        JavaType actualType = resolver.findTypeMapping(null, inputType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testResolveAbstractType() {
        JavaType inputType = JavaType.constructTypeReference(ArrayList.class, "T");
        JavaType expectedType = null; // Since this method returns null
        JavaType actualType = resolver.resolveAbstractType(null, inputType);
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperclass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class); // ArrayList is not abstract
    }

    @Test(expected = IllegalStateException.class)
    public void testAddMappingWithDuplicateMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
    }

    @Test(expected = NullPointerException.class)
    public void testAddMappingWithNullSupertype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(null, LinkedList.class);
    }

    @Test(expected = NullPointerException.class)
    public void testAddMappingWithNullSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, null);
    }

    @Test
    public void testFindTypeMappingWithNullDeserializationConfig() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, String.class);
        JavaType result = resolver.findTypeMapping(null, javaType);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFindTypeMappingWithNullJavaType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType result = resolver.findTypeMapping(null, null);
        assertNull(result);
    }
}