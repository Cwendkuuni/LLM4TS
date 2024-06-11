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
    public void testAddMapping_typicalCase() {
        Class<LinkedList> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;

        resolver.addMapping(superType, subType);

        // Assert that the mapping has been added correctly
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameType() {
        Class<HashMap> type = HashMap.class;

        resolver.addMapping(type, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_subTypeNotSubclass() {
        Class<List> superType = List.class;
        Class<LinkedList> subType = LinkedList.class;

        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_superTypeNotAbstract() {
        Class<LinkedList> superType = LinkedList.class;
        Class<LinkedList> subType = LinkedList.class;

        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping_mappingExists() {
        // Set up the mappings
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(superType);

        JavaType result = resolver.findTypeMapping(config, type);

        // Assert that the correct subtype is returned
        assertEquals(type.narrowBy(subType), result);
    }

    @Test
    public void testFindTypeMapping_mappingDoesNotExist() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);

        JavaType result = resolver.findTypeMapping(config, type);

        // Assert that null is returned if there is no mapping
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);

        JavaType result = resolver.resolveAbstractType(config, type);

        // Assert that null is always returned, since there is no materialization
        assertNull(result);
    }
}