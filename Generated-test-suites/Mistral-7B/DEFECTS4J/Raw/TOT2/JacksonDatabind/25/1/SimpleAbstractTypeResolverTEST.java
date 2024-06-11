import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> list = List.class;
        Class<LinkedList> linkedList = LinkedList.class;
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        JavaType type = JavaType.construct(LinkedList.class, List.class);
        JavaType mappedType = resolver.findTypeMapping(null, type);
        assertTrue(mappedType.getRawClass() == LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> list = List.class;
        Class<LinkedList> linkedList = LinkedList.class;
        JavaType type = JavaType.construct(LinkedList.class, List.class);
        JavaType mappedType = resolver.findTypeMapping(null, type);
        assertTrue(mappedType.getRawClass() == LinkedList.class);
    }

    // TODO: Test for resolveAbstractType method
}