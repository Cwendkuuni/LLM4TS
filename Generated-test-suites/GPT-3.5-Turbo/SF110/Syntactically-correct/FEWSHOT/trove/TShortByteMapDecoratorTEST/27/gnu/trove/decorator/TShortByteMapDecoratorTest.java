package gnu.trove.decorator;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)10);
        mapDecorator.put((short)1, (byte)10);
        assertEquals(mapDecorator.get((short)1), map.get((short)1));
        
        map.put((short)2, (byte)20);
        mapDecorator.put((short)2, (byte)20);
        assertEquals(mapDecorator.get((short)2), map.get((short)2));
        
        map.put((short)3, (byte)30);
        mapDecorator.put((short)3, (byte)30);
        assertEquals(mapDecorator.get((short)3), map.get((short)3));
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)10);
        mapDecorator.put((short)1, (byte)10);
        map.remove((short)1);
        mapDecorator.remove((short)1);
        assertNull(mapDecorator.get((short)1));
        
        map.put((short)2, (byte)20);
        mapDecorator.put((short)2, (byte)20);
        map.remove((short)2);
        mapDecorator.remove((short)2);
        assertNull(mapDecorator.get((short)2));
        
        map.put((short)3, (byte)30);
        mapDecorator.put((short)3, (byte)30);
        map.remove((short)3);
        mapDecorator.remove((short)3);
        assertNull(mapDecorator.get((short)3));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)10);
        mapDecorator.put((short)1, (byte)10);
        assertTrue(mapDecorator.containsKey((short)1));
        
        assertFalse(mapDecorator.containsKey((short)2));
        
        map.put((short)3, (byte)30);
        mapDecorator.put((short)3, (byte)30);
        assertTrue(mapDecorator.containsKey((short)3));
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)10);
        mapDecorator.put((short)1, (byte)10);
        assertTrue(mapDecorator.containsValue((byte)10));
        
        assertFalse(mapDecorator.containsValue((byte)20));
        
        map.put((short)3, (byte)30);
        mapDecorator.put((short)3, (byte)30);
        assertTrue(mapDecorator.containsValue((byte)30));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        assertEquals(mapDecorator.size(), map.size());
        
        map.put((short)1, (byte)10);
        mapDecorator.put((short)1, (byte)10);
        assertEquals(mapDecorator.size(), map.size());
        
        map.put((short)2, (byte)20);
        mapDecorator.put((short)2, (byte)20);
        assertEquals(mapDecorator.size(), map.size());
        
        map.put((short)3, (byte)30);
        mapDecorator.put((short)3, (byte)30);
        assertEquals(mapDecorator.size(), map.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        assertTrue(mapDecorator.isEmpty());
        
        mapDecorator.put((short)1, (byte)10);
        assertFalse(mapDecorator.isEmpty());
        
        mapDecorator.remove((short)1);
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        mapDecorator.put((short)1, (byte)10);
        mapDecorator.put((short)2, (byte)20);
        
        map.clear();
        mapDecorator.clear();
        
        assertEquals(mapDecorator.size(), map.size());
    }
    
    @Test
    public void testIterator() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)10);
        mapDecorator.put((short)1, (byte)10);
        
        map.put((short)2, (byte)20);
        mapDecorator.put((short)2, (byte)20);
        
        map.put((short)3, (byte)30);
        mapDecorator.put((short)3, (byte)30);
        
        for (Map.Entry<Short, Byte> entry : mapDecorator.entrySet()) {
            assertTrue(map.containsKey(entry.getKey()));
            assertTrue(map.containsValue(entry.getValue()));
        }
    }
}