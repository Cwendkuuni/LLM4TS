import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Should throw IllegalArgumentException for mapping from class to itself");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            resolver.addMapping(List.class, ArrayList.class);
            fail("Should throw IllegalArgumentException for mapping non-subtype");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            resolver.addMapping(ArrayList.class, List.class);
            fail("Should throw IllegalArgumentException for mapping abstract class");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);

        DeserializationConfig config = null; // dummy config
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JavaType resolvedType = resolver.findTypeMapping(config, type);

        assertNotNull(resolvedType);
        assertEquals(ArrayList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = null; // dummy config
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);

        assertNull(resolvedType);
    }
}