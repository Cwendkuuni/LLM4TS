import static org.junit.Assert.*;
import org.junit.Test;

public class MapTypeTest {
    
    // Test construct method
    @Test
    public void testConstruct() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
    }
    
    // Test _narrow method
    @Test
    public void testNarrow() {
        Class<?> subclass = MapType.class;
        MapType mapType = new MapType();
        JavaType narrowType = mapType._narrow(subclass);
        assertNotNull(narrowType);
    }
    
    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType withHandlerType = mapType.withTypeHandler(h);
        assertNotNull(withHandlerType);
    }
    
    // Test withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType withContentTypeHandlerType = mapType.withContentTypeHandler(h);
        assertNotNull(withContentTypeHandlerType);
    }
    
    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType withValueHandlerType = mapType.withValueHandler(h);
        assertNotNull(withValueHandlerType);
    }
    
    // Test withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType withContentValueHandlerType = mapType.withContentValueHandler(h);
        assertNotNull(withContentValueHandlerType);
    }
    
    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType();
        MapType withStaticTypingType = mapType.withStaticTyping();
        assertNotNull(withStaticTypingType);
    }
    
    // Test withContentType method
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        MapType mapType = new MapType();
        MapType withContentTypeType = mapType.withContentType(contentType);
        assertNotNull(withContentTypeType);
    }
    
    // Test withKeyType method
    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();
        MapType mapType = new MapType();
        MapType withKeyTypeType = mapType.withKeyType(keyType);
        assertNotNull(withKeyTypeType);
    }
    
    // Test refine method
    @Test
    public void testRefine() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        MapType mapType = new MapType();
        JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(refinedType);
    }
    
    // Test withKeyTypeHandler method
    @Test
    public void testWithKeyTypeHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType withKeyTypeHandlerType = mapType.withKeyTypeHandler(h);
        assertNotNull(withKeyTypeHandlerType);
    }
    
    // Test withKeyValueHandler method
    @Test
    public void testWithKeyValueHandler() {
        Object h = new Object();
        MapType mapType = new MapType();
        MapType withKeyValueHandlerType = mapType.withKeyValueHandler(h);
        assertNotNull(withKeyValueHandlerType);
    }
    
    // Test toString method
    @Test
    public void testToString() {
        MapType mapType = new MapType();
        String str = mapType.toString();
        assertNotNull(str);
    }
}