import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;

public class TShortByteMapDecoratorTest extends TestCase {

    private TShortByteMapDecorator mapDecorator;

    public void setUp(){
        mapDecorator = new TShortByteMapDecorator();
    }

    public void tearDown(){
        mapDecorator.clear();
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put((short) 1, (byte) 1);
        assertEquals(new Byte((byte) 1), mapDecorator.get((short) 1));

        mapDecorator.put((short) 2, (byte) 2);
        assertEquals(new Byte((byte) 2), mapDecorator.get((short) 2));
    }

    @Test
    public void testPutAndGetNullKey() {
        mapDecorator.put(null, (byte) 1);
        assertEquals(new Byte((byte) 1), mapDecorator.get(null));
    }

    @Test
    public void testPutAndGetNullValue() {
        mapDecorator.put((short) 1, null);
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testRemove() {
        mapDecorator.put((short) 1, (byte) 1);
        mapDecorator.put((short) 2, (byte) 2);

        mapDecorator.remove((short) 1);
        assertFalse(mapDecorator.containsKey((short) 1));
        assertNull(mapDecorator.get((short) 1));

        mapDecorator.remove((short) 2);
        assertFalse(mapDecorator.containsKey((short) 2));
        assertNull(mapDecorator.get((short) 2));
    }

    @Test
    public void testRemoveNullKey() {
        mapDecorator.put(null, (byte) 1);
        mapDecorator.remove(null);
        assertFalse(mapDecorator.containsKey(null));
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testContainsKey() {
        assertFalse(mapDecorator.containsKey((short) 1));

        mapDecorator.put((short) 1, (byte) 1);
        assertTrue(mapDecorator.containsKey((short) 1));

        mapDecorator.put(null, (byte) 1);
        assertTrue(mapDecorator.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        assertFalse(mapDecorator.containsValue((byte) 1));

        mapDecorator.put((short) 1, (byte) 1);
        assertTrue(mapDecorator.containsValue((byte) 1));

        mapDecorator.put((short) 2, null);
        assertTrue(mapDecorator.containsValue(null));
    }

    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());

        mapDecorator.put((short) 1, (byte) 1);
        assertEquals(1, mapDecorator.size());

        mapDecorator.put((short) 2, (byte) 2);
        assertEquals(2, mapDecorator.size());

        mapDecorator.remove((short) 1);
        assertEquals(1, mapDecorator.size());

        mapDecorator.remove((short) 2);
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testClear() {
        mapDecorator.put((short) 1, (byte) 1);
        mapDecorator.put((short) 2, (byte) 2);

        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertFalse(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        assertTrue(entrySet.isEmpty());

        mapDecorator.put((short) 1, (byte) 1);
        mapDecorator.put((short) 2, (byte) 2);

        entrySet = mapDecorator.entrySet();
        assertEquals(2, entrySet.size());

        for (Map.Entry<Short, Byte> entry : entrySet) {
            Short key = entry.getKey();
            Byte value = entry.getValue();
            assertTrue(mapDecorator.containsKey(key));
            assertEquals(mapDecorator.get(key), value);
        }
    }

    @Test
    public void testEquals() {
        TShortByteMapDecorator map1 = new TShortByteMapDecorator();
        TShortByteMapDecorator map2 = new TShortByteMapDecorator();

        assertEquals(map1, map2);

        map1.put((short) 1, (byte) 1);
        assertNotEquals(map1, map2);

        map2.put((short) 1, (byte) 1);
        assertEquals(map1, map2);
    }

    @Test
    public void testHashCode() {
        TShortByteMapDecorator map1 = new TShortByteMapDecorator();
        TShortByteMapDecorator map2 = new TShortByteMapDecorator();

        assertEquals(map1.hashCode(), map2.hashCode());

        map1.put((short) 1, (byte) 1);
        assertNotEquals(map1.hashCode(), map2.hashCode());

        map2.put((short) 1, (byte) 1);
        assertEquals(map1.hashCode(), map2.hashCode());
    }

}