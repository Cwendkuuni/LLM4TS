package gnu.trove.decorator;

import gnu.trove.map.TShortByteMap;
import junit.framework.TestCase;

public class TShortByteMapDecoratorTest extends TestCase {

    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 10);
        assertNotNull(decorator.get((short) 1));
        assertEquals((byte) 10, decorator.get((short) 1).byteValue());
    }

    public void testGet() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertNotNull(decorator.get((short) 1));
        assertEquals((byte) 10, decorator.get((short) 1).byteValue());
        assertNull(decorator.get((short) 2));
    }

    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.remove((short) 1);
        assertNull(decorator.get((short) 1));
    }

    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertNotNull(decorator.entrySet());
        assertEquals(1, decorator.entrySet().size());
    }

    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.containsValue((byte) 10));
        assertFalse(decorator.containsValue((byte) 20));
    }

    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.containsKey((short) 1));
        assertFalse(decorator.containsKey((short) 2));
    }

    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(1, decorator.size());
    }

    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.isEmpty());
        map.put((short) 1, (byte) 10);
        assertFalse(decorator.isEmpty());
    }

    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Map<Short, Byte> inputMap = new HashMap<>();
        inputMap.put((short) 1, (byte) 10);
        inputMap.put((short) 2, (byte) 20);
        decorator.putAll(inputMap);
        assertEquals(2, decorator.size());
        assertNotNull(decorator.get((short) 1));
        assertEquals((byte) 10, decorator.get((short) 1).byteValue());
        assertNotNull(decorator.get((short) 2));
        assertEquals((byte) 20, decorator.get((short) 2).byteValue());
    }
}