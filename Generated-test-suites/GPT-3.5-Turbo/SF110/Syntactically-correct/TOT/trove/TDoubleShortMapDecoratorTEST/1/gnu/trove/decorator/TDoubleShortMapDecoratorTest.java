import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.map.*;
import gnu.trove.iterator.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        TDoubleShortMap tDoubleShortMap = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(tDoubleShortMap);
    }

    @Test
    public void testPut() {
        // Typical case
        Double key = 5.0;
        Short value = 10;
        Short expected = 10;
        Short actual = mapDecorator.put(key, value);
        assertEquals(expected, actual);

        // Same key with different value
        key = 5.0;
        value = 20;
        expected = 10;
        actual = mapDecorator.put(key, value);
        assertEquals(expected, actual);

        // Null key
        key = null;
        value = 10;
        expected = null;
        actual = mapDecorator.put(key, value);
        assertEquals(expected, actual);

        // Null value
        key = 10.0;
        value = null;
        expected = null;
        actual = mapDecorator.put(key, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        // Typical case
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        Short expected = 10;
        Short actual = mapDecorator.get(key);
        assertEquals(expected, actual);

        // Non-existing key
        key = 10.0;
        expected = null;
        actual = mapDecorator.get(key);
        assertEquals(expected, actual);

        // Null key
        key = null;
        expected = null;
        actual = mapDecorator.get(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testClear() {
        // Typical case
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        // Typical case
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        Short expected = 10;
        Short actual = mapDecorator.remove(key);
        assertEquals(expected, actual);

        // Non-existing key
        key = 10.0;
        expected = null;
        actual = mapDecorator.remove(key);
        assertEquals(expected, actual);

        // Null key
        key = null;
        expected = null;
        actual = mapDecorator.remove(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testEntrySet() {
        // Typical case
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        int expectedSize = 1;
        int actualSize = mapDecorator.entrySet().size();
        assertEquals(expectedSize, actualSize);

        // Non-existing entry
        key = 10.0;
        value = 20;
        assertFalse(mapDecorator.entrySet().contains(new SimpleEntry<>(key, value)));

        // Existing entry
        key = 5.0;
        value = 10;
        assertTrue(mapDecorator.entrySet().contains(new SimpleEntry<>(key, value)));

        // Null key
        key = null;
        value = 10;
        assertFalse(mapDecorator.entrySet().contains(new SimpleEntry<>(key, value)));
    }

    @Test
    public void testContainsValue() {
        // Typical case
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsValue((short) 10));

        // Non-existing value
        assertFalse(mapDecorator.containsValue((short) 20));

        // Null value
        assertFalse(mapDecorator.containsValue(null));
    }

    @Test
    public void testContainsKey() {
        // Typical case
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsKey(5.0));
        
        // Non-existing key
        assertFalse(mapDecorator.containsKey(10.0));

        // Null key
        assertFalse(mapDecorator.containsKey(null));
    }

    @Test
    public void testSize() {
        // Typical case
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        int expectedSize = 1;
        int actualSize = mapDecorator.size();
        assertEquals(expectedSize, actualSize);

        // Empty map
        mapDecorator.clear();
        expectedSize = 0;
        actualSize = mapDecorator.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testIsEmpty() {
        // Non-empty map
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        assertFalse(mapDecorator.isEmpty());

        // Empty map
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        // Typical case
        TDoubleShortMapHashMap map = new TDoubleShortMapHashMap();
        map.put(1.0, (short) 2);
        map.put(3.0, (short) 4);

        mapDecorator.putAll(map);

        assertTrue(mapDecorator.containsKey(1.0));
        assertTrue(mapDecorator.containsKey(3.0));
    }

    @Test
    public void testWrapKey() {
        // Typical case
        double key = 5.0;
        Double expected = 5.0;
        Double actual = mapDecorator.wrapKey(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testUnwrapKey() {
        // Typical case
        Double key = 5.0;
        double expected = 5.0;
        double actual = mapDecorator.unwrapKey(key);
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testWrapValue() {
        // Typical case
        short value = 10;
        Short expected = 10;
        Short actual = mapDecorator.wrapValue(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testUnwrapValue() {
        // Typical case
        Short value = 10;
        short expected = 10;
        short actual = mapDecorator.unwrapValue(value);
        assertEquals(expected, actual);
    }
}