
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding a valid mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        // Test adding a mapping from a class to itself
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test adding a mapping where the subtype is not a subclass of the supertype
        try {
            resolver.addMapping(Collection.class, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test adding a mapping for a non-abstract superType
        try {
            resolver.addMapping(String.class, LinkedList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        DeserializationConfig config = null;
        JavaType type = new JavaType();
        type.setRawClass(Collection.class);
        JavaType resolvedType = resolver.findTypeMapping(config, type);

        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = null;
        JavaType type = new JavaType();
        JavaType resolvedType = resolver.resolveAbstractType(config, type);

        assertNull(resolvedType);
    }
}
