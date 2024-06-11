import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        // Assert that the mappings were added correctly
        assertEquals(LinkedList.class, resolver._mappings.get(Collection.class));
        assertEquals(LinkedList.class, resolver._mappings.get(List.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSelfClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Attempt to add a mapping from a class to itself
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Attempt to add a mapping from class A to a class that is not a subtype of A
        resolver.addMapping(Collection.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Attempt to add a mapping with a non-abstract super type
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        // Create a JavaType object for Collection
        JavaType collectionType = TypeFactory.defaultInstance().constructType(Collection.class);
        // Resolve the type mapping
        JavaType resolvedType = resolver.findTypeMapping(null, collectionType);
        // Assert that the resolved type is LinkedList
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Create a JavaType object for Collection
        JavaType collectionType = TypeFactory.defaultInstance().constructType(Collection.class);
        // Resolve the abstract type
        JavaType resolvedType = resolver.resolveAbstractType(null, collectionType);
        // Assert that the resolved type is null
        assertEquals(null, resolvedType);
    }
}