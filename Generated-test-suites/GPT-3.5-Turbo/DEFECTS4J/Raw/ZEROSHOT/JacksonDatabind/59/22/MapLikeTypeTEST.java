import org.junit.*;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, null, null, null, false);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, contentType, null, null, false);
        assertEquals(contentType, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object contentValueHandler = new Object();
        JavaType contentType = new JavaType();
        contentType.setValueHandler(contentValueHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, contentType, null, null, false);
        assertEquals(contentValueHandler, mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object contentTypeHandler = new Object();
        JavaType contentType = new JavaType();
        contentType.setTypeHandler(contentTypeHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, contentType, null, null, false);
        assertEquals(contentTypeHandler, mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        keyType.setValueHandler(valueHandler);
        valueType.setTypeHandler(typeHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, valueType, null, null, false);
        MapLikeType staticType = mapLikeType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        JavaType[] superInterfaces = new JavaType[2];
        MapLikeType mapLikeType = new MapLikeType(null, null, null, superInterfaces, keyType, valueType, null, null, false);
        JavaType refinedType = mapLikeType.refine(null, null, null, superInterfaces);
        assertEquals(mapLikeType, refinedType);
    }

    @Test
    public void testToString() {
        Class<?> mapClass = Map.class;
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(mapClass, null, null, null, keyType, valueType, null, null, false);
        String expected = "[map-like type; class " + mapClass.getName() + ", " + keyType + " -> " + valueType + "]";
        assertEquals(expected, mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> mapClass = Map.class;
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType1 = new MapLikeType(mapClass, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(mapClass, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType1.equals(mapLikeType2));
    }
}