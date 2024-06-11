
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {
    
    private MapType mapType;

    @Before
    public void setUp() {
        mapType = MapType.construct(HashMap.class, String.class, Integer.class);
    }
    
    @Test
    public void testConstruct() {
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
    }
    
    @Test
    public void testWithKeyType() {
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Long.class);
        MapType newMapType = mapType.withKeyType(newKeyType);

        assertEquals(Long.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
    }
    
    @Test
    public void testWithValueType() {
        JavaType newValueType = TypeFactory.defaultInstance().constructType(Double.class);
        MapType newMapType = mapType.withContentType(newValueType);

        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Double.class, newMapType.getValueType());
    }
    
    // Test edge cases and exception handling scenarios
    
    @Test
    public void testWithNullKeyType() {
        try {
            MapType newMapType = mapType.withKeyType(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testWithNullValueType() {
        try {
            MapType newMapType = mapType.withContentType(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType newMapType = mapType.withStaticTyping();

        assertTrue(newMapType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        MapType newMapType = mapType.refine(LinkedHashMap.class, null, null, null);

        assertEquals(LinkedHashMap.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
    }
    
    @Test
    public void testToString() {
        String toString = mapType.toString();

        assertEquals("[map type; class java.util.HashMap, class java.lang.String -> class java.lang.Integer]", toString);
    }
}
