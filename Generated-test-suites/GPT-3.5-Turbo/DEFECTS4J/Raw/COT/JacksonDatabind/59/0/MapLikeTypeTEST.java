
import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, true);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, true);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        assertEquals("java.lang.String", mapType.getKeyType().toCanonical());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        assertEquals("java.lang.Integer", mapType.getContentType().toCanonical());
    }
    
    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        assertNull(mapType.getContentValueHandler());
    }
    
    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        assertNull(mapType.getContentTypeHandler());
    }
    
    @Test
    public void testHasHandlers() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testWithKeyType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        MapLikeType withKeyType = mapType.withKeyType(Long.class);
        assertEquals("java.lang.Long", withKeyType.getKeyType().toCanonical());
    }

    @Test
    public void testWithContentType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        MapLikeType withContentType = mapType.withContentType(Float.class);
        assertEquals("java.lang.Float", withContentType.getContentType().toCanonical());
    }

    @Test
    public void testWithContentValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        MapLikeType withContentValueHandler = mapType.withContentValueHandler(new Object());
        assertNull(withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        MapLikeType withContentTypeHandler = mapType.withContentTypeHandler(new Object());
        assertNull(withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        MapLikeType withStaticTyping = mapType.withStaticTyping();
        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testEquals() {
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, true);
        assertTrue(mapType1.equals(mapType2));
    }
}
