
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;
    private TShortByteMap map;

    @Before
    public void setUp() {
        map = new TShortByteMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(Byte.valueOf((byte) 2), mapDecorator.get((short) 1));
    }

    @Test
    public void testPutNullKey() {
        mapDecorator.put(null, (byte) 2);
        assertEquals(Byte.valueOf((byte) 2), mapDecorator.get(null));
    }

    @Test
    public void testPutNullValue() {
        mapDecorator.put((short) 1, null);
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testGetInvalidKey() {
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testClear() {
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testRemove() {
        mapDecorator.put((short) 1, (byte) 2);
        Byte removedValue = mapDecorator.remove((short) 1);
        assertEquals(Byte.valueOf((byte) 2), removedValue);
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testRemoveInvalidKey() {
        assertNull(mapDecorator.remove((short) 1));
    }

    @Test
    public void testContainsValue() {
        mapDecorator.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsValue((byte) 2));
    }

    @Test
    public void testContainsInvalidValue() {
        assertFalse(mapDecorator.containsValue((byte) 2));
    }

    @Test
    public void testContainsKey() {
        mapDecorator.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsKey((short) 1));
    }

    @Test
    public void testContainsInvalidKey() {
        assertFalse(mapDecorator.containsKey((short) 1));
    }

    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short) 1, (byte) 2);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap sourceMap = new TShortByteMap();
        sourceMap.put((short) 1, (byte) 2);
        sourceMap.put((short) 3, (byte) 4);
        mapDecorator.putAll(sourceMap);
        assertEquals(Byte.valueOf((byte) 2), mapDecorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 4), mapDecorator.get((short) 3));
    }

    @Test
    public void testWrapAndUnwrapKey() {
        short key = 1;
        assertEquals((Short) key, mapDecorator.wrapKey(key));

        Short wrappedKey = 1;
        assertEquals(key, mapDecorator.unwrapKey(wrappedKey));
    }

    @Test
    public void testWrapAndUnwrapValue() {
        byte value = 2;
        assertEquals((Byte) value, mapDecorator.wrapValue(value));

        Byte wrappedValue = 2;
        assertEquals(value, mapDecorator.unwrapValue(wrappedValue));
    }

}
