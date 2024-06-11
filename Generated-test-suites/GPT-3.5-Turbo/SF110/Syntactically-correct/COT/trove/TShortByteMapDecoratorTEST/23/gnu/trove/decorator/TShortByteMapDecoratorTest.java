
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        mapDecorator = new TShortByteMapDecorator();
    }
    
    @Test
    public void testPut() {
        Short key1 = 1;
        Byte value1 = 3;
        Byte result1 = mapDecorator.put(key1, value1);
        assertNull(result1);
        
        Short key2 = 2;
        Byte value2 = null;
        Byte result2 = mapDecorator.put(key2, value2);
        assertNull(result2);
    }
    
    @Test
    public void testGet() {
        Short key1 = 1;
        Byte value1 = 3;
        mapDecorator.put(key1, value1);
        Byte result1 = mapDecorator.get(key1);
        assertEquals(value1, result1);
        
        Short key2 = 2;
        Byte result2 = mapDecorator.get(key2);
        assertNull(result2);
    }
    
    @Test
    public void testClear() {
        Short key1 = 1;
        Byte value1 = 3;
        mapDecorator.put(key1, value1);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        Short key1 = 1;
        Byte value1 = 3;
        mapDecorator.put(key1, value1);
        Byte result1 = mapDecorator.remove(key1);
        assertEquals(value1, result1);
        
        Short key2 = 2;
        Byte result2 = mapDecorator.remove(key2);
        assertNull(result2);
    }
    
    @Test
    public void testEntrySet() {
        Short key1 = 1;
        Byte value1 = 3;
        mapDecorator.put(key1, value1);
        assertNotNull(mapDecorator.entrySet());
        assertTrue(mapDecorator.entrySet().contains(new TShortByteMapDecorator.MapEntry(key1, value1)));
        
        Short key2 = 2;
        assertFalse(mapDecorator.entrySet().contains(new TShortByteMapDecorator.MapEntry(key2, value1)));
    }
    
    @Test
    public void testContainsValue() {
        Short key1 = 1;
        Byte value1 = 3;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsValue(value1));
        assertFalse(mapDecorator.containsValue((byte) 2));
    }
    
    @Test
    public void testContainsKey() {
        Short key1 = 1;
        Byte value1 = 3;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsKey(key1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }
    
    @Test
    public void testSize() {
        Short key1 = 1;
        Byte value1 = 3;
        mapDecorator.put(key1, value1);
        assertEquals(1, mapDecorator.size());
        
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        
        Short key1 = 1;
        Byte value1 = 3;
        mapDecorator.put(key1, value1);
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator newMap = new TShortByteMapDecorator();
        newMap.put((short) 1, (byte) 3);
        newMap.put((short) 2, (byte) 4);
        
        mapDecorator.putAll(newMap);
        assertEquals(2, mapDecorator.size());
    }
    
    @Test
    public void testWrapKey() {
        short key = 1;
        Short wrappedKey = mapDecorator.wrapKey(key);
        assertEquals(key, wrappedKey.shortValue());
    }
    
    @Test
    public void testUnwrapKey() {
        Short wrappedKey = 1;
        short key = mapDecorator.unwrapKey(wrappedKey);
        assertEquals(wrappedKey.shortValue(), key);
    }
    
    @Test
    public void testWrapValue() {
        byte value = 3;
        Byte wrappedValue = mapDecorator.wrapValue(value);
        assertEquals(value, wrappedValue.byteValue());
    }
    
    @Test
    public void testUnwrapValue() {
        Byte wrappedValue = 3;
        byte value = mapDecorator.unwrapValue(wrappedValue);
        assertEquals(wrappedValue.byteValue(), value);
    }
}
