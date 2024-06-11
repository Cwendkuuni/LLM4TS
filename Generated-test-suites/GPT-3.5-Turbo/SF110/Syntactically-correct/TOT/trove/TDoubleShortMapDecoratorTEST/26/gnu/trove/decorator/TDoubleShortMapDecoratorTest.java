import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }
    
    @Test
    public void testPutAndGet() {
        Double key = 1.0;
        Short value = 10;
        
        mapDecorator.put(key, value);
        
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testPutAndGet_NullKey() {
        Double key = null;
        Short value = 10;
        
        mapDecorator.put(key, value);
        
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testPutAndGet_NullValue() {
        Double key = 1.0;
        Short value = null;
        
        mapDecorator.put(key, value);
        
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testPutAndGet_NullKeyAndValue() {
        Double key = null;
        Short value = null;
        
        mapDecorator.put(key, value);
        
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 10;
        
        mapDecorator.put(key, value);
        
        Short removedValue = mapDecorator.remove(key);
        
        assertEquals(value, removedValue);
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testRemove_NullKey() {
        Double key = null;
        Short value = 10;
        
        mapDecorator.put(key, value);
        
        Short removedValue = mapDecorator.remove(key);
        
        assertEquals(value, removedValue);
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testContainsKey() {
        Double key = 1.0;
        Short value = 10;
        
        mapDecorator.put(key, value);
        
        assertTrue(mapDecorator.containsKey(key));
        assertFalse(mapDecorator.containsKey(2.0));
    }
    
    @Test
    public void testContainsKey_NullKey() {
        Double key = null;
        Short value = 10;
        
        mapDecorator.put(key, value);
        
        assertTrue(mapDecorator.containsKey(key));
    }
    
    @Test
    public void testContainsValue() {
        Double key = 1.0;
        Short value = 10;
        
        mapDecorator.put(key, value);
        
        assertTrue(mapDecorator.containsValue(value));
        assertFalse(mapDecorator.containsValue((short)20));
    }
    
    @Test
    public void testContainsValue_NullValue() {
        Double key = 1.0;
        Short value = null;
        
        mapDecorator.put(key, value);
        
        assertTrue(mapDecorator.containsValue(value));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        
        mapDecorator.put(1.0, 10);
        mapDecorator.put(2.0, 20);
        mapDecorator.put(3.0, 30);
        
        assertEquals(3, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        
        mapDecorator.put(1.0, 10);
        
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testClear() {
        mapDecorator.put(1.0, 10);
        mapDecorator.put(2.0, 20);
        
        assertFalse(mapDecorator.isEmpty());
        
        mapDecorator.clear();
        
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> testMap = new HashMap<>();
        testMap.put(1.0, 10);
        testMap.put(2.0, 20);
        
        mapDecorator.putAll(testMap);
        
        assertEquals(2, mapDecorator.size());
        assertTrue(mapDecorator.containsKey(1.0));
        assertTrue(mapDecorator.containsKey(2.0));
        assertEquals(10, mapDecorator.get(1.0).intValue());
        assertEquals(20, mapDecorator.get(2.0).intValue());
    }
    
    @Test
    public void testEntrySet() {
        mapDecorator.put(1.0, 10);
        mapDecorator.put(2.0, 20);
        
        Set<Map.Entry<Double, Short>> entrySet = mapDecorator.entrySet();
        
        assertEquals(2, entrySet.size());
        
        for (Map.Entry<Double, Short> entry : entrySet) {
            assertTrue(entry.getKey() == 1.0 || entry.getKey() == 2.0);
            assertTrue(entry.getValue() == 10 || entry.getValue() == 20);
        }
    }
}