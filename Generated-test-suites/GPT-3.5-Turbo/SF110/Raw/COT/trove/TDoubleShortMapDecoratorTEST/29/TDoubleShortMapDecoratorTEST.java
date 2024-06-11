
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        
        // Basic test case
        map.put(key, value);
        assertEquals(value, map.get(key));
        
        // Test null key
        key = null;
        value = 2;
        map.put(key, value);
        assertEquals(value, map.get(key));
        
        // Test null value
        key = 3.0;
        value = null;
        map.put(key, value);
        assertEquals(value, map.get(key));
        
        // Test existing entry
        key = 1.0;
        value = 3;
        map.put(key, value);
        assertEquals(value, map.get(key));
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        
        // Basic test case
        map.put(key, value);
        assertEquals(value, map.get(key));
        
        // Test null key
        key = null;
        assertEquals(null, map.get(key));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        
        // Add an entry and clear
        map.put(key, value);
        map.clear();
        
        assertEquals(0, map.size());
        assertEquals(null, map.get(key));
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        
        // Add an entry and remove
        map.put(key, value);
        Short removedValue = map.remove(key);
        
        assertEquals(value, removedValue);
        assertEquals(null, map.get(key));
        
        // Test null key
        key = null;
        assertEquals(null, map.remove(key));
    }
    
    // Test entrySet()
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        
        // Basic test case
        map.put(key, value);
        assertTrue(map.containsValue(value));
        
        // Test with invalid value type
        assertFalse(map.containsValue("invalid"));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        
        // Basic test case
        map.put(key, value);
        assertTrue(map.containsKey(key));
        
        // Test with invalid key type
        assertFalse(map.containsKey("invalid"));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        
        // Add an entry
        map.put(key, value);
        assertEquals(1, map.size());
        
        // Clear the map
        map.clear();
        assertEquals(0, map.size());
    }
    
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertTrue(map.isEmpty());
        
        // Add an entry
        Double key = 1.0;
        Short value = 1;
        map.put(key, value);
        assertFalse(map.isEmpty());
        
        // Clear the map
        map.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        
        // Create a map with multiple entries
        Map<Double, Short> inputMap = new HashMap<>();
        inputMap.put(2.0, 2);
        inputMap.put(3.0, 3);
        
        // Put all entries into the map
        map.putAll(inputMap);
        
        // Verify the values are correct
        assertEquals(inputMap.get(2.0), map.get(2.0));
        assertEquals(inputMap.get(3.0), map.get(3.0));
    }
    
    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        double key = 1.0;
        
        assertEquals(key, map.wrapKey(key), 0.01);
    }
    
    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        
        assertEquals(key, map.unwrapKey(key), 0.01);
    }
    
    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        short value = 1;
        
        assertEquals(value, map.wrapValue(value), 0.01);
    }
    
    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Short value = 1;
        
        assertEquals(value, map.unwrapValue(value), 0.01);
    }
    
    // Test readExternal() and writeExternal()
    
}
