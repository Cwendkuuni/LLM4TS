import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    
    private TShortByteMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        TShortByteMap map = new TShortByteMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }
    
    @Test
    public void testPutAndGet() {
        Short key = 1;
        Byte value = 2;
        
        assertNull(mapDecorator.put(key, value));
        assertEquals(value, mapDecorator.get(key));
    }
    
    @Test
    public void testPutNullKey() {
        Short key = null;
        Byte value = 2;
        
        TShortByteMap map = mapDecorator.getMap();
        byte noEntryKey = map.getNoEntryKey();
        
        assertEquals(noEntryKey, mapDecorator.put(key, value).shortValue());
        assertEquals(value, mapDecorator.get(key));
    }
    
    @Test
    public void testPutNullValue() {
        Short key = 1;
        Byte value = null;
        
        TShortByteMap map = mapDecorator.getMap();
        byte noEntryValue = map.getNoEntryValue();
        
        assertNull(mapDecorator.put(key, value));
        assertEquals(noEntryValue, mapDecorator.get(key).byteValue());
    }
    
    @Test
    public void testGetWithNullKey() {
        Byte value = 2;
        
        assertNull(mapDecorator.get(null));
        mapDecorator.put(null, value);
        assertEquals(value, mapDecorator.get(null));
    }
    
    @Test
    public void testClear() {
        Short key = 1;
        Byte value = 2;
        
        mapDecorator.put(key, value);
        assertFalse(mapDecorator.isEmpty());
        
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
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
    public void testEntrySet() {
        Short key = 1;
        Byte value = 2;
        
        mapDecorator.put(key, value);
        
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        assertEquals(1, entrySet.size());
        
        Map.Entry<Short, Byte> entry = entrySet.iterator().next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }
    
    @Test
    public void testContainsValue() {
        Byte value = 2;
        
        assertFalse(mapDecorator.containsValue(value));
        
        mapDecorator.put((short)1, value);
        assertTrue(mapDecorator.containsValue(value));
    }
    
    @Test
    public void testContainsKey() {
        assertFalse(mapDecorator.containsKey((short)1));
        
        mapDecorator.put((short)1, (byte)2);
        assertTrue(mapDecorator.containsKey((short)1));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        
        mapDecorator.put((short)1, (byte)2);
        assertEquals(1, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        
        mapDecorator.put((short)1, (byte)2);
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Short, Byte> inputMap = new AbstractMap<Short, Byte>() {
            @Override
            public Set<java.util.Map.Entry<Short, Byte>> entrySet() {
                return null;
            }
        };
        
        inputMap.put((short)1, (byte)2);
        inputMap.put((short)3, (byte)4);
        
        mapDecorator.putAll(inputMap);
        assertEquals(2, mapDecorator.size());
        assertEquals((byte)2, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)4, mapDecorator.get((short)3).byteValue());
    }
    
    @Test
    public void testWrapKey() {
        Short key = 1;
        
        assertEquals(key, mapDecorator.wrapKey((short)1));
    }
    
    @Test
    public void testUnwrapKey() {
        Short key = 1;
        
        assertEquals((short)1, mapDecorator.unwrapKey(key));
    }
    
    @Test
    public void testWrapValue() {
        Byte value = 2;
        
        assertEquals(value, mapDecorator.wrapValue((byte)2));
    }
    
    @Test
    public void testUnwrapValue() {
        Byte value = 2;
        
        assertEquals((byte)2, mapDecorator.unwrapValue(value));
    }
    
    @Test
    public void testReadExternal() {
        // Not testable without mocking and external dependencies
    }
    
    @Test
    public void testWriteExternal() {
        // Not testable without mocking and external dependencies
    }
    
}