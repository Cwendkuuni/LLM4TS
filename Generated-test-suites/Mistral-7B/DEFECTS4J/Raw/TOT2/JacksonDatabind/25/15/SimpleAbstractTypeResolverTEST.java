
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

    @Test
    public void testAddMappingSameClass() {
        Class<List> collectionClass = List.class;
        try {
            resolver.addMapping(collectionClass, collectionClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMappingNotAbstractSuperType() {
        Class<List> collectionClass = List.class;
        try {
            resolver.addMapping(collectionClass, LinkedList.class);
            resolver.addMapping(int.class, String.class); // Int is not abstract
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMappingNotSubType() {
        Class<List> collectionClass = List.class;
        Class<ArrayList> arrayListClass = ArrayList.class;

        try {
            resolver.addMapping(collectionClass, arrayListClass);
            resolver.addMapping(int.class, String.class); // Int is not a supertype of String
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMappingValid() {
        Class<List> collectionClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        resolver.addMapping(collectionClass, linkedListClass);
        Class<?> mappedClass = resolver._mappings.get(new ClassKey(collectionClass));
        assertEquals(linkedListClass, mappedClass);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> collectionClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;
        resolver.addMapping(collectionClass, linkedListClass);

        JavaType javaType = TypeFactory.defaultInstance().constructType(collectionClass);
        JavaType mappedType = resolver.findTypeMapping(null, javaType);

        assertEquals(linkedListClass, mappedType.getRawClass());
    }
}