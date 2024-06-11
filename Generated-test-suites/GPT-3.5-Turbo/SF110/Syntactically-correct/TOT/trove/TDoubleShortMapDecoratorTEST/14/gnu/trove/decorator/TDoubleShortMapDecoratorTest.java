import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    private TDoubleShortMap map;
    
    @Before
    public void setUp() {
        map = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }
    
    @Test
    public void testPutAndGet() {
        // Typical use case: Put and get a key-value pair
        Double key1 = 3.14;
        Short value1 = 42;
        mapDecorator.put(key1, value1);
        Short retrievedValue1 = mapDecorator.get(key1);
        assertEquals(value1, retrievedValue1);
        
        // Edge case: Put and get a negative key-value pair
        Double key2 = -2.5;
        Short value2 = -10;
        mapDecorator.put(key2, value2);
        Short retrievedValue2 = mapDecorator.get(key2);
        assertEquals(value2, retrievedValue2);
        
        // Error scenario: Attempt to put null key and value pair
        Double key3 = null;
        Short value3 = null;
        mapDecorator.put(key3, value3);
        Short retrievedValue3 = mapDecorator.get(key3);
        assertEquals(value3, retrievedValue3);
    }
    
    @Test
    public void testRemove() {
        // Typical use case: Remove an existing key-value pair
        Double key1 = 2.0;
        Short value1 = 5;
        mapDecorator.put(key1, value1);
        Short removedValue1 = mapDecorator.remove(key1);
        assertEquals(value1, removedValue1);
        assertNull(mapDecorator.get(key1));
        
        // Edge case: Attempt to remove a non-existing key-value pair
        Double key2 = 10.5;
        Short removedValue2 = mapDecorator.remove(key2);
        assertNull(removedValue2);
    }
    
    @Test
    public void testContainsKey() {
        // Typical use case: Check if a key exists in the map
        Double key1 = 1.5;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsKey(key1));
        
        // Edge case: Check if a non-existing key exists in the map
        Double key2 = -5.0;
        assertFalse(mapDecorator.containsKey(key2));
        
        // Error scenario: Check if null key exists in the map
        Double key3 = null;
        assertFalse(mapDecorator.containsKey(key3));
    }
    
    @Test
    public void testContainsValue() {
        // Typical use case: Check if a value exists in the map
        Double key1 = 0.5;
        Short value1 = 15;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsValue(value1));
        
        // Edge case: Check if a non-existing value exists in the map
        Short value2 = -20;
        assertFalse(mapDecorator.containsValue(value2));
    }
    
    @Test
    public void testSize() {
        // Typical use case: Add multiple key-value pairs and check the size
        Double key1 = 1.0;
        Short value1 = 20;
        mapDecorator.put(key1, value1);
        Double key2 = 2.0;
        Short value2 = 30;
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.size());
        
        // Edge case: Add and remove key-value pairs to check the size
        Double key3 = 3.0;
        Short value3 = 40;
        mapDecorator.put(key3, value3);
        mapDecorator.remove(key1);
        assertEquals(2, mapDecorator.size());
        
        // Error scenario: Add a null key-value pair and check the size
        Double key4 = null;
        Short value4 = null;
        mapDecorator.put(key4, value4);
        assertEquals(3, mapDecorator.size());
    }
    
    @Test
    public void testClear() {
        // Typical use case: Add key-value pairs and clear the map
        Double key1 = 1.0;
        Short value1 = 20;
        mapDecorator.put(key1, value1);
        Double key2 = 2.0;
        Short value2 = 30;
        mapDecorator.put(key2, value2);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        
        // Edge case: Clear an already empty map
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testPutAll() {
        // Typical use case: Put all key-value pairs from another map
        Map<Double, Short> otherMap = new HashMap<Double, Short>();
        otherMap.put(1.0, 10);
        otherMap.put(2.0, 20);
        mapDecorator.putAll(otherMap);
        assertEquals(2, mapDecorator.size());
        
        // Edge case: Put all key-value pairs from an empty map
        Map<Double, Short> emptyMap = new HashMap<Double, Short>();
        mapDecorator.putAll(emptyMap);
        assertEquals(2, mapDecorator.size());
        
        // Error scenario: Put all key-value pairs from a map with null key-value pair
        Map<Double, Short> nullMap = new HashMap<Double, Short>();
        nullMap.put(3.0, null);
        mapDecorator.putAll(nullMap);
        assertEquals(3, mapDecorator.size());
    }
    
    @Test
    public void testEntrySet() {
        // Typical use case: Get the entry set of the map
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        Double key2 = 2.0;
        Short value2 = 20;
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.entrySet().size());
        
        // Edge case: Get the entry set of an empty map
        mapDecorator.clear();
        assertEquals(0, mapDecorator.entrySet().size());
        
        // Error scenario: Add null key-value pair and check the entry set
        Double key3 = null;
        Short value3 = null;
        mapDecorator.put(key3, value3);
        assertEquals(1, mapDecorator.entrySet().size());
    }
}