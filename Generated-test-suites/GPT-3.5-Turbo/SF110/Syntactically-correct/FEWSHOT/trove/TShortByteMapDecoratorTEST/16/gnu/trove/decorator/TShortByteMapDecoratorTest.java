package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

     @Test
    public void testPutAndGet() {
        TShortByteMap map = new TShortByteMapDecorator();
        assertNull(map.put(null, null));
        assertNull(map.get(null));

        assertEquals((byte) 1, (byte) map.put((short) 1, (byte) 1));
        assertEquals((byte) 1, (byte) map.get((short) 1));

        assertEquals((byte) 2, (byte) map.put((short) 2, (byte) 2));
        assertEquals((byte) 2, (byte) map.get((short) 2));
        
        assertNull(map.put(null, (byte) 3));
        assertEquals((byte) 3, (byte) map.get(null));

    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);

        assertEquals(2, map.size());
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);

        assertEquals((byte) 1, (byte) map.remove((short) 1));
        assertNull(map.remove(null));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);

        assertEquals(true, map.containsKey((short) 1));
        assertEquals(false, map.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);

        assertEquals(true, map.containsValue((byte) 1));
        assertEquals(false, map.containsValue(null));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMapDecorator();
        assertEquals(0, map.size());

        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);

        assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMapDecorator();
        assertEquals(true, map.isEmpty());

        map.put((short) 1, (byte) 1);

        assertEquals(false, map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);

        TShortByteMap map2 = new TShortByteMapDecorator();
        map2.put((short) 3, (byte) 3);

        map.putAll(map2);

        assertEquals(3, map.size());
        assertEquals((byte) 3, (byte) map.get((short) 3));
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMapDecorator();
        assertEquals(0, map.entrySet().size());

        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);

        assertEquals(2, map.entrySet().size());
    }
}