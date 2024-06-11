import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;
    private TDoubleShortMap underlyingMap;

    @Before
    public void setup() {
        underlyingMap = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(underlyingMap);
    }

    @Test
    public void testPut_typical() {
        // Typical case: Adding a new key-value pair
        Double key = 10.0;
        Short value = 5;

        assertNull(mapDecorator.put(key, value));
        assertTrue(mapDecorator.containsKey(key));
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testPut_duplicateKey() {
        // Error scenario: Adding a duplicate key
        Double key = 10.0;
        Short value1 = 5;
        Short value2 = 7;

        assertNull(mapDecorator.put(key, value1));
        assertEquals(value1, mapDecorator.put(key, value2));
        assertTrue(mapDecorator.containsKey(key));
        assertEquals(value2, mapDecorator.get(key));
    }

    @Test
    public void testPut_nullKey() {
        // Error scenario: Adding a null key
        Short value = 5;

        assertNull(mapDecorator.put(null, value));
        assertTrue(mapDecorator.containsKey(null));
        assertEquals(value, mapDecorator.get(null));
    }

    @Test
    public void testPut_nullValue() {
        // Typical case: Adding a null value
        Double key = 10.0;

        assertNull(mapDecorator.put(key, null));
        assertTrue(mapDecorator.containsKey(key));
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testGet_typical() {
        // Typical case: Getting a value for a key that exists
        Double key = 10.0;
        Short value = 5;

        underlyingMap.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testGet_keyNotPresent() {
        // Typical case: Getting a value for a key that does not exist
        Double key = 10.0;

        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testGet_nullKey() {
        // Error scenario: Getting a value with a null key
        Short value = 5;

        underlyingMap.put(null, value);
        assertEquals(value, mapDecorator.get(null));
    }

    @Test
    public void testClear() {
        // Typical case: Clearing the map
        Double key = 10.0;
        Short value = 5;

        underlyingMap.put(key, value);
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove_typical() {
        // Typical case: Removing a key-value pair that exists
        Double key = 10.0;
        Short value = 5;

        underlyingMap.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        assertFalse(mapDecorator.containsKey(key));
    }

    @Test
    public void testRemove_keyNotPresent() {
        // Typical case: Removing a key-value pair that does not exist
        Double key = 10.0;

        assertNull(mapDecorator.remove(key));
        assertFalse(mapDecorator.containsKey(key));
    }

    @Test
    public void testRemove_nullKey() {
        // Error scenario: Removing a key-value pair with a null key
        Short value = 5;

        underlyingMap.put(null, value);
        assertEquals(value, mapDecorator.remove(null));
        assertFalse(mapDecorator.containsKey(null));
    }

    @Test
    public void testContainsValue_typical() {
        // Typical case: Checking if the map contains a specific value
        Double key = 10.0;
        Short value = 5;

        underlyingMap.put(key, value);
        assertTrue(mapDecorator.containsValue(value));
    }

    @Test
    public void testContainsValue_valueNotPresent() {
        // Typical case: Checking if the map contains a value that does not exist
        Double key = 10.0;
        Short value = 5;

        underlyingMap.put(key, value);
        assertFalse(mapDecorator.containsValue((short) 7));
    }

    @Test
    public void testContainsValue_nullValue() {
        // Error scenario: Checking if the map contains a null value
        Double key = 10.0;

        underlyingMap.put(key, null);
        assertTrue(mapDecorator.containsValue(null));
    }

    @Test
    public void testContainsKey_typical() {
        // Typical case: Checking if the map contains a specific key
        Double key = 10.0;
        Short value = 5;

        underlyingMap.put(key, value);
        assertTrue(mapDecorator.containsKey(key));
    }

    @Test
    public void testContainsKey_keyNotPresent() {
        // Typical case: Checking if the map contains a key that does not exist
        Double key = 10.0;

        assertFalse(mapDecorator.containsKey(key));
    }

    @Test
    public void testContainsKey_nullKey() {
        // Error scenario: Checking if the map contains a null key
        Short value = 5;

        underlyingMap.put(null, value);
        assertTrue(mapDecorator.containsKey(null));
    }

    @Test
    public void testSize() {
        // Typical case: Getting the size of the map
        Double key1 = 10.0;
        Double key2 = 20.0;
        Short value = 5;

        assertEquals(0, mapDecorator.size());
        underlyingMap.put(key1, value);
        assertEquals(1, mapDecorator.size());
        underlyingMap.put(key2, value);
        assertEquals(2, mapDecorator.size());
        mapDecorator.remove(key1);
        assertEquals(1, mapDecorator.size());
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        // Typical case: Checking if the map is empty
        Double key = 10.0;
        Short value = 5;

        assertTrue(mapDecorator.isEmpty());
        underlyingMap.put(key, value);
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        // Typical case: Putting all key-value pairs from another map
        Double key1 = 10.0;
        Double key2 = 20.0;
        Short value1 = 5;
        Short value2 = 7;

        TDoubleShortHashMap otherMap = new TDoubleShortHashMap();
        otherMap.put(key1, value1);
        otherMap.put(key2, value2);

        mapDecorator.putAll(otherMap);

        assertTrue(mapDecorator.containsKey(key1));
        assertEquals(value1, mapDecorator.get(key1));
        assertTrue(mapDecorator.containsKey(key2));
        assertEquals(value2, mapDecorator.get(key2));
    }

    @Test
    public void testWrapKey() {
        // Typical case: Wrapping a double key
        double key = 10.0;

        assertEquals(Double.valueOf(key), mapDecorator.wrapKey(key));
    }

    @Test
    public void testUnwrapKey() {
        // Typical case: Unwrapping a double key
        double key = 10.0;

        assertEquals(key, mapDecorator.unwrapKey(Double.valueOf(key)), 0.0);
    }

    @Test
    public void testWrapValue() {
        // Typical case: Wrapping a short value
        short value = 5;

        assertEquals(Short.valueOf(value), mapDecorator.wrapValue(value));
    }

    @Test
    public void testUnwrapValue() {
        // Typical case: Unwrapping a short value
        short value = 5;

        assertEquals(value, mapDecorator.unwrapValue(Short.valueOf(value)));
    }

    @Test
    public void testReadExternal() {
        // Skip testing as it is dependent on external factors
    }

    @Test
    public void testWriteExternal() {
        // Skip testing as it is dependent on external factors
    }
}