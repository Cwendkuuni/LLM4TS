import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class TShortByteMapDecoratorTest {
    
    TShortByteMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        TShortByteMap map = new TShortByteHashMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }
    
    @Test
    public void testPutAndGet() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }
    
    @Test
    public void testClear() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        mapDecorator.clear();
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testRemove() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testContainsKey() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsKey(key));
    }
    
    @Test
    public void testContainsValue() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsValue(value));
    }
    
    @Test
    public void testSize() {
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        Short key2 = 2;
        Byte value2 = 3;
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);
        mapDecorator.putAll(map);
        assertEquals(map, mapDecorator.getMap());
    }
    
    @Test
    public void testEntrySetSize() {
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        Short key2 = 2;
        Byte value2 = 3;
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.entrySet().size());
    }
    
    @Test
    public void testEntrySetIsEmpty() {
        assertTrue(mapDecorator.entrySet().isEmpty());
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertFalse(mapDecorator.entrySet().isEmpty());
    }
    
    @Test
    public void testEntrySetContains() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.entrySet().contains(new AbstractMap.SimpleEntry<>(key, value)));
    }
    
    @Test
    public void testEntrySetIterator() {
        Short key1 = 1;
        Byte value1 = 2;
        mapDecorator.put(key1, value1);
        Short key2 = 2;
        Byte value2 = 3;
        mapDecorator.put(key2, value2);
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        int count = 0;
        for (Map.Entry<Short, Byte> entry : entrySet) {
            count++;
            if (count == 1) {
                assertEquals(key1, entry.getKey());
                assertEquals(value1, entry.getValue());
            } else if (count == 2) {
                assertEquals(key2, entry.getKey());
                assertEquals(value2, entry.getValue());
            }
        }
        assertEquals(2, count);
    }
    
    @Test
    public void testEntrySetRemove() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.entrySet().remove(new AbstractMap.SimpleEntry<>(key, value)));
        assertFalse(mapDecorator.containsKey(key));
    }
    
    @Test
    public void testEntrySetAddAll() {
        try {
            mapDecorator.entrySet().addAll(null);
        } catch (UnsupportedOperationException e) {
            assertEquals(0, mapDecorator.entrySet().size());
        }
    }
    
    @Test
    public void testEntrySetClear() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        mapDecorator.entrySet().clear();
        assertFalse(mapDecorator.containsKey(key));
    }
    
    @Test
    public void testWrapKey() {
        short k = 1;
        Short key = mapDecorator.wrapKey(k);
        assertEquals(k, (short) key);
    }
    
    @Test
    public void testUnwrapKey() {
        Short key = 1;
        short k = mapDecorator.unwrapKey(key);
        assertEquals(key, (short) k);
    }
    
    @Test
    public void testWrapValue() {
        byte v = 2;
        Byte value = mapDecorator.wrapValue(v);
        assertEquals(v, (byte) value);
    }
    
    @Test
    public void testUnwrapValue() {
        Byte value = 2;
        byte v = mapDecorator.unwrapValue(value);
        assertEquals(value, (byte) v);
    }
    
    @Test
    public void testReadExternal() {
        // Implementation not provided in source code
    }
    
    @Test
    public void testWriteExternal() {
        // Implementation not provided in source code
    }
}