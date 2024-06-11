import org.junit.*;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
        boolean result = mapLikeType.isContainerType();
        assertTrue(result);
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
        boolean result = mapLikeType.isMapLikeType();
        assertTrue(result);
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(null, null, null, null, null);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, null, null, null, false);
        JavaType result = mapLikeType.getKeyType();
        assertEquals(keyType, result);
    }

    @Test
    public void testGetContentType() {
        JavaType valueType = new JavaType(null, null, null, null, null);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, valueType, null, null, false);
        JavaType result = mapLikeType.getContentType();
        assertEquals(valueType, result);
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType valueType = new JavaType(null, null, null, null, null);
        valueType.setValueHandler(valueHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, valueType, null, null, false);
        Object result = mapLikeType.getContentValueHandler();
        assertEquals(valueHandler, result);
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType valueType = new JavaType(null, null, null, null, null);
        valueType.setTypeHandler(typeHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, valueType, null, null, false);
        Object result = mapLikeType.getContentTypeHandler();
        assertEquals(typeHandler, result);
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(null, null, null, null, null);
        JavaType valueType = new JavaType(null, null, null, null, null);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType, null, null, false);
        boolean result = mapLikeType.hasHandlers();
        assertFalse(result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        Class<?> testClass = MapLikeType.class;
        MapLikeType mapLikeType = new MapLikeType(testClass, null, null, null, null, null, null, null, false);
        StringBuilder result = mapLikeType.getErasedSignature(sb);
        assertEquals(testClass.getName(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType keyType = new JavaType(null, null, null, null, null);
        JavaType valueType = new JavaType(null, null, null, null, null);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType, null, null, false);
        StringBuilder result = mapLikeType.getGenericSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> testClass = MapLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(null, null, null, null, null);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = new JavaType(null, null, null, null, null);
        JavaType valueType = new JavaType(null, null, null, null, null);
        MapLikeType mapLikeType = new MapLikeType(testClass, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
        JavaType result = mapLikeType.refine(testClass, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        Class<?> testClass = MapLikeType.class;
        JavaType keyType = new JavaType(null, null, null, null, null);
        JavaType valueType = new JavaType(null, null, null, null, null);
        MapLikeType mapLikeType = new MapLikeType(testClass, null, null, null, keyType, valueType, null, null, false);
        String result = mapLikeType.toString();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        JavaType keyType = new JavaType(null, null, null, null, null);
        JavaType valueType = new JavaType(null, null, null, null, null);
        MapLikeType mapLikeType1 = new MapLikeType(null, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(null, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType1.equals(mapLikeType2));
    }
}