import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());

        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType2.isTrueMapType());

        MapLikeType mapLikeType3 = new MapLikeType(String.class, null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType3.isTrueMapType());
    }

    @Test
    public void testKeyType() {
        JavaType keyType = new JavaType(String.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, null, null, null, false);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testContentType() {
        JavaType contentType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, contentType, null, null, false);
        assertEquals(contentType, mapLikeType.getContentType());
    }

    @Test
    public void testContentValueHandler() {
        Object valueHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        mapLikeType = mapLikeType.withValueHandler(valueHandler);
        assertEquals(valueHandler, mapLikeType.getContentValueHandler());
    }

    @Test
    public void testContentTypeHandler() {
        Object typeHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        mapLikeType = mapLikeType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType keyType = new JavaType(String.class);
        JavaType contentType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, contentType, valueHandler,
                typeHandler, false);
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testErasedSignature() {
        StringBuilder sb = new StringBuilder();
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        mapLikeType.getErasedSignature(sb);
        assertEquals("java.util.HashMap", sb.toString());
    }

    @Test
    public void testGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType keyType = new JavaType(String.class);
        JavaType contentType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, contentType, null, null,
                false);
        mapLikeType.getGenericSignature(sb);
        assertEquals("java.util.HashMap<java.lang.String,java.lang.Integer>;", sb.toString());
    }

    @Test
    public void testRefine() {
        JavaType keyType = new JavaType(String.class);
        JavaType contentType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, contentType, null, null,
                false);
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refinedType = mapLikeType.refine(Map.class, null, superClass, superInterfaces);
        assertEquals(Map.class, refinedType.getRawClass());
    }

    @Test
    public void testToString() {
        JavaType keyType = new JavaType(String.class);
        JavaType contentType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, contentType, null, null,
                false);
        assertEquals("[map-like type; class java.util.HashMap, java.lang.String -> java.lang.Integer]",
                mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        JavaType keyType = new JavaType(String.class);
        JavaType contentType = new JavaType(Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(HashMap.class, null, null, null, keyType, contentType, null, null,
                false);
        MapLikeType mapLikeType2 = new MapLikeType(HashMap.class, null, null, null, keyType, contentType, null, null,
                false);
        assertTrue(mapLikeType1.equals(mapLikeType2));
    }
}