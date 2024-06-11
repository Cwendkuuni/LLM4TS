package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;
        
        assertNull(map.put(0.0, (short)0));
        assertNull(map.put(key1, value1));
        assertNull(map.put(key2, value2));
        assertEquals(value1, map.get(key1));
        assertEquals(value2, map.get(key2));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;
        
        map.put(key1, value1);
        map.put(key2, value2);
        
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;
        
        map.put(key1, value1);
        map.put(key2, value2);
        
        assertEquals(value1, map.remove(key1));
        assertNull(map.get(key1));
        assertNull(map.remove(key1));
    }
    
    @Test
    public void testContainsKeyAndContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;
        
        map.put(key1, value1);
        map.put(key2, value2);
        
        assertTrue(map.containsKey(key1));
        assertTrue(map.containsKey(key2));
        assertFalse(map.containsKey(3.0));
        
        assertTrue(map.containsValue(value1));
        assertTrue(map.containsValue(value2));
        assertFalse(map.containsValue((short)30));
    }
    
    @Test
    public void testSizeAndIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;
        
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        
        map.put(key1, value1);
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
        
        map.put(key2, value2);
        assertEquals(2, map.size());
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map1 = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        
        TDoubleShortMapDecorator map2 = new TDoubleShortMapDecorator();
        Double key2 = 2.0;
        Short value2 = 20;
        
        map1.put(key1, value1);
        map2.put(key2, value2);
        
        map1.putAll(map2);
        
        assertEquals(value1, map1.get(key1));
        assertEquals(value2, map1.get(key2));
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;
        
        map.put(key1, value1);
        map.put(key2, value2);
        
        for (Entry<Double, Short> entry : map.entrySet()) {
            assertTrue(entry.getKey().equals(key1) || entry.getKey().equals(key2));
            assertTrue(entry.getValue().equals(value1) || entry.getValue().equals(value2));
        }
    }
    
}