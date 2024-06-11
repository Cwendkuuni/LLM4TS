import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
        
        MapLikeType mapLikeType2 = new MapLikeType(String.class, null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType2.isTrueMapType());
    }
    
    @Test
    public void testKeyType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(keyType, mapLikeType.getKeyType());
    }
    
    @Test
    public void testContentType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(valueType, mapLikeType.getContentType());
    }
    
    @Test
    public void testContentValueHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Object valueHandler = new Object();
        mapLikeType.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, mapLikeType.getContentValueHandler());
    }
    
    @Test
    public void testContentTypeHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Object typeHandler = new Object();
        mapLikeType.withContentTypeHandler(typeHandler);
        assertEquals(typeHandler, mapLikeType.getContentTypeHandler());
    }
    
    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Object valueHandler = new Object();
        mapLikeType.withValueHandler(valueHandler);
        assertTrue(mapLikeType.hasHandlers());
    }
    
    @Test
    public void testErasedSignature() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        StringBuilder sb = new StringBuilder();
        mapLikeType.getErasedSignature(sb);
        assertEquals("java.util.Map<>", sb.toString());
    }
    
    @Test
    public void testGenericSignature() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        StringBuilder sb = new StringBuilder();
        mapLikeType.getGenericSignature(sb);
        assertEquals("java.util.Map<<T, S>>;", sb.toString());
    }
}