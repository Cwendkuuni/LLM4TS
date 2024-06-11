
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
        assertNotNull(mapType);
        // Add assertions to check mapType construction
    }
    
    @Test
    public void testDeprecatedConstruct() {
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        assertNotNull(mapType);
        // Add assertions to check mapType construction
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = MapType.class;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType narrowedType = mapType._narrow(subclass);
        assertNotNull(narrowedType);
        // Add assertions to check narrowedType
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withTypeHandler = mapType.withTypeHandler(h);
        assertNotNull(withTypeHandler);
        // Add assertions to check withTypeHandler
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withContentTypeHandler = mapType.withContentTypeHandler(h);
        assertNotNull(withContentTypeHandler);
        // Add assertions to check withContentTypeHandler
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withValueHandler = mapType.withValueHandler(h);
        assertNotNull(withValueHandler);
        // Add assertions to check withValueHandler
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withContentValueHandler = mapType.withContentValueHandler(h);
        assertNotNull(withContentValueHandler);
        // Add assertions to check withContentValueHandler
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withStaticTyping = mapType.withStaticTyping();
        assertNotNull(withStaticTyping);
        // Add assertions to check withStaticTyping
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withContentType = mapType.withContentType(contentType);
        assertNotNull(withContentType);
        // Add assertions to check withContentType
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withKeyType = mapType.withKeyType(keyType);
        assertNotNull(withKeyType);
        // Add assertions to check withKeyType
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
        JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(refinedType);
        // Add assertions to check refinedType
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(h);
        assertNotNull(withKeyTypeHandler);
        // Add assertions to check withKeyTypeHandler
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType withKeyValueHandler = mapType.withKeyValueHandler(h);
        assertNotNull(withKeyValueHandler);
        // Add assertions to check withKeyValueHandler
    }
    
    @Test
    public void testToString() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        String str = mapType.toString();
        assertNotNull(str);
        // Add assertions to check str
    }
}

