import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        
        mapDecorator.put(key, value);
        
        assertEquals(value, mapDecorator.get(key));
    }
    
    @Test
    public void testPutNullKeyAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = null;
        Byte value = 2;
        
        mapDecorator.put(key, value);
        
        assertEquals(value, mapDecorator.get(null));
    }
    
    @Test
    public void testPutAndGetNullValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = null;
        
        mapDecorator.put(key, value);
        
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testPutNullKeyAndGetNullValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = null;
        Byte value = null;
        
        mapDecorator.put(key, value);
        
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);
        
        mapDecorator.clear();
        
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        
        Byte removedValue = mapDecorator.remove((short)1);
        
        assertEquals((byte)2, (byte)removedValue);
        assertNull(mapDecorator.get((short)1));
    }
    
    @Test
    public void testRemoveNonExistingKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        Byte removedValue = mapDecorator.remove((short)1);
        
        assertNull(removedValue);
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);
        
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        
        assertEquals(2, entrySet.size());
        for (Map.Entry<Short, Byte> entry : entrySet) {
            Short key = entry.getKey();
            Byte value = entry.getValue();
            assertTrue(mapDecorator.containsKey(key));
            assertEquals(value, mapDecorator.get(key));
        }
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        
        boolean containsValue = mapDecorator.containsValue((byte)2);
        
        assertTrue(containsValue);
    }
    
    @Test
    public void testDoesNotContainValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        
        boolean containsValue = mapDecorator.containsValue((byte)3);
        
        assertFalse(containsValue);
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        
        boolean containsKey = mapDecorator.containsKey((short)1);
        
        assertTrue(containsKey);
    }
    
    @Test
    public void testDoesNotContainKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        
        boolean containsKey = mapDecorator.containsKey((short)2);
        
        assertFalse(containsKey);
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        
        int size = mapDecorator.size();
        
        assertEquals(1, size);
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        
        boolean isEmpty = mapDecorator.isEmpty();
        
        assertTrue(isEmpty);
    }
    
    @Test
    public void testIsNotEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        
        boolean isEmpty = mapDecorator.isEmpty();
        
        assertFalse(isEmpty);
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short)1, (byte)2);
        map.put((short)3, (byte)4);
        
        mapDecorator.putAll(map);
        
        assertEquals(2, mapDecorator.size());
        for (Map.Entry<Short, Byte> entry : map.entrySet()) {
            Short key = entry.getKey();
            Byte value = entry.getValue();
            assertTrue(mapDecorator.containsKey(key));
            assertEquals(value, mapDecorator.get(key));
        }
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        short key = 1;
        
        Short wrappedKey = mapDecorator.wrapKey(key);
        
        assertEquals(key, (short)wrappedKey);
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        
        short unwrappedKey = mapDecorator.unwrapKey(key);
        
        assertEquals((short)key, unwrappedKey);
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        byte value = 2;
        
        Byte wrappedValue = mapDecorator.wrapValue(value);
        
        assertEquals(value, (byte)wrappedValue);
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Byte value = 2;
        
        byte unwrappedValue = mapDecorator.unwrapValue(value);
        
        assertEquals((byte)value, unwrappedValue);
    }
}