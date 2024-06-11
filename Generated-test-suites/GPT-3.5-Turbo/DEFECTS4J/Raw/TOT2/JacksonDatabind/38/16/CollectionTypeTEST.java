import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), 
            null, null, new JavaType());
        assertNotNull(collectionType);
        assertEquals("ArrayList", collectionType.getRawClass().getSimpleName());
    }

    @Test
    public void testWithContentType() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), 
            null, null, new JavaType());
        JavaType newContentType = new JavaType();
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        assertNotNull(newCollectionType);
        assertEquals(newContentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), 
            null, null, new JavaType());
        CollectionType staticCollectionType = collectionType.withStaticTyping();
        assertTrue(staticCollectionType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), 
            null, null, new JavaType());
        CollectionType refinedType = collectionType.refine(List.class, new TypeBindings(), 
            null, new JavaType[]{new JavaType()});
        assertNotNull(refinedType);
        assertEquals("List", refinedType.getRawClass().getSimpleName());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), 
            null, null, new JavaType());
        assertEquals("[collection type; class java.util.ArrayList, contains JavaType]", collectionType.toString());
    }

}