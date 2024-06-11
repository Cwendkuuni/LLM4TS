
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(Object.class, null, null, null, null);
        assertNotNull(collectionType);
        assertEquals(Object.class, collectionType.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = CollectionType.construct(Object.class, null, null, null, null);
        Object typeHandler = new Object();
        CollectionType result = collectionType.withTypeHandler(typeHandler);
        assertNotNull(result);
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType = CollectionType.construct(Object.class, null, null, null, null);
        Object typeHandler = new Object();
        CollectionType result = collectionType.withContentTypeHandler(typeHandler);
        assertNotNull(result);
        assertNotNull(result.getContentType());
        assertEquals(typeHandler, result.getContentType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = CollectionType.construct(Object.class, null, null, null, null);
        CollectionType result = collectionType.withStaticTyping();
        assertNotNull(result);
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(Object.class, null, null, null, null);
        CollectionType result = collectionType.refine(String.class, null, null, null);
        assertNotNull(result);
        assertEquals(String.class, result.getRawClass());
    }
}

