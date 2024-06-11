import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Byte value1 = mapDecorator.put((short) 1, (byte) 10);
        assertEquals(null, value1);
        assertTrue(mapDecorator.containsKey((short) 1));
        assertEquals((byte) 10, mapDecorator.get((short) 1).byteValue());
        
        Byte value2 = mapDecorator.put((short) 1, (byte) 20);
        assertEquals((byte) 10, value2.byteValue());
        assertTrue(mapDecorator.containsKey((short) 1));
        assertEquals((byte) 20, mapDecorator.get((short) 1).byteValue());
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        mapDecorator.put((short) 1, (byte) 10);
        assertTrue(mapDecorator.containsKey((short) 1));
        
        Byte value = mapDecorator.remove((short) 1);
        assertEquals((byte) 10, value.byteValue());
        assertFalse(mapDecorator.containsKey((short) 1));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);
        
        assertFalse(mapDecorator.isEmpty());
        
        mapDecorator.clear();
        
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        assertEquals(0, mapDecorator.size());
        
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);
        
        assertEquals(2, mapDecorator.size());
        
        mapDecorator.remove((short) 1);
        
        assertEquals(1, mapDecorator.size());
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        mapDecorator.put((short) 1, (byte) 10);
        
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 10);
        mapDecorator.put((short) 3, (byte) 20);
        
        assertTrue(mapDecorator.containsValue((byte) 10));
        assertTrue(mapDecorator.containsValue((byte) 20));
        assertFalse(mapDecorator.containsValue((byte) 30));
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 10);
        map.put((short) 2, (byte) 20);
        
        mapDecorator.putAll(map);
        
        assertTrue(mapDecorator.containsKey((short) 1));
        assertTrue(mapDecorator.containsKey((short) 2));
        assertEquals((byte) 10, mapDecorator.get((short) 1).byteValue());
        assertEquals((byte) 20, mapDecorator.get((short) 2).byteValue());
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);
        
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        
        assertEquals(2, entrySet.size());
        
        for (Map.Entry<Short, Byte> entry : entrySet) {
            Short key = entry.getKey();
            Byte value = entry.getValue();
            
            assertTrue(mapDecorator.containsKey(key));
            assertTrue(mapDecorator.containsValue(value));
        }
    }

}