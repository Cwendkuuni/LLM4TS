
import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
        
        MapLikeType notTrueMapType = new MapLikeType(ArrayList.class, null, null, null, null, null, null, null, false);
        assertFalse(notTrueMapType.isTrueMapType());
    }

    @Test
    public void testHasKeyType() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertNull(mapLikeType.withKeyType(null));
        
        JavaType keyType = new JavaType(String.class);
        MapLikeType mapLikeTypeWithKeyType = mapLikeType.withKeyType(keyType);
        assertEquals(keyType, mapLikeTypeWithKeyType.getKeyType());
    }

    @Test
    public void testHasContentType() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertNull(mapLikeType.withContentType(null));
        
        JavaType contentType = new JavaType(Integer.class);
        MapLikeType mapLikeTypeWithContentType = mapLikeType.withContentType(contentType);
        assertEquals(contentType, mapLikeTypeWithContentType.getContentType());
    }

    @Test
    public void testHasTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertNull(mapLikeType.withTypeHandler(null));
        
        Object typeHandler = new Object();
        MapLikeType mapLikeTypeWithTypeHandler = mapLikeType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, mapLikeTypeWithTypeHandler.getTypeHandler());
        
        Object contentTypeHandler = new Object();
        MapLikeType mapLikeTypeWithContentTypeHandler = mapLikeType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, mapLikeTypeWithContentTypeHandler.getContentTypeHandler());
        
        Object valueHandler = new Object();
        MapLikeType mapLikeTypeWithValueHandler = mapLikeType.withValueHandler(valueHandler);
        assertEquals(valueHandler, mapLikeTypeWithValueHandler.getValueHandler());
        
        Object contentValueHandler = new Object();
        MapLikeType mapLikeTypeWithContentValueHandler = mapLikeType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, mapLikeTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(String.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, null, null, null, false);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, contentType, null, null, false);
        assertEquals(contentType, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, valueHandler, null, false);
        assertEquals(valueHandler, mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, typeHandler, false);
        assertEquals(typeHandler, mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, new Object(), null, false);
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        StringBuilder sb = mapLikeType.getErasedSignature(new StringBuilder());
        assertNotNull(sb);
    }

    @Test
    public void testGetGenericSignature() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        StringBuilder sb = mapLikeType.getGenericSignature(new StringBuilder());
        assertNotNull(sb);
    }

    @Test
    public void testRefine() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        JavaType refinedType = new JavaType(TreeMap.class);
        JavaType[] superInterfaces = new JavaType[]{refinedType};
        
        JavaType refinedMapLikeType = mapLikeType.refine(HashMap.class, null, null, superInterfaces);
        assertEquals(HashMap.class, refinedMapLikeType.getRawClass());
        assertEquals(1, refinedMapLikeType.getInterfaces().length);
        assertEquals(refinedType, refinedMapLikeType.getInterfaces()[0]);
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.equals(mapLikeType));
        
        MapLikeType sameMapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.equals(sameMapLikeType));
        
        assertFalse(mapLikeType.equals(new Object()));
    }
}

