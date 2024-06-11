
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CollectionTypeTest {

    @Test
    public void testToString() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = ClassUtil.findConcreteSuperclass(listClass, Collection.class);
        JavaType[] superInts = new JavaType[0];

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemType);
        String actual = collectionType.toString();
        String expected = "[collection type; class List<Integer>, contains Integer]";
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringNull() {
        String actual = toString((CollectionType) null);
        String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringEmpty() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = ClassUtil.findConcreteSuperclass(listClass, Collection.class);
        JavaType[] superInts = new JavaType[0];

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemType);

        String actual = collectionType.toString();
        String expected = "[collection type; class List<>, contains Object]";
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringGeneric() {
        Class<List<Map<String, Integer>>> listClass = List.class;
        JavaType elemType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType mapKeyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mapValueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructTypeParameterizedType(Map.class, mapKeyType, mapValueType);

        TypeBindings bindings = new TypeBindings();
        JavaType superClass = ClassUtil.findConcreteSuperclass(listClass, Collection.class);
        JavaType[] superInts = new JavaType[0];

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, mapType);

        String actual = collectionType.toString();
        String expected = "[collection type; class List<Map<String, Integer>>, contains Map<String, Integer>]";
        assertEquals(expected, actual);
    }

    // Add test cases for other methods here

    private static String toString(CollectionType collectionType) {
        return collectionType.toString();
    }
}