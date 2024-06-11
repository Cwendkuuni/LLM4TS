import static org.junit.Assert.*;
import org.junit.Test;

public class MapTypeTest {
    
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        assertNotNull(mapType);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(new SimpleType(String.class), mapType.getKeyType());
        assertEquals(new SimpleType(Integer.class), mapType.getValueType());
    }
    
    @Test
    public void testWithTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        Object typeHandler = new Object();
        MapType newMapType = mapType.withTypeHandler(typeHandler);
        assertNotNull(newMapType);
        assertEquals(typeHandler, newMapType.getTypeHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        MapType staticMapType = mapType.withStaticTyping();
        assertNotNull(staticMapType);
        assertTrue(staticMapType.isStaticTyping());
    }

    // Additional test cases for edge cases and error scenarios can be added here
    
}