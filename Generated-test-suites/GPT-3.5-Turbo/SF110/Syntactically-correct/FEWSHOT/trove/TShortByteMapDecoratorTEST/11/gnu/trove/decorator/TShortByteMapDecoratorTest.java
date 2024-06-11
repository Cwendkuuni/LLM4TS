package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.hash.TShortByteHashMap;


public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;
    private TShortByteMap map;

    @Before
    public void setUp() {
        map = new TShortByteHashMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        assertNull(mapDecorator.put((short) 1, (byte) 2));
        assertEquals(2, mapDecorator.get((short) 1).byteValue());
    }

    @Test
    public void testPutWithNullKey() {
        assertNull(mapDecorator.put(null, (byte) 2));
        assertEquals(2, mapDecorator.get(null).byteValue());
    }

    @Test
    public void testPutWithNullValue() {
        assertNull(mapDecorator.put((short) 1, null));
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testPutAndGetMultipleEntries() {
        assertNull(mapDecorator.put((short) 1, (byte) 2));
        assertNull(mapDecorator.put((short) 3, (byte) 4));
        assertEquals(2, mapDecorator.get((short) 1).byteValue());
        assertEquals(4, mapDecorator.get((short) 3).byteValue());
    }

    @Test
    public void testClear() {
        map.put((short) 1, (byte) 2);
        assertFalse(map.isEmpty());
        mapDecorator.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        map.put((short) 1, (byte) 2);
        assertEquals(2, mapDecorator.remove((short) 1).byteValue());
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemoveWithNullKey() {
        map.put(null, (byte) 2);
        assertEquals(2, mapDecorator.remove(null).byteValue());
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        map.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsValue((byte) 2));
        assertFalse(mapDecorator.containsValue((byte) 3));
    }

    @Test
    public void testContainsKey() {
        map.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        map.put((short) 1, (byte) 2);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        map.put((short) 1, (byte) 2);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator otherMapDecorator = new TShortByteMapDecorator();
        otherMapDecorator.put((short) 1, (byte) 2);
        otherMapDecorator.put((short) 3, (byte) 4);
        
        mapDecorator.putAll(otherMapDecorator);
        
        assertEquals(2, mapDecorator.size());
        assertEquals(2, mapDecorator.get((short) 1).byteValue());
        assertEquals(4, mapDecorator.get((short) 3).byteValue());
    }

}