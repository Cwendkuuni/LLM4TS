import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstructor() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        assertNotNull(collectionType);
    }

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(null, null, null, null, null);
        assertNotNull(collectionType);
    }

    @Test
    public void test_refine() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType javaType = collectionType.refine(null, null, null, null);
        assertNotNull(javaType);
    }

    @Test
    public void test_toString() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        String string = collectionType.toString();
        assertNotNull(string);
        assertTrue(string.contains("collection type"));
        assertTrue(string.contains("class"));
        assertTrue(string.contains("contains"));
    }
}