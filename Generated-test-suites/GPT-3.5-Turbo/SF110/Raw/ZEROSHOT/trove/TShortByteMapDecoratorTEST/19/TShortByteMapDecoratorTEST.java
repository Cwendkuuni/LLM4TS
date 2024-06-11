import gnu.trove.decorator.TShortByteMapDecorator;
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
        mapDecorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPut() {
        assertNull(mapDecorator.put((short) 4, (byte) 4));
        assertEquals(4, mapDecorator.getMap().size());
        assertEquals((byte) 4, (byte) mapDecorator.getMap().get((short) 4));
        assertEquals((byte) 4, (byte) mapDecorator.put((short) 4, (byte) 5));
        assertEquals(4, mapDecorator.getMap().size());
        assertEquals((byte) 5, (byte) mapDecorator.getMap().get((short) 4));
    }

    @Test
    public void testGet() {
        assertEquals((byte) 1, (byte) mapDecorator.get((short) 1));
        assertEquals((byte) 2, (byte) mapDecorator.get((short) 2));
        assertEquals((byte) 3, (byte) mapDecorator.get((short) 3));
        assertNull(mapDecorator.get((short) 4));
    }

    @Test
    public void testClear() {
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        assertNull(mapDecorator.remove((short) 4));
        assertEquals(3, mapDecorator.getMap().size());
        assertEquals((byte) 1, (byte) mapDecorator.remove((short) 1));
        assertEquals(2, mapDecorator.getMap().size());
        assertNull(mapDecorator.getMap().get((short) 1));
    }

    @Test
    public void testContainsValue() {
        assertTrue(mapDecorator.containsValue((byte) 1));
        assertFalse(mapDecorator.containsValue((byte) 4));
    }

    @Test
    public void testContainsKey() {
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 4));
    }

    @Test
    public void testSize() {
        assertEquals(3, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new TShortByteMap();
        map.put((short) 4, (byte) 4);
        map.put((short) 5, (byte) 5);
        mapDecorator.putAll(map);
        assertEquals(5, mapDecorator.getMap().size());
        assertEquals((byte) 4, (byte) mapDecorator.getMap().get((short) 4));
        assertEquals((byte) 5, (byte) mapDecorator.getMap().get((short) 5));
    }

    // Additional tests for wrapKey, unwrapKey, wrapValue, unwrapValue methods
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