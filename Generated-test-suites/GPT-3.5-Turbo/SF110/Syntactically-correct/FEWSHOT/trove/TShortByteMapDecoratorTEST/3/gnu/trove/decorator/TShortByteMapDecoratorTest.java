package gnu.trove.decorator;

import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setUp() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);
        map.put((short) 4, (byte) 4);
        mapDecorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPut() {
        assertNull(mapDecorator.put((short) 5, (byte) 5));
        assertEquals((byte) 5, (byte) mapDecorator.getMap().get((short) 5));
        assertEquals(5, mapDecorator.size());
    }

    @Test
    public void testPutWithNullKey() {
        assertNull(mapDecorator.put(null, (byte) 1));
        assertEquals((byte) 1, (byte) mapDecorator.getMap().getNoEntryKey());
        assertEquals(5, mapDecorator.size());
    }

    @Test
    public void testPutWithNullValue() {
        assertNull(mapDecorator.put((short) 1, null));
        assertEquals((byte) 0, (byte) mapDecorator.getMap().get((short) 1));
        assertEquals(4, mapDecorator.size());
    }

    @Test
    public void testGet() {
        assertNull(mapDecorator.get((short) 0));
        assertEquals((byte) 1, (byte) mapDecorator.get((short) 1));
        assertEquals((byte) 4, (byte) mapDecorator.get((short) 4));
    }

    @Test
    public void testGetWithNullKey() {
        assertEquals((byte) 0, (byte) mapDecorator.get(null));
    }

    @Test
    public void testClear() {
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertTrue(mapDecorator.isEmpty());
        assertNull(mapDecorator.getMap().get((short) 1));
    }

    @Test
    public void testRemove() {
        assertNull(mapDecorator.remove((short) 0));
        assertEquals((byte) 1, (byte) mapDecorator.remove((short) 1));
        assertEquals(3, mapDecorator.size());
        assertFalse(mapDecorator.containsKey((short) 1));
    }

    @Test
    public void testRemoveWithNullKey() {
        assertNull(mapDecorator.remove(null));
        assertEquals((byte) 0, (byte) mapDecorator.getMap().getNoEntryKey());
        assertEquals(3, mapDecorator.size());
    }

    @Test
    public void testContainsValue() {
        assertFalse(mapDecorator.containsValue(null));
        assertFalse(mapDecorator.containsValue((byte) 0));
        assertTrue(mapDecorator.containsValue((byte) 1));
        assertTrue(mapDecorator.containsValue((byte) 4));
    }

    @Test
    public void testContainsKey() {
        assertFalse(mapDecorator.containsKey(null));
        assertFalse(mapDecorator.containsKey((short) 0));
        assertTrue(mapDecorator.containsKey((short) 1));
        assertTrue(mapDecorator.containsKey((short) 4));
    }

    @Test
    public void testSize() {
        assertEquals(4, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator anotherMapDecorator = new TShortByteMapDecorator(new TShortByteMap());
        anotherMapDecorator.put((short) 5, (byte) 5);
        anotherMapDecorator.put((short) 6, (byte) 6);

        mapDecorator.putAll(anotherMapDecorator);

        assertEquals((byte) 5, (byte) mapDecorator.getMap().get((short) 5));
        assertEquals((byte) 6, (byte) mapDecorator.getMap().get((short) 6));
        assertEquals(6, mapDecorator.size());
    }

    @Test
    public void testWrapKey() {
        assertEquals((short) 1, (short) mapDecorator.wrapKey((short) 1));
    }

    @Test
    public void testUnwrapKey() {
        assertEquals((short) 1, mapDecorator.unwrapKey((short) 1));
    }

    @Test
    public void testWrapValue() {
        assertEquals((byte) 1, (byte) mapDecorator.wrapValue((byte) 1));
    }

    @Test
    public void testUnwrapValue() {
        assertEquals((byte) 1, mapDecorator.unwrapValue((byte) 1));
    }
}