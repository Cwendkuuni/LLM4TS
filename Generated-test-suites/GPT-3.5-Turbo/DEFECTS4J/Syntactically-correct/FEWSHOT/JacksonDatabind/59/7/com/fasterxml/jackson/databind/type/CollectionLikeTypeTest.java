import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionLikeTypeTest {
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.List.class, SimpleType.construct(String.class));
        assertTrue(listType.isTrueCollectionType());

        CollectionLikeType mapType = CollectionLikeType.construct(java.util.Map.class, SimpleType.construct(String.class));
        assertFalse(mapType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.List.class, SimpleType.construct(String.class));
        assertEquals(SimpleType.construct(String.class), listType.getContentType());

        CollectionLikeType mapType = CollectionLikeType.construct(java.util.Map.class, SimpleType.construct(String.class));
        assertEquals(SimpleType.construct(String.class), mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.List.class, SimpleType.construct(String.class));
        assertNull(listType.getContentValueHandler());

        CollectionLikeType mapType = CollectionLikeType.construct(java.util.Map.class, SimpleType.construct(String.class));
        assertNull(mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.List.class, SimpleType.construct(String.class));
        assertNull(listType.getContentTypeHandler());

        CollectionLikeType mapType = CollectionLikeType.construct(java.util.Map.class, SimpleType.construct(String.class));
        assertNull(mapType.getContentTypeHandler());
    }
}