import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = CollectionType.construct(ArrayList.class, String.class);
    }

    @Test
    public void testConstruct() {
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);

        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(Integer.class, newCollectionType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Object newTypeHandler = new Object();
        CollectionType newCollectionType = collectionType.withTypeHandler(newTypeHandler);

        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(newTypeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object newTypeHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(newTypeHandler);

        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(newTypeHandler, newCollectionType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object newValueHandler = new Object();
        CollectionType newCollectionType = collectionType.withValueHandler(newValueHandler);

        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(newValueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object newValueHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentValueHandler(newValueHandler);

        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(newValueHandler, newCollectionType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType newCollectionType = collectionType.withStaticTyping();

        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertTrue(newCollectionType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        CollectionType newCollectionType = collectionType.refine(ArrayList.class, bindings, superClass, superInterfaces);

        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getTypeBindings());
    }
}