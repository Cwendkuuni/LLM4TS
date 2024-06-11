
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        // Verify that the mapping was added correctly
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<Collection> type = Collection.class;
        resolver.addMapping(type, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNoSubtypeRelationship() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(LinkedList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(superType);
        JavaType expected = TypeFactory.defaultInstance().constructType(subType);
        JavaType result = resolver.findTypeMapping(config, type);
        // Verify that the correct type mapping is returned
        assertEquals(expected, result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType result = resolver.resolveAbstractType(config, type);
        // Verify that null is returned since no materialization is done
        assertNull(result);
    }
}

