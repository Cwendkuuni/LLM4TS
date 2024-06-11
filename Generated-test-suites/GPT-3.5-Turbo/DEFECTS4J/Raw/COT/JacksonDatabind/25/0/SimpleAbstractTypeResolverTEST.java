import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        // Verify that mappings are added correctly
        Class<?> subType1 = resolver._mappings.get(new ClassKey(Collection.class));
        assertEquals(LinkedList.class, subType1);
        Class<?> subType2 = resolver._mappings.get(new ClassKey(List.class));
        assertEquals(LinkedList.class, subType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
        // Should throw IllegalArgumentException when adding mapping from class to itself
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSubtypeMismatch() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        // Should throw IllegalArgumentException when adding mapping from incompatible subtype
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingConcreteClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
        // Should throw IllegalArgumentException when adding mapping from concrete class
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

}