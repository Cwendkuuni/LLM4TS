package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {
    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Byte result = decorator.put((short) 1, (byte) 2);
        assertNull(result);
        
        result = decorator.put(null, (byte) 3);
        assertNull(result);
    }

    @Test
    public void testGet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        Byte result = decorator.get((short) 1);
        assertEquals((byte) 2, result.byteValue());
        
        result = decorator.get(null);
        assertNull(result);
    }
    
    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        decorator.clear();
        
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }
    
    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        
        Byte result = decorator.remove((short) 1);
        assertEquals((byte) 2, result.byteValue());
        
        result = decorator.remove(null);
        assertNull(result);
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        assertEquals(2, decorator.entrySet().size());
        
        TShortByteIterator iterator = decorator.getMap().iterator();
        assertTrue(iterator.hasNext());
        
        while (iterator.hasNext()) {
            iterator.advance();
            short key = iterator.key();
            byte value = iterator.value();
            assertTrue((key == 1 && value == 2) || (key == 2 && value == 3));
        }
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        
        boolean result = decorator.containsValue((byte) 2);
        assertTrue(result);
        
        result = decorator.containsValue(null);
        assertFalse(result);
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        
        boolean result = decorator.containsKey((short) 1);
        assertTrue(result);
        
        result = decorator.containsKey(null);
        assertFalse(result);
    }
    
    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        
        assertEquals(1, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        assertTrue(decorator.isEmpty());
        
        decorator.put((short) 1, (byte) 2);
        
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Map<Short, Byte> input = new HashMap<>();
        input.put((short) 1, (byte) 2);
        input.put((short) 2, (byte) 3);
        
        decorator.putAll(input);
        
        assertEquals(2, decorator.size());
        assertEquals((byte) 2, decorator.get((short) 1).byteValue());
        assertEquals((byte) 3, decorator.get((short) 2).byteValue());
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = decorator.wrapKey((short) 1);
        assertEquals((short) 1, key.shortValue());
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        short key = decorator.unwrapKey((short) 1);
        assertEquals((short) 1, key);
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value = decorator.wrapValue((byte) 2);
        assertEquals((byte) 2, value.byteValue());
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        byte value = decorator.unwrapValue((byte) 2);
        assertEquals((byte) 2, value);
    }
}