import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<Integer>> listInt = List.class.getGenericType();
        Class<LinkedList<Integer>> linkedListInt = LinkedList.class;

        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType expectedType = JavaType.constructParametrizedType(LinkedList.class, Integer.class);
        JavaType actualType = resolver.findTypeMapping(null, JavaType.constructRawType(listInt));

        assertEquals(expectedType, actualType);
    }
}