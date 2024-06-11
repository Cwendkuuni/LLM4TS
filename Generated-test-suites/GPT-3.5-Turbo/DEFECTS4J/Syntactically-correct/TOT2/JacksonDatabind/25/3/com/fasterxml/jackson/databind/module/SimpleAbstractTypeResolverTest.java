import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        assertTrue(resolver._mappings.containsKey(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
    }

    @Test
    public void testAddMapping_Exception() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType inputType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType resolvedType = resolver.findTypeMapping(null, inputType);
        assertEquals(TypeFactory.defaultInstance().constructType(LinkedList.class), resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType inputType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(null, inputType);
        assertNull(resolvedType);
    }
}