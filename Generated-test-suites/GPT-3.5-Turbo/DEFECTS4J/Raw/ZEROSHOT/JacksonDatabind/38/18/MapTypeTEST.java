import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {
    
    // Test construct method
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(String.class, null, null, null, null, null);
        assertNotNull(mapType);
    }
    
    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withTypeHandler(handler);
        assertNotNull(newMapType);
        assertSame(handler, newMapType.getTypeHandler());
    }
    
    // Test withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withContentTypeHandler(handler);
        assertNotNull(newMapType);
        assertSame(handler, newMapType.getContentType().getTypeHandler());
    }
    
    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withValueHandler(handler);
        assertNotNull(newMapType);
        assertSame(handler, newMapType.getValueHandler());
    }
    
    // Test withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withContentValueHandler(handler);
        assertNotNull(newMapType);
        assertSame(handler, newMapType.getContentType().getValueHandler());
    }
    
    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withStaticTyping();
        assertNotNull(newMapType);
        assertTrue(newMapType.isStaticTyping());
    }
    
    // Test withContentType method
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withContentType(contentType);
        assertNotNull(newMapType);
        assertSame(contentType, newMapType.getContentType());
    }
    
    // Test withKeyType method
    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withKeyType(keyType);
        assertNotNull(newMapType);
        assertSame(keyType, newMapType.getKeyType());
    }
    
    // Test refine method
    @Test
    public void testRefine() {
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.refine(MapType.class, null, null, null);
        assertNotNull(newMapType);
        assertSame(MapType.class, newMapType.getClass());
    }
    
    // Test withKeyTypeHandler method
    @Test
    public void testWithKeyTypeHandler() {
        Object handler = new Object();
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withKeyTypeHandler(handler);
        assertNotNull(newMapType);
        assertSame(handler, newMapType.getKeyType().getTypeHandler());
    }
    
    // Test withKeyValueHandler method
    @Test
    public void testWithKeyValueHandler() {
        Object handler = new Object();
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withKeyValueHandler(handler);
        assertNotNull(newMapType);
        assertSame(handler, newMapType.getKeyType().getValueHandler());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        MapType mapType = new MapType(String.class, null, null, null, null, null, null, null, false);
        String string = mapType.toString();
        assertEquals("[map type; class java.lang.String, null -> null]", string);
    }
}