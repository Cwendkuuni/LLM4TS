import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Number.class, Integer.class); // positive case

        // Check that mapping was added successfully
        assertNotNull(resolver._mappings.get(new ClassKey(Number.class)));
        assertEquals(Integer.class, resolver._mappings.get(new ClassKey(Number.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSelf() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class); // adding mapping from class to itself
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class); // adding mapping from non-subtype to supertype
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingConcreteSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class); // adding mapping with concrete super type
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Number.class, Integer.class); // Add mapping

        JavaType originalType = TypeFactory.defaultInstance().constructType(Number.class);
        JavaType resolvedType = resolver.findTypeMapping(null, originalType);

        assertNotNull(resolvedType);
        assertEquals(Integer.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType originalType = TypeFactory.defaultInstance().constructType(Number.class);
        JavaType resolvedType = resolver.resolveAbstractType(null, originalType);

        assertNull(resolvedType);
    }
}