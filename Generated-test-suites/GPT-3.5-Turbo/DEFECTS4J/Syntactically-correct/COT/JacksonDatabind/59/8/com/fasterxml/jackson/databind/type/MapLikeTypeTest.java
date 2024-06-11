
import static org.junit.Assert.*;
import org.junit.Test;

public class MapLikeTypeTest {
    
    // Method: isTrueMapType()
    @Test
    public void testIsTrueMapType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapType.isTrueMapType());
        
        MapLikeType nonMapType = new MapLikeType(List.class, null, null, null, null, null, null, null, false);
        assertFalse(nonMapType.isTrueMapType());
    }
    
    // Method: getKeyType()
    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(String.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, null, null, null, false);
        assertEquals(keyType, mapType.getKeyType());
    }
    
    // Method: getContentType()
    @Test
    public void testGetContentType() {
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, valueType, null, null, false);
        assertEquals(valueType, mapType.getContentType());
    }
    
    // Method: getContentValueHandler()
    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType valueType = new JavaType(Integer.class, valueHandler);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, valueType, null, null, false);
        assertEquals(valueHandler, mapType.getContentValueHandler());
    }
    
    // Method: getContentTypeHandler()
    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType valueType = new JavaType(Integer.class, null, typeHandler);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, valueType, null, null, false);
        assertEquals(typeHandler, mapType.getContentTypeHandler());
    }
    
    // Method: hasHandlers()
    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, valueHandler, typeHandler, false);
        assertTrue(mapType.hasHandlers());
    }
    
    // Method: getErasedSignature()
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        StringBuilder result = mapType.getErasedSignature(sb);
        assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]", result.toString());
    }
    
    // Method: getGenericSignature()
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        StringBuilder result = mapType.getGenericSignature(sb);
        assertEquals("java.util.Map<Ljava.lang.String;Ljava.lang.Integer>;", result.toString());
    }
    
    // Method: buildCanonicalName()
    @Test
    public void testBuildCanonicalName() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        String result = mapType.buildCanonicalName();
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", result);
    }
    
    // Method: isContainerType()
    @Test
    public void testIsContainerType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapType.isContainerType());
    }
    
    // Method: isMapLikeType()
    @Test
    public void testIsMapLikeType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapType.isMapLikeType());
    }
    
    // Method: refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
    @Test
    public void testRefine() {
        Class<?> rawType = Map.class;
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        JavaType[] superInterfaces = {new JavaType(Serializable.class)};
        JavaType[] superInterfaces2 = {new JavaType(Cloneable.class)};
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType result = mapType.refine(rawType, null, null, superInterfaces);
        assertEquals(new MapLikeType(rawType, null, null, superInterfaces, keyType, valueType, null, null ,false), result);
        
        JavaType result2 = mapType.refine(rawType, null, null, superInterfaces2);
        assertEquals(new MapLikeType(rawType, null, null, superInterfaces2, keyType, valueType, null, null ,false), result2);
    }
    
    // Method: toString()
    @Test
    public void testToString() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        String result = mapType.toString();
        assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]", result);
    }
    
    // Method: equals(Object o)
    @Test
    public void testEquals() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        JavaType keyType2 = new JavaType(String.class);
        JavaType valueType2 = new JavaType(Integer.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, keyType2, valueType2, null, null, false);
        assertTrue(mapType.equals(mapType2));
    }
    
}
