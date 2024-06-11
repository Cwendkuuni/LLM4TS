import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;



public class MapTypeTest {
    
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        assertEquals("Map type should have correct key type", String.class, mapType.getKeyType());
        assertEquals("Map type should have correct value type", Integer.class, mapType.getValueType());
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType newMapType = mapType.withKeyType(Long.class);
        assertNotEquals("Map type should have different key type", mapType.getKeyType(), newMapType.getKeyType());
        assertEquals("Map type should have correct value type", Integer.class, newMapType.getValueType());
    }
    
    @Test
    public void testWithValueType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        MapType newMapType = mapType.withValueType(Boolean.class);
        assertEquals("Map type should have correct key type", String.class, newMapType.getKeyType());
        assertNotEquals("Map type should have different value type", mapType.getValueType(), newMapType.getValueType());
    }
    
    // Add more test cases here for other methods in the MapType class
    
}

