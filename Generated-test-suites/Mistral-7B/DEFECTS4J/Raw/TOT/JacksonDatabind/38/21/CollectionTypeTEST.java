import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {

    @Test
    public void testConstructWithValidArguments() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);

        assertEquals("collection type; class List, contains class Int", collectionType.toString());
    }

    @Test
    public void testConstructWithInvalidArguments() {
        Class<?> rawType = Object.class;
        JavaType elemT = JavaType.constructRawType(String.class);

        try {
            CollectionType collectionType = CollectionType.construct(rawType, elemT);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);
        JavaType contentType = JavaType.constructRawType(Long.class);

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
        CollectionType newCollectionType = collectionType.withContentType(contentType);

        assertEquals("collection type; class List, contains class Long", newCollectionType.toString());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
        Object typeHandler = new Object();

        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);

        assertEquals("collection type; class List, contains class Int, type handler: " + typeHandler, newCollectionType.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);
        Object contentTypeHandler = new Object();

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
        CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);

        assertEquals("collection type; class List, contains class Int, content type handler: " + contentTypeHandler, newCollectionType.toString());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
        Object valueHandler = new Object();

        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);

        assertEquals("collection type; class List, contains class Int, value handler: " + valueHandler, newCollectionType.toString());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);
        Object contentValueHandler = new Object();

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);

        assertEquals("collection type; class List, contains class Int, content value handler: " + contentValueHandler, newCollectionType.toString());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructRawType(Integer.class);

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
        CollectionType newCollectionType = collectionType.withStaticTyping();

        assertEquals("collection type; class List<Int>, contains class Int", newCollectionType.toString());
    }

    @Test
    public void testRefine() {
        Class<ArrayList<String>> arrayListClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructRawListType(String.class);
        JavaType[] superInterfaces = new JavaType[] {JavaType.constructRawType(Serializable.class)};
        JavaType elemT = JavaType.constructRawType(String.class);

        CollectionType collectionType = new CollectionType(arrayListClass, bindings, superClass, superInterfaces, elemT);
        Class<LinkedList<String>> linkedListClass = LinkedList.class;

        CollectionType newCollectionType = collectionType.refine(linkedListClass, bindings, superClass, superInterfaces);

        assertEquals("collection type; class LinkedList, contains class String", newCollectionType.toString());
    }
}