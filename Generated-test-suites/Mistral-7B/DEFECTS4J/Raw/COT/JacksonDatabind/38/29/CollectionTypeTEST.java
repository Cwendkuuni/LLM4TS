import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType expected = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        CollectionType actual = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);

        CollectionType expected = CollectionType.construct(rawType, elemT);

        CollectionType actual = new CollectionType(rawType, null, null, null, elemT, null, null, false);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = new CollectionType(rawType, null, null, null, IntegerType.getInstance());

        collectionType.narrow(LinkedList.class);
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        JavaType contentType = JavaType.constructCollectionType(ArrayList.class, Long.class);

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        CollectionType expected = collectionType.withContentType(contentType);
        CollectionType actual = new CollectionType(rawType, bindings, superClass, superInts, contentType);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        Object typeHandler = new Object() { /* TypeHandler implementation */ };

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        CollectionType expected = collectionType.withTypeHandler(typeHandler);
        CollectionType actual = new CollectionType(rawType, bindings, null, null, elemT, typeHandler, null, false);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        Object typeHandler = new Object() { /* TypeHandler implementation */ };
        JavaType contentType = JavaType.constructCollectionType(ArrayList.class, Long.class);

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        CollectionType expected = collectionType.withContentTypeHandler(typeHandler);
        CollectionType actual = new CollectionType(rawType, bindings, null, null, contentType.withTypeHandler(typeHandler), elemT, typeHandler, false);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        Object valueHandler = new Object() { /* ValueHandler implementation */ };

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        CollectionType expected = collectionType.withValueHandler(valueHandler);
        CollectionType actual = new CollectionType(rawType, bindings, null, null, elemT, null, valueHandler, false);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        Object valueHandler = new Object() { /* ValueHandler implementation */ };
        JavaType contentType = JavaType.constructCollectionType(ArrayList.class, Long.class);

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        CollectionType expected = collectionType.withContentValueHandler(valueHandler);
        CollectionType actual = new CollectionType(rawType, bindings, null, null, contentType.withValueHandler(valueHandler), elemT, null, false);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        CollectionType expected = collectionType.withStaticTyping();
        CollectionType actual = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, true);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = LinkedList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructReferenceType(LinkedList.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        CollectionType expected = collectionType.refine(ArrayList.class, bindings, superClass, superInts);
        CollectionType actual = new CollectionType(ArrayList.class, bindings, superClass, superInts, elemT);

        assertEquals("Collection types should be equal", expected, actual);
    }

    @Test
    public void testToString() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT);

        String expected = "[collection type; class " + rawType.getName() + ", contains " + elemT + "]";
        String actual = collectionType.toString();

        assertEquals("ToString should match", expected, actual);
    }
}