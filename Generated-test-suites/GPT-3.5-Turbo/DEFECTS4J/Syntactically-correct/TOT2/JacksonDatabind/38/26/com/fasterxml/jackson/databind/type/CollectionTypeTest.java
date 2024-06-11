import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), new JavaType(), new JavaType[]{new JavaType()}, new JavaType());
        assertNotNull(collectionType);
    }

    @Test
    public void testWithContentType() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), new JavaType(), new JavaType[]{new JavaType()}, new JavaType());
        JavaType newContentType = new JavaType();
        CollectionType updatedType = collectionType.withContentType(newContentType);
        assertEquals(newContentType, updatedType.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), new JavaType(), new JavaType[]{new JavaType()}, new JavaType());
        Object handler = new Object();
        CollectionType updatedType = collectionType.withContentValueHandler(handler);
        assertEquals(handler, updatedType.getContentValueHandler());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), new JavaType(), new JavaType[]{new JavaType()}, new JavaType());
        assertEquals("[collection type; class java.util.List, contains JavaType]", collectionType.toString());
    }
}