import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {
    @Test
    public void testCollectionLikeTypeConstruction() {
        // Basic test
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructParametrizedType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
        assertEquals(rawType, collectionLikeType.getType());
        assertEquals(elemT, collectionLikeType.getContentType());

        // Edge case: null argument for bindings
        bindings = null;
        collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
        assertEquals(rawType, collectionLikeType.getType());
        assertEquals(elemT, collectionLikeType.getContentType());

        // Exception scenario: invalid raw type
        rawType = Object.class;
        try {
            collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}