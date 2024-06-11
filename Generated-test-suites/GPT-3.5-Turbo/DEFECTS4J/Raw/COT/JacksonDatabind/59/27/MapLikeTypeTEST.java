import org.junit.*;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType();
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType();
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType();
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testGetGenericSignature() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.withKeyTypeHandler(new Object()));
    }

    @Test
    public void testWithKeyValueHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.withKeyValueHandler(new Object()));
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType();
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType();
        MapLikeType mapLikeType2 = new MapLikeType();
        assertTrue(mapLikeType1.equals(mapLikeType2));
    }
}