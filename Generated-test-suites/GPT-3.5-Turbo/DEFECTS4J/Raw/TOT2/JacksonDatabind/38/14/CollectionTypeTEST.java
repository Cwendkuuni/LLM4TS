import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertNotNull(collectionType);
    }

    @Test
    public void testConstructWithElemType() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, String.class);
        assertNotNull(collectionType);
    }

    @Test
    public void testNarrow() {
        CollectionType baseCollectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType narrowedCollectionType = (CollectionType) baseCollectionType._narrow(List.class);
        assertNotNull(narrowedCollectionType);
    }

    @Test
    public void testWithContentType() {
        CollectionType baseCollectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType contentTypeCollectionType = (CollectionType) baseCollectionType.withContentType(Integer.class);
        assertNotNull(contentTypeCollectionType);
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType baseCollectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType typeHandlerCollectionType = (CollectionType) baseCollectionType.withTypeHandler(new Object());
        assertNotNull(typeHandlerCollectionType);
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionType baseCollectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType contentValueHandlerCollectionType = (CollectionType) baseCollectionType.withContentValueHandler(new Object());
        assertNotNull(contentValueHandlerCollectionType);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType baseCollectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType staticCollectionType = (CollectionType) baseCollectionType.withStaticTyping();
        assertNotNull(staticCollectionType);
    }

    @Test
    public void testRefine() {
        CollectionType baseCollectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType refinedCollectionType = (CollectionType) baseCollectionType.refine(List.class, null, null, null);
        assertNotNull(refinedCollectionType);
    }
}