import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(HashMap.class, LinkedList.class);
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.HashMap to java.util.LinkedList, as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_selfMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(List.class, List.class);
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.List to itself", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_invalidSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping_mappingExists() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        DeserializationConfig config = null;
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
        JavaType resolvedType = resolver.findTypeMapping(config, collectionType);

        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_mappingDoesNotExist() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = null;
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
        JavaType resolvedType = resolver.findTypeMapping(config, collectionType);

        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = null;
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, collectionType);

        assertNull(resolvedType);
    }

}