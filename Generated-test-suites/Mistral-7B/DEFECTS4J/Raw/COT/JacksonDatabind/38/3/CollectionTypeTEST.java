import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = List.class;
        JavaType javaType = CollectionType.construct(listClass, null, null, null, Integer.TYPE);
        CollectionType collectionType = (CollectionType) javaType;
        assertEquals("[collection type; class List, contains class Int]", collectionType.toString());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listClass = List.class;
        JavaType javaType = CollectionType.construct(listClass, Integer.TYPE);
        CollectionType collectionType = (CollectionType) javaType;
        assertEquals("[collection type; class List, contains class Int]", collectionType.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        CollectionType collectionType = new CollectionType(arrayListClass, null);
        JavaType javaType = collectionType._narrow(ArrayList.class);
    }

    @Test
    public void testWithContentType() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = new CollectionType(listClass, null, null, null, String.class);
        JavaType javaType = collectionType.withContentType(String[].class);
        assertEquals("[collection type; class List, contains class [Ljava.lang.String;]", javaType.toString());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        CollectionType collectionType = new CollectionType(listClass, null, null, null, Integer.TYPE);
        Object handler = new Object();
        JavaType javaType = collectionType.withTypeHandler(handler);
        assertEquals("[collection type; class List, contains class Int, type handler: class " + handler.getClass() + "]", javaType.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = new CollectionType(listClass, null, null, null, String.class);
        Object handler = new Object();
        JavaType javaType = collectionType.withContentTypeHandler(handler);
        assertEquals("[collection type; class List, contains class java.lang.Object, type handler: class " + handler.getClass() + "]", javaType.toString());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = List.class;
        CollectionType collectionType = new CollectionType(listClass, null, null, null, Integer.TYPE);
        Object handler = new Object();
        JavaType javaType = collectionType.withValueHandler(handler);
        assertEquals("[collection type; class List, contains class Int, value handler: class " + handler.getClass() + "]", javaType.toString());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = new CollectionType(listClass, null, null, null, String.class);
        Object handler = new Object();
        JavaType javaType = collectionType.withContentValueHandler(handler);
        assertEquals("[collection type; class List, contains class java.lang.Object, value handler: class " + handler.getClass() + "]", javaType.toString());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = List.class;
        CollectionType collectionType = new CollectionType(listClass, null, null, null, Integer.TYPE);
        JavaType javaType = collectionType.withStaticTyping();
        assertEquals("[collection type; class List<Integer>, static]", javaType.toString());
    }

    @Test
    public void testRefine() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        CollectionType collectionType = new CollectionType(List.class, null);
        JavaType javaType = collectionType.refine(arrayListClass.getClass(), null, null, null);
        assertEquals("[collection type; class ArrayList, contains class Integer]", javaType.toString());
    }
}