import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstructRawTypeElemT() {
        CollectionType collectionType = CollectionType.construct(String.class, JavaType.String);
        assertEquals(String.class, collectionType.getRawType());
        assertEquals(JavaType.String, collectionType.getElementType());
    }
}