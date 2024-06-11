
package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapDecorator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator emptyMap;
    private TShortByteMapDecorator mapWithValues;

    @Before
    public void setUp() {
        TShortByteMap empty = new TShortByteMap();
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);
        emptyMap = new TShortByteMapDecorator(empty);
        mapWithValues = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPut() {
        assertNull(emptyMap.put(null, null));
        assertNull(mapWithValues.put(null, null));

        assertEquals((Byte) 1, emptyMap.put((short) 4, (byte) 1));
        assertEquals((Byte) 1, emptyMap.put((short) 4, (byte) 2));

        assertEquals((Byte) 1, mapWithValues.put((short) 1, (byte) 4));
        assertEquals((Byte) 2, mapWithValues.put((short) 2, (byte) 5));
    }

    @Test
    public void testGet() {
        assertNull(emptyMap.get(null));
        assertNull(mapWithValues.get(null));

        assertEquals((Byte) 1, emptyMap.get((short) 1));
        assertEquals((Byte) 2, emptyMap.get((short) 2));
        assertEquals(null, emptyMap.get((short) 3));

        assertEquals((Byte) 1, mapWithValues.get((short) 1));
        assertEquals((Byte) 2, mapWithValues.get((short) 2));
        assertEquals((Byte) 3, mapWithValues.get((short) 3));
        assertEquals(null, mapWithValues.get((short) 4));
    }

    @Test
    public void testClear() {
        emptyMap.clear();
        assertEquals(0, emptyMap.size());

        mapWithValues.clear();
        assertEquals(0, mapWithValues.size());
    }

    @Test
    public void testRemove() {
        assertNull(emptyMap.remove(null));
        assertNull(mapWithValues.remove(null));

        assertNull(emptyMap.remove((short) 1));

        assertEquals((Byte) 1, mapWithValues.remove((short) 1));
        assertEquals((Byte) 2, mapWithValues.remove((short) 2));
        assertEquals(null, mapWithValues.remove((short) 3));
    }

    @Test
    public void testEntrySet() {
        assertEquals(0, emptyMap.entrySet().size());

        assertEquals(3, mapWithValues.entrySet().size());

        Map<Short, Byte> expectedMap = new HashMap<>();
        expectedMap.put((short) 1, (byte) 1);
        expectedMap.put((short) 2, (byte) 2);
        expectedMap.put((short) 3, (byte) 3);

        assertEquals(expectedMap.entrySet(), mapWithValues.entrySet());
    }

    @Test
    public void testContainsValue() {
        assertFalse(emptyMap.containsValue(null));
        assertFalse(emptyMap.containsValue((byte) 1));

        assertTrue(mapWithValues.containsValue((byte) 1));
        assertFalse(mapWithValues.containsValue((byte) 4));
    }

    @Test
    public void testContainsKey() {
        assertFalse(emptyMap.containsKey(null));
        assertFalse(emptyMap.containsKey((short) 1));

        assertTrue(mapWithValues.containsKey((short) 1));
        assertFalse(mapWithValues.containsKey((short) 4));
    }

    @Test
    public void testSize() {
        assertEquals(0, emptyMap.size());

        assertEquals(3, mapWithValues.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(emptyMap.isEmpty());

        assertFalse(mapWithValues.isEmpty());
    }

    @Test
    public void testPutAll() {
        emptyMap.putAll(new HashMap<Short, Byte>() {{
            put((short) 4, (byte) 1);
            put((short) 5, (byte) 2);
        }});
        assertEquals(2, emptyMap.size());

        mapWithValues.putAll(new HashMap<Short, Byte>() {{
            put((short) 1, (byte) 4);
            put((short) 2, (byte) 5);
        }});
        assertEquals(3, mapWithValues.size());
    }

    @Test
    public void testWrapKey() {
        assertEquals((short) 1, emptyMap.wrapKey((short) 1));
        assertEquals((short) 2, emptyMap.wrapKey((short) 2));

        assertEquals((short) 1, mapWithValues.wrapKey((short) 1));
        assertEquals((short) 2, mapWithValues.wrapKey((short) 2));
        assertEquals((short) 3, mapWithValues.wrapKey((short) 3));
    }

    @Test
    public void testUnwrapKey() {
        assertEquals((short) 1, emptyMap.unwrapKey((short) 1));
        assertEquals((short) 2, emptyMap.unwrapKey((short) 2));

        assertEquals((short) 1, mapWithValues.unwrapKey((short) 1));
        assertEquals((short) 2, mapWithValues.unwrapKey((short) 2));
        assertEquals((short) 3, mapWithValues.unwrapKey((short) 3));
    }

    @Test
    public void testWrapValue() {
        assertEquals((byte) 1, emptyMap.wrapValue((byte) 1));
        assertEquals((byte) 2, emptyMap.wrapValue((byte) 2));

        assertEquals((byte) 1, mapWithValues.wrapValue((byte) 1));
        assertEquals((byte) 2, mapWithValues.wrapValue((byte) 2));
        assertEquals((byte) 3, mapWithValues.wrapValue((byte) 3));
    }

    @Test
    public void testUnwrapValue() {
        assertEquals((byte) 1, emptyMap.unwrapValue((byte) 1));
        assertEquals((byte) 2, emptyMap.unwrapValue((byte) 2));

        assertEquals((byte) 1, mapWithValues.unwrapValue((byte) 1));
        assertEquals((byte) 2, mapWithValues.unwrapValue((byte) 2));
        assertEquals((byte) 3, mapWithValues.unwrapValue((byte) 3));
    }
}
