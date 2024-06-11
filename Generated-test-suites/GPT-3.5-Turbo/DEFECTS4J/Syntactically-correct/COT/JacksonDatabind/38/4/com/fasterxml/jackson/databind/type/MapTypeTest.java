import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTypeTest {
    
    private MapType mapType;
    
    @Before
    public void setUp() {
        mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
    }

    @Test
    public void testConstruct() {
        assertNotNull(mapType);
    }
    
    @Test
    public void testWithTypeHandler() {
        MapType updatedType = mapType.withTypeHandler(Object.class);
        assertEquals(Object.class, updatedType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        MapType updatedType = mapType.withContentTypeHandler(Object.class);
        assertEquals(Object.class, updatedType.getContentTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        MapType updatedType = mapType.withValueHandler(Object.class);
        assertEquals(Object.class, updatedType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        MapType updatedType = mapType.withContentValueHandler(Object.class);
        assertEquals(Object.class, updatedType.getContentValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType updatedType = mapType.withStaticTyping();
        assertTrue(updatedType.isStaticTyping());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = JavaType.forType(Long.class);
        MapType updatedType = mapType.withContentType(contentType);
        assertEquals(contentType, updatedType.getContentType());
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = JavaType.forType(Double.class);
        MapType updatedType = mapType.withKeyType(keyType);
        assertEquals(keyType, updatedType.getKeyType());
    }
    
    @Test
    public void testRefine() {
        MapType updatedType = mapType.refine(Map.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(Map.class, updatedType.getRawClass());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType updatedType = mapType.withKeyTypeHandler(Object.class);
        assertEquals(Object.class, updatedType.getKeyType().getTypeHandler());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType updatedType = mapType.withKeyValueHandler(Object.class);
        assertEquals(Object.class, updatedType.getKeyType().getValueHandler());
    }
    
    @Test
    public void testToString() {
        String expected = "[map type; class java.util.Map, class java.lang.String -> class java.lang.Integer]";
        assertEquals(expected, mapType.toString());
    }
}