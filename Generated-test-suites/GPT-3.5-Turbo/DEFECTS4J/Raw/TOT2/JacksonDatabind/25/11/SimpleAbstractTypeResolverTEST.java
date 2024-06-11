import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        assertNotNull(resolver);
    }

    @Test
    public void testAddMapping_SameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(List.class, List.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_SubclassMismatch() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(Collection.class, LinkedList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to class java.util.LinkedList, as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_NonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        JavaType result = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(List.class));
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType result = resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(List.class));
        assertNull(result);
    }
}