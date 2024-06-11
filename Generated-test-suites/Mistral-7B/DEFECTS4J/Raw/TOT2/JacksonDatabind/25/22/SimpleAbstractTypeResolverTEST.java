import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest1 {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> collectionClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;
        resolver.addMapping(collectionClass, linkedListClass);
        assertNotNull(resolver._mappings.get(new ClassKey(collectionClass)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(collectionClass)));
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> collectionClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;
        resolver.addMapping(collectionClass, linkedListClass);
        JavaType type = JavaType.construct(collectionClass, collectionClass.getComponentType());
        JavaType mappedType = resolver.findTypeMapping(new DeserializationConfig(), type);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType type = JavaType.construct(List.class, Object.class);
        assertNull(resolver.resolveAbstractType(new DeserializationConfig(), type));
    }
}