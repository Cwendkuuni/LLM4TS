import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        
        assertEquals(rawType, mapType.getClass());
        assertNull(mapType.getKeyType());
        assertNull(mapType.getValueType());
    }
    
    @Test
    public void testConstructWithKeyAndValue() {
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        
        assertEquals(rawType, mapType.getClass());
        assertNull(mapType.getKeyType());
        assertNull(mapType.getValueType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType narrowedMapType = mapType._narrow(subclass);
        
        assertEquals(subclass, narrowedMapType.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withTypeHandlerMapType = mapType.withTypeHandler(h);
        
        assertEquals(h, withTypeHandlerMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withContentTypeHandlerMapType = mapType.withContentTypeHandler(h);
        
        assertEquals(h, withContentTypeHandlerMapType.getValueType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withValueHandlerMapType = mapType.withValueHandler(h);
        
        assertEquals(h, withValueHandlerMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withContentValueHandlerMapType = mapType.withContentValueHandler(h);
        
        assertEquals(h, withContentValueHandlerMapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withStaticTypingMapType = mapType.withStaticTyping();
        
        assertEquals(true, withStaticTypingMapType.isStaticTyping());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withContentTypeMapType = mapType.withContentType(contentType);
        
        assertEquals(contentType, withContentTypeMapType.getValueType());
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withKeyTypeMapType = mapType.withKeyType(keyType);
        
        assertEquals(keyType, withKeyTypeMapType.getKeyType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType keyT = null;
        JavaType valueT = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType refinedMapType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertEquals(rawType, refinedMapType.getClass());
        assertNull(refinedMapType.getKeyType());
        assertNull(refinedMapType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withKeyTypeHandlerMapType = mapType.withKeyTypeHandler(h);
        
        assertEquals(h, withKeyTypeHandlerMapType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object h = null;
        
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withKeyValueHandlerMapType = mapType.withKeyValueHandler(h);
        
        assertEquals(h, withKeyValueHandlerMapType.getKeyType().getValueHandler());
    }

    @Test
    public void testToString() {
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        
        MapType mapType = new MapType(rawType, null, null, null, keyT, valueT, null, null, false);
        
        String expected = "[map type; class " + rawType.getName() + ", " + keyT + " -> " + valueT + "]";
        
        assertEquals(expected, mapType.toString());
    }
}