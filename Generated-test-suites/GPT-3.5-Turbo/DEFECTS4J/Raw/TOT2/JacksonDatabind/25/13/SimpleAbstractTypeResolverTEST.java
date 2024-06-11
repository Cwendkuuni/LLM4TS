import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);

        assertNotNull(resolver._mappings.get(new ClassKey(List.class)));
        assertEquals(ArrayList.class, resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test
    public void testAddMappingInvalidSelfMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        try {
            resolver.addMapping(List.class, List.class);
            fail("Expected IllegalArgumentException for adding mapping from class to itself");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testAddMappingInvalidSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("Expected IllegalArgumentException for adding mapping with subtype that is not a subtype of the superclass");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testAddMappingNonAbstractSuperclass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Expected IllegalArgumentException for adding mapping with non-abstract superclass");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);

        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType inputType = new JavaType(Object.class);

        JavaType resolvedType = resolver.findTypeMapping(config, inputType);

        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType inputType = new JavaType(Object.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, inputType);

        assertNull(resolvedType);
    }
}