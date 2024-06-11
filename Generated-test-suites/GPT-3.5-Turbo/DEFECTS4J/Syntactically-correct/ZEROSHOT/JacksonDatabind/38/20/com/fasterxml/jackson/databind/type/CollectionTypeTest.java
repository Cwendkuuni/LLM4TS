import org.junit.*;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionType);
    }

    @Deprecated
    @Test
    public void testConstruct2() {
        Class<?> rawType = null;
        JavaType elemT = null;
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        assertNotNull(collectionType);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = collectionType._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = collectionType.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        String result = collectionType.toString();
        assertNotNull(result);
    }
}