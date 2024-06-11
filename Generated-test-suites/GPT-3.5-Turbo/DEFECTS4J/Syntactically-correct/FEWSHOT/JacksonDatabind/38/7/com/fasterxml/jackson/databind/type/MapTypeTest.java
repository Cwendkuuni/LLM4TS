import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapTypeTest {
    
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        assertNotNull(mapType);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }
    
    @Test
    public void testConstructWithSuperClassAndSuperInterfaces() {
        JavaType superClass = TypeFactory.constructType(Object.class);
        ArrayType[] superInterfaces = {TypeFactory.constructType(List.class), TypeFactory.constructType(Set.class)};
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), superClass, superInterfaces, String.class, Integer.class);
        assertNotNull(mapType);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInterfaces, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        MapType staticMapType = mapType.withStaticTyping();
        assertNotNull(staticMapType);
        assertEquals(HashMap.class, staticMapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), staticMapType.getBindings());
        assertNull(staticMapType.getSuperClass());
        assertNull(staticMapType.getSuperInterfaces());
        assertTrue(staticMapType.isStatic());
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        JavaType newKeyType = TypeFactory.constructType(Object.class);
        MapType withKeyTypeMapType = mapType.withKeyType(newKeyType);
        assertNotNull(withKeyTypeMapType);
        assertEquals(HashMap.class, withKeyTypeMapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), withKeyTypeMapType.getBindings());
        assertNull(withKeyTypeMapType.getSuperClass());
        assertNull(withKeyTypeMapType.getSuperInterfaces());
        assertEquals(newKeyType, withKeyTypeMapType.getKeyType());
        assertEquals(Integer.class, withKeyTypeMapType.getValueType());
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        JavaType newContentType = TypeFactory.constructType(Long.class);
        MapType withContentTypeMapType = mapType.withContentType(newContentType);
        assertNotNull(withContentTypeMapType);
        assertEquals(HashMap.class, withContentTypeMapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), withContentTypeMapType.getBindings());
        assertNull(withContentTypeMapType.getSuperClass());
        assertNull(withContentTypeMapType.getSuperInterfaces());
        assertEquals(String.class, withContentTypeMapType.getKeyType());
        assertEquals(newContentType, withContentTypeMapType.getValueType());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        Object newKeyTypeHandler = new Object();
        MapType withKeyTypeHandlerMapType = mapType.withKeyTypeHandler(newKeyTypeHandler);
        assertNotNull(withKeyTypeHandlerMapType);
        assertEquals(HashMap.class, withKeyTypeHandlerMapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), withKeyTypeHandlerMapType.getBindings());
        assertNull(withKeyTypeHandlerMapType.getSuperClass());
        assertNull(withKeyTypeHandlerMapType.getSuperInterfaces());
        assertEquals(newKeyTypeHandler, withKeyTypeHandlerMapType.getKeyType().getTypeHandler());
        assertEquals(Integer.class, withKeyTypeHandlerMapType.getValueType());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        Object newKeyValueHandler = new Object();
        MapType withKeyValueHandlerMapType = mapType.withKeyValueHandler(newKeyValueHandler);
        assertNotNull(withKeyValueHandlerMapType);
        assertEquals(HashMap.class, withKeyValueHandlerMapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), withKeyValueHandlerMapType.getBindings());
        assertNull(withKeyValueHandlerMapType.getSuperClass());
        assertNull(withKeyValueHandlerMapType.getSuperInterfaces());
        assertEquals(String.class, withKeyValueHandlerMapType.getKeyType());
        assertEquals(newKeyValueHandler, withKeyValueHandlerMapType.getValueType().getValueHandler());
    }
    
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        String expectedToString = "[map type; class java.util.HashMap, java.lang.String -> java.lang.Integer]";
        assertEquals(expectedToString, mapType.toString());
    }
    
}