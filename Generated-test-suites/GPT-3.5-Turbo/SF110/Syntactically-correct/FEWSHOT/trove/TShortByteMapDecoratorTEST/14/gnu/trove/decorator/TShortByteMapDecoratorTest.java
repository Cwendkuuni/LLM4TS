import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        assertNull(mapDecorator.put(key1, value1));
        assertEquals(value1, mapDecorator.get(key1));
        
        Short key2 = (short) 2;
        Byte value2 = (byte) 2;
        assertNull(mapDecorator.put(key2, value2));
        assertEquals(value2, mapDecorator.get(key2));
    }
    
    @Test
    public void testPutAndGetWithNullKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = null;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        assertEquals(value, mapDecorator.get(key));
    }
    
    @Test
    public void testPutAndGetWithNullValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = (short) 1;
        Byte value = null;
        assertNull(mapDecorator.put(key, value));
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = (short) 1;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        
        mapDecorator.clear();
        
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = (short) 1;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        
        assertEquals(value, mapDecorator.remove(key));
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testRemoveWithNullKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = null;
        assertNull(mapDecorator.remove(key));
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        assertNull(mapDecorator.put(key1, value1));
        
        Short key2 = (short) 2;
        Byte value2 = (byte) 2;
        assertNull(mapDecorator.put(key2, value2));
        
        assertEquals(2, mapDecorator.entrySet().size());
        
        mapDecorator.entrySet().forEach(entry -> {
            if (entry.getKey().equals(key1)) {
                assertEquals(value1, entry.getValue());
            } else if (entry.getKey().equals(key2)) {
                assertEquals(value2, entry.getValue());
            }
        });
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = (short) 1;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        
        assertTrue(mapDecorator.containsValue(value));
    }
    
    @Test
    public void testDoesNotContainValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = (short) 1;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        
        assertFalse(mapDecorator.containsValue((byte) 2));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = (short) 1;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        
        assertTrue(mapDecorator.containsKey(key));
    }
    
    @Test
    public void testDoesNotContainKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = (short) 1;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        
        assertFalse(mapDecorator.containsKey((short) 2));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        assertEquals(0, mapDecorator.size());
        
        Short key = (short) 1;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        
        assertEquals(1, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        assertTrue(mapDecorator.isEmpty());
        
        Short key = (short) 1;
        Byte value = (byte) 1;
        assertNull(mapDecorator.put(key, value));
        
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        
        mapDecorator.putAll(map);
        
        assertEquals((byte) 1, (byte) mapDecorator.get((short) 1));
        assertEquals((byte) 2, (byte) mapDecorator.get((short) 2));
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        assertEquals((short) 1, (short) mapDecorator.wrapKey((short) 1));
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        assertEquals((short) 1, (short) mapDecorator.unwrapKey((short) 1));
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        assertEquals((byte) 1, (byte) mapDecorator.wrapValue((byte) 1));
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        assertEquals((byte) 1, (byte) mapDecorator.unwrapValue((byte) 1));
    }
}