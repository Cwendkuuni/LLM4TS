import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
        assertEquals("collection type; class List, contains class Int", collectionType.toString());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = new CollectionType(listClass, elemType);
        assertEquals("collection type; class List, contains class Int", collectionType.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        CollectionType collectionType = new CollectionType(List.class, null);
        Class<ArrayList<Integer>> ArrayListClass = ArrayList.class;
        collectionType.narrow(ArrayListClass);
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType, null, null, false);
        JavaType newContentType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        assertEquals("collection type; class ArrayList, contains class Int", newCollectionType.toString());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType, null, null, false);
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertEquals("collection type; class List, contains class Int", newCollectionType.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType, null, null, false);
        Object contentTypeHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertEquals("collection type; class List, contains class Int", newCollectionType.toString());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType, null, null, false);
        Object valueHandler = new Object();
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertEquals("collection type; class List, contains class Int", newCollectionType.toString());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType, null, null, false);
        Object contentValueHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
        assertEquals("collection type; class List, contains class Int", newCollectionType.toString());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType, null, null, false);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType, null, null, false);
        Class<ArrayList<Integer>> ArrayListClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType refinedType = collectionType.refine(ArrayListClass.getClass(), bindings, null, null);
        assertEquals("collection type; class ArrayList, contains class Int", refinedType.toString());
    }
}