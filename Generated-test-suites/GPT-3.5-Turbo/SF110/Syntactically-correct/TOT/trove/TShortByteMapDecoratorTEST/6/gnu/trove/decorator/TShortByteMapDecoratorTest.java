import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Typical case
        Byte value = map.put((short)10, (byte)5);
        assertEquals(Byte.valueOf((byte)5), value);
        
        // Edge case - key is null
        value = map.put(null, (byte)6);
        assertEquals(Byte.valueOf((byte)6), value);
        
        // Error case - key already exists
        value = map.put((short)10, (byte)8);
        assertEquals(Byte.valueOf((byte)5), value);
    }
    
    @Test
    public void testGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Typical case - key exists
        map.put((short)10, (byte)5);
        Byte value = map.get((short)10);
        assertEquals(Byte.valueOf((byte)5), value);
        
        // Edge case - key does not exist
        value = map.get((short)20);
        assertNull(value);
        
        // Error case - key is null
        value = map.get(null);
        assertNull(value);
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)10, (byte)5);
        map.put((short)20, (byte)6);
        map.put((short)30, (byte)7);
        
        map.clear();
        
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Typical case - key exists
        map.put((short)10, (byte)5);
        Byte value = map.remove((short)10);
        assertEquals(Byte.valueOf((byte)5), value);
        
        // Edge case - key does not exist
        value = map.remove((short)20);
        assertNull(value);
        
        // Error case - key is null
        value = map.remove(null);
        assertNull(value);
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Typical case - value exists
        map.put((short)10, (byte)5);
        boolean contains = map.containsValue((byte)5);
        assertTrue(contains);
        
        // Edge case - value does not exist
        contains = map.containsValue((byte)6);
        assertFalse(contains);
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Typical case - key exists
        map.put((short)10, (byte)5);
        boolean contains = map.containsKey((short)10);
        assertTrue(contains);
        
        // Edge case - key does not exist
        contains = map.containsKey((short)20);
        assertFalse(contains);
        
        // Error case - key is null
        contains = map.containsKey(null);
        assertTrue(contains);
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        map.put((short)10, (byte)5);
        map.put((short)20, (byte)6);
        map.put((short)30, (byte)7);
        
        int size = map.size();
        assertEquals(3, size);
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        Map<Short, Byte> inputMap = new HashMap<>();
        inputMap.put((short)10, (byte)5);
        inputMap.put((short)20, (byte)6);
        inputMap.put((short)30, (byte)7);
        
        map.putAll(inputMap);
        
        assertTrue(map.containsKey((short)10));
        assertTrue(map.containsValue((byte)5));
        assertTrue(map.containsKey((short)20));
        assertTrue(map.containsValue((byte)6));
        assertTrue(map.containsKey((short)30));
        assertTrue(map.containsValue((byte)7));
    }
}