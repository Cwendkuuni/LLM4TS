import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
        assertEquals("collection type; class List, contains class [I]", collectionType.toString());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        assertEquals("collection type; class List, contains class [I]", collectionType.toString());
    }

    @Test
    public void testNarrowDeprecated() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, elemType);
        CollectionType narrowedType = collectionType._narrow(ArrayList.class);
        assertEquals("collection type; class ArrayList, contains class [I]", narrowedType.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, elemType);
        JavaType narrowedType = collectionType.withContentType(HashMap.class);
        collectionType._narrow(HashMap.class); // this line should throw an exception
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        JavaType contentType = JavaType.construct(String.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType);
        CollectionType withContentType = collectionType.withContentType(contentType);
        assertEquals("collection type; class List, contains class java.lang.String", withContentType.toString());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType);
        Object typeHandler = new Object();
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertEquals("collection type; class List, contains class [I]", withTypeHandler.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        JavaType contentType = JavaType.construct(String.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType);
        Object typeHandler = new Object();
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(typeHandler);
        assertEquals("collection type; class List, contains class java.lang.String", withContentTypeHandler.toString());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType);
        Object valueHandler = new Object();
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertEquals("collection type; class List, contains class [I]", withValueHandler.toString());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        JavaType contentType = JavaType.construct(String.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType);
        Object valueHandler = new Object();
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(valueHandler);
        assertEquals("collection type; class List, contains class java.lang.String", withContentValueHandler.toString());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertEquals("collection type; class List, contains class [I]", withStaticTyping.toString());
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testRefine() {
        Class<List<Integer>> listClass = List.class;
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elemType);
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(arrayListClass);
        JavaType[] superInterfaces = null;
        CollectionType refinedType = collectionType.refine(arrayListClass, bindings, superClass, superInterfaces);
        assertEquals("collection type; class ArrayList, contains class [I]", refinedType.toString());
    }

}