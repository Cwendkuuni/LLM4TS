package gnu.trove.decorator;

import static org.junit.Assert.*;
import org.junit.Test;

import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));
        assertNull(decorator.get((short) 5));
        assertNull(decorator.get(null));
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        
        decorator.clear();
        
        assertEquals(0, decorator.size());
        assertNull(decorator.get((short) 1));
        assertNull(decorator.get((short) 3));
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        
        Byte removedValue = decorator.remove((short) 1);
        
        assertEquals(Byte.valueOf((byte) 2), removedValue);
        assertNull(decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));  
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 2);
        
        assertTrue(decorator.containsKey((short) 1));
        assertFalse(decorator.containsKey((short) 2));
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 2);
        
        assertTrue(decorator.containsValue((byte) 2));
        assertFalse(decorator.containsValue((byte) 3));
        assertFalse(decorator.containsValue(null));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        
        assertEquals(2, decorator.size());
        decorator.put((short) 5, (byte) 6);
        assertEquals(3, decorator.size());
        decorator.remove((short) 3);
        assertEquals(2, decorator.size());
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        TShortByteMap mapToPut = new TShortByteMap();
        mapToPut.put((short) 1, (byte) 2);
        mapToPut.put((short) 3, (byte) 4);
        
        decorator.putAll(mapToPut);
        
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));
    }

    @Test
    public void testWrapAndUnwrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        assertEquals(1, decorator.unwrapKey((short) 1));
        assertEquals((short) 1, decorator.wrapKey(1).shortValue());
        assertEquals(0, decorator.unwrapKey(null));
        assertNull(decorator.wrapKey(null));
    }

    @Test
    public void testWrapAndUnwrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        assertEquals((byte) 2, decorator.unwrapValue((byte) 2));
        assertEquals((byte) 2, decorator.wrapValue((byte) 2).byteValue());
        assertEquals(0, decorator.unwrapValue(null));
        assertNull(decorator.wrapValue(null));
    }
}