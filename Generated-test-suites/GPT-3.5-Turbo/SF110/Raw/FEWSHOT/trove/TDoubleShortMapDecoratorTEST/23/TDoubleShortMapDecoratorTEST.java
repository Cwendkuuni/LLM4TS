package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.map.TDoubleShortMap;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setup() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        
        mapDecorator = new TDoubleShortMapDecorator(map);
    }
    
    @Test
    public void testPut() {
        assertEquals((Short) 4, mapDecorator.put(4.0, (short) 4));
        assertNull(mapDecorator.put(null, (short) 5));
    }
    
    @Test
    public void testGet() {
        assertEquals((Short) 1, mapDecorator.get(1.0));
        assertEquals((Short) 2, mapDecorator.get(2.0));
        assertEquals((Short) 3, mapDecorator.get(3.0));
        assertNull(mapDecorator.get(4.0));
        assertNull(mapDecorator.get(null));
    }
    
    @Test
    public void testClear() {
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        assertEquals((Short) 1, mapDecorator.remove(1.0));
        assertEquals((Short) 2, mapDecorator.remove(2.0));
        assertEquals((Short) 3, mapDecorator.remove(3.0));
        assertNull(mapDecorator.remove(4.0));
        assertNull(mapDecorator.remove(null));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(mapDecorator.containsValue((short) 1));
        assertFalse(mapDecorator.containsValue((short) 4));
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(4.0));
        assertFalse(mapDecorator.containsKey(null));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMap otherMap = new TDoubleShortHashMap();
        otherMap.put(4.0, (short) 4);
        otherMap.put(5.0, (short) 5);
        
        mapDecorator.putAll(otherMap);
        
        assertEquals((Short) 4, mapDecorator.get(4.0));
        assertEquals((Short) 5, mapDecorator.get(5.0));
    }
}