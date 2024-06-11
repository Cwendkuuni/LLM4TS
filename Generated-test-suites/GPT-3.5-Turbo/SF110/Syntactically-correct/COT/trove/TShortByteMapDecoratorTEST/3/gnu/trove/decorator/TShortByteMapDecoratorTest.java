
package gnu.trove.decorator;

import gnu.trove.map.TShortByteMap;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 10);

        assertTrue(decorator.containsKey((short) 1));
        assertEquals(Byte.valueOf((byte) 10), decorator.get((short) 1));
    }

    @Test
    public void testPutNullKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put(null, (byte) 10);

        assertTrue(decorator.containsKey(null));
        assertEquals(Byte.valueOf((byte) 10), decorator.get(null));
    }

    @Test
    public void testPutNullValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, null);

        assertTrue(decorator.containsKey((short) 1));
        assertNull(decorator.get((short) 1));
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte removedValue = decorator.remove((short) 1);

        assertEquals(Byte.valueOf((byte) 10), removedValue);
        assertFalse(decorator.containsKey((short) 1));
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte removedValue = decorator.remove(null);

        assertNull(removedValue);
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        map.put((short) 2, (byte) 20);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();

        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 1, (byte) 10)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 2, (byte) 20)));
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.clear();

        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey((short) 1));
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        assertTrue(decorator.containsValue((byte) 10));
        assertFalse(decorator.containsValue((byte) 20));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        map.put((short) 2, (byte) 20);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 10);
        map.put((short) 2, (byte) 20);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Map<Short, Byte> otherMap = new HashMap<>();
        otherMap.put((short) 3, (byte) 30);
        otherMap.put((short) 4, (byte) 40);

        decorator.putAll(otherMap);

        assertEquals(4, decorator.size());
        assertTrue(decorator.containsKey((short) 3));
        assertEquals(Byte.valueOf((byte) 30), decorator.get((short) 3));
        assertTrue(decorator.containsKey((short) 4));
        assertEquals(Byte.valueOf((byte) 40), decorator.get((short) 4));
    }

    @Test
    public void testWrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short wrappedKey = decorator.wrapKey((short) 1);

        assertEquals(Short.valueOf((short) 1), wrappedKey);
    }

    @Test
    public void testUnwrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        short unwrappedKey = decorator.unwrapKey(Short.valueOf((short) 1));

        assertEquals((short) 1, unwrappedKey);
    }

    @Test
    public void testWrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte wrappedValue = decorator.wrapValue((byte) 10);

        assertEquals(Byte.valueOf((byte) 10), wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        byte unwrappedValue = decorator.unwrapValue(Byte.valueOf((byte) 10));

        assertEquals((byte) 10, unwrappedValue);
    }
}
