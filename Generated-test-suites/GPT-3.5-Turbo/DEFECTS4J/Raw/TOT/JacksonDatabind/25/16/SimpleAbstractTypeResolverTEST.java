import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping_success() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        assertNotNull(resolver._mappings);
        assertEquals(1, resolver._mappings.size());
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(LinkedList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping_success() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        assertNotNull(resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Collection.class)));
    }

    @Test
    public void testFindTypeMapping_mappingNotFound() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        assertNull(resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(ArrayList.class)));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNull(resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Collection.class)));
    }
}