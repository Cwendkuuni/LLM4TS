import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, SimpleType.construct(String.class));
        assertNotNull(collectionType);
        assertEquals(ArrayList.class, collectionType.getClass());
        assertEquals(SimpleType.construct(String.class), collectionType.getElementType());
    }

    @Test
    public void testWithContentType() {
        CollectionType collectionType = CollectionType.construct(HashSet.class, null, null, null, SimpleType.construct(Integer.class));
        CollectionType newType = collectionType.withContentType(SimpleType.construct(Integer.class));
        assertEquals(collectionType, newType);
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = CollectionType.construct(HashSet.class, null, null, null, SimpleType.construct(Integer.class));
        CollectionType newType = collectionType.withTypeHandler(new TypeHandler());
        assertEquals(collectionType, newType);
    }

    // Additional test cases for other methods can be added here

}