
import org.junit.Test;
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
        assertNull(mapLikeType.getKeyType());
    }
    
    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNull(mapLikeType.getContentType());
    }
    
    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNull(mapLikeType.getContentValueHandler());
    }
    
    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNull(mapLikeType.getContentTypeHandler());
    }
    
    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType();
        assertFalse(mapLikeType.hasHandlers());
    }
    
    @Test
    public void testGetErasedSignature() {
        MapLikeType mapLikeType = new MapLikeType();
        assertEquals("Lcom/fasterxml/jackson/databind/type/JavaType;", mapLikeType.getErasedSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        MapLikeType mapLikeType = new MapLikeType();
        assertEquals("Lcom/fasterxml/jackson/databind/type/JavaType;\\<Ljava/lang/reflect/TypeVariable;*\\<Ljava/lang/String;*\\>;Ljava/lang/reflect/Type;*\\<Ljava/lang/Integer;*\\>;Lcom/fasterxml/jackson/databind/type/JavaType;>;", mapLikeType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.withKeyTypeHandler(null));
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNotNull(mapLikeType.withKeyValueHandler(null));
    }
    
    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType();
        assertFalse(mapLikeType.isTrueMapType());
    }
    
    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType();
        assertEquals("[map-like type; class null, null -> null]", mapLikeType.toString());
    }
    
    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType();
        MapLikeType mapLikeType2 = new MapLikeType();
        assertTrue(mapLikeType1.equals(mapLikeType2));
    }
}
