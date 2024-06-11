import static org.junit.Assert.*;
import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testKeyType() {
        MapLikeType mapType = new MapLikeType();
        JavaType keyType = mapType.getKeyType();
        assertNotNull(keyType);
        assertEquals(JavaType.class, keyType.getClass());
    }

    @Test
    public void testContentType() {
        MapLikeType mapType = new MapLikeType();
        JavaType contentType = mapType.getContentType();
        assertNotNull(contentType);
        assertEquals(JavaType.class, contentType.getClass());
    }

    @Test
    public void testContentValueHandler() {
        MapLikeType mapType = new MapLikeType();
        Object contentValueHandler = mapType.getContentValueHandler();
        assertNull(contentValueHandler);
    }

    @Test
    public void testContentTypeHandler() {
        MapLikeType mapType = new MapLikeType();
        Object contentTypeHandler = mapType.getContentTypeHandler();
        assertNull(contentTypeHandler);
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapType = new MapLikeType();
        boolean hasHandlers = mapType.hasHandlers();
        assertFalse(hasHandlers);
    }

    @Test
    public void testErasedSignature() {
        MapLikeType mapType = new MapLikeType();
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = mapType.getErasedSignature(sb);
        assertNotNull(erasedSignature);
        assertEquals("Ljava/util/Map<*>;", erasedSignature.toString());
    }

    @Test
    public void testGenericSignature() {
        MapLikeType mapType = new MapLikeType();
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = mapType.getGenericSignature(sb);
        assertNotNull(genericSignature);
        assertEquals("Ljava/util/Map<*>;", genericSignature.toString());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapType = new MapLikeType();
        boolean isTrueMapType = mapType.isTrueMapType();
        assertTrue(isTrueMapType);
    }

    @Test
    public void testEquals() {
        MapLikeType mapType1 = new MapLikeType();
        MapLikeType mapType2 = new MapLikeType();
        boolean areEqual = mapType1.equals(mapType2);
        assertTrue(areEqual);
    }

}