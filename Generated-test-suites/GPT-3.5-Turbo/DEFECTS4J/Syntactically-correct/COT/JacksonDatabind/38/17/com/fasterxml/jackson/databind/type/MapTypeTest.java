import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class MapTypeTest {
    
    private MapType mapType;
    
    @Before
    public void setUp() {
        mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
    }
    
    @Test
    public void testConstruct() {
        MapType result = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        assertNotNull(result);
        assertEquals(result.getRawClass(), Map.class);
        assertEquals(result.getKeyType(), String.class);
        assertEquals(result.getValueType(), Integer.class);
    }
    
    @Test
    public void testNarrow() {
        MapType result = mapType._narrow(HashMap.class);
        assertNotNull(result);
        assertEquals(result.getRawClass(), HashMap.class);
    }
    
    @Test
    public void testWithTypeHandler() {
        MapType result = mapType.withTypeHandler("handler");
        assertNotNull(result);
        assertEquals(result.getTypeHandler(), "handler");
    }
    
    @Test
    public void testWithContentTypeHandler() {
        MapType result = mapType.withContentTypeHandler("handler");
        assertNotNull(result);
        assertEquals(result.getValueType().getTypeHandler(), "handler");
    }
    
    @Test
    public void testWithValueHandler() {
        MapType result = mapType.withValueHandler("handler");
        assertNotNull(result);
        assertEquals(result.getValueHandler(), "handler");
    }
    
    @Test
    public void testWithContentValueHandler() {
        MapType result = mapType.withContentValueHandler("handler");
        assertNotNull(result);
        assertEquals(result.getValueType().getValueHandler(), "handler");
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType result = mapType.withStaticTyping();
        assertTrue(result.isStatic());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(Object.class);
        MapType result = mapType.withContentType(contentType);
        assertNotNull(result);
        assertEquals(result.getValueType(), contentType);
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType(Object.class);
        MapType result = mapType.withKeyType(keyType);
        assertNotNull(result);
        assertEquals(result.getKeyType(), keyType);
    }
    
    @Test
    public void testRefine() {
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[] { new JavaType(Runnable.class) };
        MapType result = mapType.refine(HashMap.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertNotNull(result);
        assertEquals(result.getRawClass(), HashMap.class);
        assertEquals(result.getSuperClass(), superClass);
        assertEquals(result.getInterfaces(), superInterfaces);
    }
    
    @Test
    public void testWithTypeHandlerForKeyType() {
        MapType result = mapType.withKeyTypeHandler("handler");
        assertNotNull(result);
        assertEquals(result.getKeyType().getTypeHandler(), "handler");
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType result = mapType.withKeyValueHandler("handler");
        assertNotNull(result);
        assertEquals(result.getKeyType().getValueHandler(), "handler");
    }
    
    @Test
    public void testToString() {
        String result = mapType.toString();
        assertEquals(result, "[map type; class java.util.Map, class java.lang.String -> class java.lang.Integer]");
    }
}