import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[2];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        MapType mapType = MapType.construct(MapType.class, bindings, superClass, superInts, keyT, valueT);
        
        assertNotNull(mapType);
        assertEquals(MapType.class, mapType.getClass());
    }
    
    @Test
    public void testConstruct_withNulls() {
        MapType mapType = MapType.construct(MapType.class, null, null);
        
        assertNotNull(mapType);
        assertEquals(MapType.class, mapType.getClass());
    }
    
    @Test
    public void testWithKeyHandler() {
        Object h = new Object();
        
        MapType mapType = new MapType();
        MapType result = mapType.withKeyTypeHandler(h);
        
        assertNotNull(result);
        assertEquals(mapType, result);
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object h = new Object();
        
        MapType mapType = new MapType();
        MapType result = mapType.withKeyValueHandler(h);
        
        assertNotNull(result);
        assertEquals(mapType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType();
        MapType result = mapType.withStaticTyping();
        
        assertNotNull(result);
        assertEquals(mapType, result);
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        
        MapType mapType = new MapType();
        MapType result = mapType.withContentType(contentType);
        
        assertNotNull(result);
        assertEquals(mapType, result);
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();
        
        MapType mapType = new MapType();
        MapType result = mapType.withKeyType(keyType);
        
        assertNotNull(result);
        assertEquals(mapType, result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[2];
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        MapType mapType = new MapType();
        JavaType result = mapType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNotNull(result);
        assertEquals(mapType, result);
    }
    
    @Test
    public void testToString() {
        MapType mapType = new MapType();
        String result = mapType.toString();
        
        assertNotNull(result);
    }
}