import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        map.put((short)1, (byte)10);
        assertTrue(map.containsKey((short)1));
        assertEquals((byte)10, map.get((short)1).byteValue());
        
        // Test null key and value
        map.put(null, null);
        assertTrue(map.containsKey(null));
        assertNull(map.get(null));
    }
    
    @Test
    public void testGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        map.put((short)1, (byte)10);
        assertEquals((byte)10, map.get((short)1).byteValue());
        
        // Test non-existent key
        assertNull(map.get((short)2));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        map.put((short)1, (byte)10);
        map.clear();
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey((short)1));
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        map.put((short)1, (byte)10);
        map.remove((short)1);
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey((short)1));
        
        // Test non-existent key
        assertNull(map.remove((short)2));
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        map.put((short)1, (byte)10);
        assertTrue(map.containsValue((byte)10));
        
        // Test non-existent value
        assertFalse(map.containsValue((byte)20));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        map.put((short)1, (byte)10);
        assertTrue(map.containsKey((short)1));
        
        // Test non-existent key
        assertFalse(map.containsKey((short)2));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        assertEquals(0, map.size());
        map.put((short)1, (byte)10);
        assertEquals(1, map.size());
        
        // Test multiple entries
        map.put((short)2, (byte)20);
        assertEquals(2, map.size());
        
        // Test clearing the map
        map.clear();
        assertEquals(0, map.size());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        Map<Short, Byte> inputMap = new HashMap<>();
        inputMap.put((short)1, (byte)10);
        inputMap.put((short)2, (byte)20);
        inputMap.put((short)3, (byte)30);
        map.putAll(inputMap);
        assertEquals(3, map.size());
        assertEquals((byte)10, map.get((short)1).byteValue());
        assertEquals((byte)20, map.get((short)2).byteValue());
        assertEquals((byte)30, map.get((short)3).byteValue());
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        assertEquals((short)1, (short)map.wrapKey((short)1));
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        assertEquals((short)1, map.unwrapKey((short)1));
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        assertEquals((byte)10, (byte)map.wrapValue((byte)10));
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        
        // Test basic functionality
        assertEquals((byte)10, map.unwrapValue((byte)10));
    }
}