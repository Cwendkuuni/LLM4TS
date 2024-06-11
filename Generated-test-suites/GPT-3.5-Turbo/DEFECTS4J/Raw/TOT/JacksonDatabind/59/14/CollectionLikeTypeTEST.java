import org.junit.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    CollectionLikeType collectionLikeType;

    @Before
    public void setUp() {
        JavaType elementType = JavaType.INT;
        collectionLikeType = CollectionLikeType.construct(Collection.class, elementType);
    }

    @Test
    public void testConstructor() {
        assertSame(Collection.class, collectionLikeType.getRawClass());
        assertEquals(JavaType.INT, collectionLikeType.getContentType());
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsContainerType() {
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        assertEquals(JavaType.INT, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getErasedSignature(sb);
        assertEquals("java.util.Collection", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getGenericSignature(sb);
        assertEquals("java.util.Collection<int>;", sb.toString());
    }

    @Test
    public void testEquals() {
        CollectionLikeType sameType = CollectionLikeType.construct(Collection.class, JavaType.INT);
        CollectionLikeType differentType = CollectionLikeType.construct(List.class, JavaType.INT);
        assertTrue(collectionLikeType.equals(collectionLikeType));
        assertTrue(collectionLikeType.equals(sameType));
        assertFalse(collectionLikeType.equals(null));
        assertFalse(collectionLikeType.equals(differentType));
    }

    @Test
    public void testToString() {
        String expected = "[collection-like type; class java.util.Collection, contains int]";
        assertEquals(expected, collectionLikeType.toString());
    }

}