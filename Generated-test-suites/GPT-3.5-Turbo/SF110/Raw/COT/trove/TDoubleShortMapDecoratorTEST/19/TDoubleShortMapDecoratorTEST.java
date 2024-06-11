
import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        
        // Test put() method
        Short result1 = mapDecorator.put(key1, value1);
        assertNull(result1); // Should return null because there was no previous value
        assertTrue(mapDecorator.containsKey(key1)); // Check if the key was added
        
        // Test get() method
        Short result2 = mapDecorator.get(key1);
        assertEquals(value1, result2); // Check if the value is correct
        
        // Test put() method with null arguments
        Double key2 = null;
        Short value2 = null;
        Short result3 = mapDecorator.put(key2, value2);
        assertNull(result3); // Should return null because there was no previous value
        assertTrue(mapDecorator.containsKey(key2)); // Check if the key was added
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        
        mapDecorator.put(key1, value1);
        assertFalse(mapDecorator.isEmpty()); // Check if the map is not empty after adding a key-value pair
        
        // Test clear() method
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty()); // Check if the map is empty after calling clear() method
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsKey(key1)); // Check if the key is present before removing
        
        // Test remove() method
        Short result1 = mapDecorator.remove(key1);
        assertEquals(value1, result1); // Check if the removed value is correct
        assertFalse(mapDecorator.containsKey(key1)); // Check if the key is no longer present
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        
        mapDecorator.put(key1, value1);
        
        // Test entrySet() method
        assertEquals(1, mapDecorator.entrySet().size()); // Check if the entry set size is correct
        assertTrue(mapDecorator.entrySet().contains(new AbstractMap.SimpleEntry<Double, Short>(key1, value1))); // Check if the key-value pair is present in the entry set
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        
        mapDecorator.put(key1, value1);
        
        // Test containsValue() method
        assertTrue(mapDecorator.containsValue(value1)); // Check if the value is present in the map
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        
        mapDecorator.put(key1, value1);
        
        // Test containsKey() method
        assertTrue(mapDecorator.containsKey(key1)); // Check if the key is present in the map
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        
        mapDecorator.put(key1, value1);
        
        // Test size() method
        assertEquals(1, mapDecorator.size()); // Check if the size is correct after adding a key-value pair
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        Double key1 = 1.0;
        Short value1 = 10;
        map.put(key1, value1);
        
        // Test putAll() method
        mapDecorator.putAll(map);
        assertTrue(mapDecorator.containsKey(key1)); // Check if the key-value pair was added
    }

}
