import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        // Assertions
        assertEquals(2, resolver._mappings.size());
        assertTrue(resolver._mappings.containsKey(new ClassKey(Collection.class)));
        assertTrue(resolver._mappings.containsKey(new ClassKey(List.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    // TODO: Add more test cases for other methods in the SimpleAbstractTypeResolver class

}