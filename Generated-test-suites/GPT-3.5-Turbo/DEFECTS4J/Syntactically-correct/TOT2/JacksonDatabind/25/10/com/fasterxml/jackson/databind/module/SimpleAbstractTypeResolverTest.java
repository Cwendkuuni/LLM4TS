import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        // Test adding a valid mapping
        resolver.addMapping(List.class, ArrayList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSelfMapping() {
        // Test adding a mapping from a class to itself
        resolver.addMapping(List.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidMapping() {
        // Test adding an invalid mapping
        resolver.addMapping(List.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractSuperType() {
        // Test adding a mapping with a non-abstract super type
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        // Test finding a type mapping for a known mapping
        resolver.addMapping(List.class, ArrayList.class);
        assertEquals(ArrayList.class, resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(List.class)).getRawClass());
    }

    @Test
    public void testFindTypeMappingNotFound() {
        // Test finding a type mapping for an unknown mapping
        assertNull(resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Map.class)));
    }

    @Test
    public void testResolveAbstractType() {
        // Test resolving an abstract type
        assertNull(resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(List.class)));
    }
}