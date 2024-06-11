import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMappingWithSameClasses() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;

        try {
            resolver.addMapping(listClass, listClass);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMappingNonAssignableTypes() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;
        Class<String> stringClass = String.class;

        try {
            resolver.addMapping(listClass, stringClass);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMappingNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;

        try {
            resolver.addMapping(Collections.synchronizedList(new ArrayList<>()).getClass(), listClass);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMappingDuplicateMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        resolver.addMapping(Collection.class, linkedListClass);
        resolver.addMapping(listClass, linkedListClass);

        JavaType listType = JavaType.construct(List.class, Integer.class);
        JavaType linkedListType = JavaType.construct(LinkedList.class, Integer.class);

        Class<?> actual = resolver.findTypeMapping(null, listType).getRawClass();
        Class<?> expected = linkedListClass;

        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void testAddMappingValidMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        JavaType listType = JavaType.construct(List.class, Integer.class);
        JavaType linkedListType = JavaType.construct(LinkedList.class, Integer.class);

        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(listClass, linkedListClass);

        Class<?> actual = resolver.findTypeMapping(null, listType).getRawClass();
        Class<?> expected = linkedListClass;

        assertEquals(expected.getName(), actual.getName());
    }
}