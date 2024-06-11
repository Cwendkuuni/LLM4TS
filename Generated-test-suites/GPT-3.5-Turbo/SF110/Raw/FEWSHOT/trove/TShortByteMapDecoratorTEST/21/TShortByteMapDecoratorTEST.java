package gnu.trove.decorator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator decorator;
    
    @Before
    public void setUp() {
        decorator = new TShortByteMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        decorator.put((short) 1, (byte) 2);
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        
        decorator.put((short) 2, (byte) 3);
        assertEquals(Byte.valueOf((byte) 3), decorator.get((short) 2));
        
        decorator.put((short) 3, (byte) 4);
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));
    }
    
    @Test
    public void testContainsKey() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        decorator.put((short) 3, (byte) 4);
        
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 2));
        assertTrue(decorator.containsKey((short) 3));
        assertFalse(decorator.containsKey((short) 4));
    }
    
    @Test
    public void testContainsValue() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        decorator.put((short) 3, (byte) 4);
        
        assertTrue(decorator.containsValue((byte) 2));
        assertTrue(decorator.containsValue((byte) 3));
        assertTrue(decorator.containsValue((byte) 4));
        assertFalse(decorator.containsValue((byte) 5));
    }
    
    @Test
    public void testRemove() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        decorator.put((short) 3, (byte) 4);
        
        assertEquals(Byte.valueOf((byte) 2), decorator.remove((short) 1));
        assertNull(decorator.get((short) 1));
        
        assertEquals(Byte.valueOf((byte) 3), decorator.remove((short) 2));
        assertNull(decorator.get((short) 2));
        
        assertEquals(Byte.valueOf((byte) 4), decorator.remove((short) 3));
        assertNull(decorator.get((short) 3));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, decorator.size());
        
        decorator.put((short) 1, (byte) 2);
        assertEquals(1, decorator.size());
        
        decorator.put((short) 2, (byte) 3);
        assertEquals(2, decorator.size());
        
        decorator.put((short) 3, (byte) 4);
        assertEquals(3, decorator.size());
        
        decorator.remove((short) 2);
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        
        decorator.put((short) 1, (byte) 2);
        assertFalse(decorator.isEmpty());
        
        decorator.remove((short) 1);
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testClear() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        decorator.put((short) 3, (byte) 4);
        
        assertFalse(decorator.isEmpty());
        
        decorator.clear();
        
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get((short) 1));
        assertNull(decorator.get((short) 2));
        assertNull(decorator.get((short) 3));
    }
    
    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);
        map.put((short) 3, (byte) 4);
        
        decorator.putAll(map);
        
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 3), decorator.get((short) 2));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));
    }
    
    @Test
    public void testEntrySet() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        decorator.put((short) 3, (byte) 4);
        
        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        
        assertEquals(3, entrySet.size());
        
        for (Map.Entry<Short, Byte> entry : entrySet) {
            Short key = entry.getKey();
            Byte value = entry.getValue();
            
            assertNotNull(key);
            assertNotNull(value);
            assertTrue(decorator.containsKey(key));
            assertEquals(decorator.get(key), value);
        }
    }
}