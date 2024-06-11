
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertNull(mapDecorator.put((short) 1, (byte) 1));

        mapDecorator.put((short) 2, (byte) 2);
        assertTrue(mapDecorator.containsKey((short) 2));
        assertEquals((byte) 2, (byte) mapDecorator.get((short) 2));
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertNull(mapDecorator.get((short) 1));

        mapDecorator.put((short) 1, (byte) 1);
        assertEquals((byte) 1, (byte) mapDecorator.get((short) 1));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 1);

        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertNull(mapDecorator.remove((short) 1));

        mapDecorator.put((short) 1, (byte) 1);
        assertEquals((byte) 1, (byte) mapDecorator.remove((short) 1));
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals(0, mapDecorator.entrySet().size());

        mapDecorator.put((short) 1, (byte) 1);
        assertEquals(1, mapDecorator.entrySet().size());
        assertTrue(mapDecorator.entrySet().contains(Map.entry((short) 1, (byte) 1)));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertFalse(mapDecorator.containsValue((byte) 1));

        mapDecorator.put((short) 1, (byte) 1);
        assertTrue(mapDecorator.containsValue((byte) 1));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertFalse(mapDecorator.containsKey((short) 1));

        mapDecorator.put((short) 1, (byte) 1);
        assertTrue(mapDecorator.containsKey((short) 1));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals(0, mapDecorator.size());

        mapDecorator.put((short) 1, (byte) 1);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertTrue(mapDecorator.isEmpty());

        mapDecorator.put((short) 1, (byte) 1);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);

        mapDecorator.putAll(map);
        assertTrue(mapDecorator.containsKey((short) 1));
        assertTrue(mapDecorator.containsKey((short) 2));
    }
}
