package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setup() {
        mapDecorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.get((short) 1));

        mapDecorator.put((short) 2, (byte) 20);
        assertEquals(Byte.valueOf((byte) 20), mapDecorator.get((short) 2));
    }

    @Test
    public void testPutNullKeyAndValue() {
        mapDecorator.put(null, null);
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testPutNullKey() {
        mapDecorator.put(null, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.get(null));
    }

    @Test
    public void testPutNullValue() {
        mapDecorator.put((short) 1, null);
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testRemove() {
        mapDecorator.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.remove((short) 1));
    }

    @Test
    public void testRemoveNonExistingKey() {
        assertNull(mapDecorator.remove((short) 2));
    }

    @Test
    public void testClear() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testContainsKey() {
        mapDecorator.put((short) 1, (byte) 10);
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testContainsValue() {
        mapDecorator.put((short) 1, (byte) 10);
        assertTrue(mapDecorator.containsValue((byte) 10));
        assertFalse(mapDecorator.containsValue((byte) 20));
    }

    @Test
    public void testSize() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short) 1, (byte) 10);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testEntrySet() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();

        assertNotNull(entrySet);
        assertEquals(2, entrySet.size());

        for (Map.Entry<Short, Byte> entry : entrySet) {
            Short key = entry.getKey();
            Byte value = entry.getValue();

            assertNotNull(key);
            assertNotNull(value);

            assertTrue(key.equals((short) 1) || key.equals((short) 2));
            assertTrue(value.equals((byte) 10) || value.equals((byte) 20));
        }
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new TShortByteHashMap();
        map.put((short) 1, (byte) 10);
        map.put((short) 2, (byte) 20);

        mapDecorator.putAll(map);

        assertEquals(Byte.valueOf((byte) 10), mapDecorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 20), mapDecorator.get((short) 2));
    }

    @Test
    public void testWrapKey() {
        assertEquals(Short.valueOf((short) 1), mapDecorator.wrapKey((short) 1));
    }

    @Test
    public void testUnwrapKey() {
        assertEquals((short) 1, mapDecorator.unwrapKey(Short.valueOf((short) 1)));
    }

    @Test
    public void testWrapValue() {
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.wrapValue((byte) 10));
    }

    @Test
    public void testUnwrapValue() {
        assertEquals((byte) 10, mapDecorator.unwrapValue(Byte.valueOf((byte) 10)));
    }
}