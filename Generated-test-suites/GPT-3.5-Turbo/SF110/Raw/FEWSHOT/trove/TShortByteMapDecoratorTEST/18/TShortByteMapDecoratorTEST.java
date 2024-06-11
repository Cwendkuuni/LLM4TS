import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.hash.TShortByteHashMap;

public class TShortByteMapDecoratorTest {
    
    private TShortByteMapDecorator mapDecorator;
    private TShortByteMap map;
    
    @Before
    public void setup() {
        map = new TShortByteHashMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }
    
    @Test
    public void testPut() {
        map.put((short)1, (byte)10);
        assertEquals(new Byte((byte)10), mapDecorator.put((short)1, (byte)10));
        assertNull(mapDecorator.put(null, (byte)20));
    }
    
    @Test
    public void testGet() {
        map.put((short)1, (byte)10);
        assertEquals(new Byte((byte)10), mapDecorator.get((short)1));
        assertNull(mapDecorator.get(null));
    }
    
    @Test
    public void testClear() {
        map.put((short)1, (byte)10);
        assertFalse(map.isEmpty());
        mapDecorator.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        map.put((short)1, (byte)10);
        assertEquals(new Byte((byte)10), mapDecorator.remove((short)1));
        assertNull(mapDecorator.remove(null));
    }
    
    @Test
    public void testContainsValue() {
        map.put((short)1, (byte)10);
        assertTrue(mapDecorator.containsValue((byte)10));
        assertFalse(mapDecorator.containsValue((byte)20));
    }
    
    @Test
    public void testContainsKey() {
        map.put((short)1, (byte)10);
        assertTrue(mapDecorator.containsKey((short)1));
        assertFalse(mapDecorator.containsKey((short)2));
        assertFalse(mapDecorator.containsKey(null));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        map.put((short)1, (byte)10);
        assertEquals(1, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        map.put((short)1, (byte)10);
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMap otherMap = new TShortByteHashMap();
        otherMap.put((short)1, (byte)10);
        otherMap.put((short)2, (byte)20);
        
        mapDecorator.putAll(otherMap);
        
        assertEquals(new Byte((byte)10), mapDecorator.get((short)1));
        assertEquals(new Byte((byte)20), mapDecorator.get((short)2));
    }

}