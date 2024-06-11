import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.get(key1));
        
        Short key2 = 3;
        Byte value2 = 4;
        mapDecorator.put(key2, value2);
        assertEquals(value2, mapDecorator.get(key2));

        assertEquals(null, mapDecorator.get(null));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        
        Short key2 = 3;
        Byte value2 = 4;
        mapDecorator.put(key2, value2);
        
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        
        Short key2 = 3;
        Byte value2 = 4;
        mapDecorator.put(key2, value2);
        
        mapDecorator.remove(key1);
        assertEquals(null, mapDecorator.get(key1));
        assertEquals(1, mapDecorator.size());
        
        assertEquals(null, mapDecorator.remove(null));
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        
        assertTrue(mapDecorator.containsValue(value1));
        assertFalse(mapDecorator.containsValue(3));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        
        assertTrue(mapDecorator.containsKey(key1));
        assertFalse(mapDecorator.containsKey(3));
        assertFalse(mapDecorator.containsKey(null));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        
        Short key2 = 3;
        Byte value2 = 4;
        mapDecorator.put(key2, value2);
        
        assertEquals(2, mapDecorator.size());
        
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Short key1 = 1;
        Byte value1 = 2;
        
        Map<Short, Byte> map = new HashMap<Short, Byte>();
        map.put(key1, value1);
        
        mapDecorator.putAll(map);
        
        assertEquals(value1, mapDecorator.get(key1));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        
        Short key2 = 3;
        Byte value2 = 4;
        mapDecorator.put(key2, value2);
        
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        
        assertEquals(2, entrySet.size());
        
        Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        
        Map.Entry<Short, Byte> entry1 = iterator.next();
        assertEquals(key1, entry1.getKey());
        assertEquals(value1, entry1.getValue());
        
        Map.Entry<Short, Byte> entry2 = iterator.next();
        assertEquals(key2, entry2.getKey());
        assertEquals(value2, entry2.getValue());
    }
}