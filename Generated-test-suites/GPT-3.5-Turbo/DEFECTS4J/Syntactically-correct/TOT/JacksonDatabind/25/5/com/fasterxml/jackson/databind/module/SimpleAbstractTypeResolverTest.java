import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding valid mapping
        resolver.addMapping(Collection.class, LinkedList.class);

        // Test adding invalid mapping where superType is not abstract
        try {
            resolver.addMapping(List.class, ArrayList.class);
        } catch (IllegalArgumentException ex) {
            assertEquals("Can not add mapping from class java.util.List since it is not abstract", ex.getMessage());
        }

        // Test adding invalid mapping where superType is the same as subType
        try {
            resolver.addMapping(LinkedList.class, LinkedList.class);
        } catch (IllegalArgumentException ex) {
            assertEquals("Can not add mapping from class java.util.LinkedList to java.util.LinkedList, as latter is not a subtype of former", ex.getMessage());
        }

        // Test adding invalid mapping where subType is not a subtype of superType
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
        } catch (IllegalArgumentException ex) {
            assertEquals("Can not add mapping from class java.util.Collection to java.util.ArrayList, as latter is not a subtype of former", ex.getMessage());
        }
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        // Test finding type mapping for a superclass with a valid mapping
        DeserializationConfig config = null; // Mocked DeserializationConfig
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        assertTrue(mappedType.getRawClass().equals(LinkedList.class));

        // Test finding type mapping for a superclass without a mapping
        type = TypeFactory.defaultInstance().constructType(Set.class);
        mappedType = resolver.findTypeMapping(config, type);
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test resolving an abstract type (should always return null)
        DeserializationConfig config = null; // Mocked DeserializationConfig
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }
}