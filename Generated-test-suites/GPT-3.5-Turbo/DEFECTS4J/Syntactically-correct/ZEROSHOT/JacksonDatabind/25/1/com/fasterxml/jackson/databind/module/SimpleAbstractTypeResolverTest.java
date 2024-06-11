import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        // Verify that the mappings are added correctly
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_selfMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_subtypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_nonAbstractMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        // Test mapping exists
        assertEquals(LinkedList.class, resolver.findTypeMapping(null, new JavaType(Collection.class)));
        assertEquals(LinkedList.class, resolver.findTypeMapping(null, new JavaType(List.class)));
        // Test mapping does not exist
        assertNull(resolver.findTypeMapping(null, new JavaType(Set.class)));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Test always returns null
        assertNull(resolver.resolveAbstractType(null, new JavaType(Collection.class)));
    }

}