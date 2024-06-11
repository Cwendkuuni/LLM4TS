import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        map.put((short)3, (byte)30);
        
        assertEquals((byte)10, map.get((short)1).byteValue());
        assertEquals((byte)20, map.get((short)2).byteValue());
        assertEquals((byte)30, map.get((short)3).byteValue());
    }

    @Test
    public void testPutNull() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        
        assertEquals((byte)10, map.get((short)1).byteValue());
        
        map.put(null, (byte)20);
        
        assertNull(map.get(null));
        assertEquals((byte)20, map.get((short)1).byteValue());
    }

    @Test
    public void testGetNonexistentKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        assertNull(map.get((short)999));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        map.put((short)3, (byte)30);
        
        assertEquals(3, map.size());
        
        map.clear();
        
        assertEquals(0, map.size());
        assertNull(map.get((short)1));
        assertNull(map.get((short)2));
        assertNull(map.get((short)3));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        assertEquals((byte)10, map.remove((short)1).byteValue());
        
        assertNull(map.get((short)1));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        Set<Map.Entry<Short, Byte>> entrySet = map.entrySet();
        
        assertEquals(2, entrySet.size());
        
        for (Map.Entry<Short, Byte> entry : entrySet) {
            Short key = entry.getKey();
            Byte value = entry.getValue();
            
            assertTrue((key == (short)1 && value == (byte)10) || (key == (short)2 && value == (byte)20));
        }
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        assertTrue(map.containsValue((byte)10));
        assertFalse(map.containsValue((byte)30));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        assertTrue(map.containsKey((short)1));
        assertFalse(map.containsKey((short)3));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        assertEquals(0, map.size());
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        assertEquals(2, map.size());
        
        map.clear();
        
        assertEquals(0, map.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        assertTrue(map.isEmpty());
        
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        assertFalse(map.isEmpty());
        
        map.clear();
        
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator map1 = new TShortByteMapDecorator();
        TShortByteMapDecorator map2 = new TShortByteMapDecorator();
        
        map1.put((short)1, (byte)10);
        map1.put((short)2, (byte)20);
        
        map2.putAll(map1);
        
        assertEquals(map1.size(), map2.size());
        
        for (Map.Entry<Short, Byte> entry : map1.entrySet()) {
            assertEquals(entry.getValue(), map2.get(entry.getKey()));
        }
    }

    @Test
    public void testKeyWrapper() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        
        assertEquals((short)1, map.wrapKey((short)1).shortValue());
    }

    @Test
    public void testKeyUnwrapper() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        
        assertEquals((short)1, map.unwrapKey((short)1));
    }

    @Test
    public void testValueWrapper() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        
        assertEquals((byte)10, map.wrapValue((byte)10).byteValue());
    }

    @Test
    public void testValueUnwrapper() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)1, (byte)10);
        
        assertEquals((byte)10, map.unwrapValue((byte)10));
    }
    
}