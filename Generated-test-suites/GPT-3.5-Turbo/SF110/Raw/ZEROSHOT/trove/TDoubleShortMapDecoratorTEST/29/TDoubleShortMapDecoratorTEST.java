import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        Double key1 = 1.0;
        Short value1 = 1;
        Double key2 = 2.0;
        Short value2 = 2;

        // Put key1-value1 pair
        assertNull(mapDecorator.put(key1, value1));
        // Put key2-value2 pair
        assertNull(mapDecorator.put(key2, value2));

        // Get value1 using key1
        assertEquals(value1, mapDecorator.get(key1));
        // Get value2 using key2
        assertEquals(value2, mapDecorator.get(key2));
    }

    @Test
    public void testPutNullKey() {
        Short value = 1;

        // Put null key-value pair
        assertNull(mapDecorator.put(null, value));

        // Get value using null key
        assertEquals(value, mapDecorator.get(null));
    }

    @Test
    public void testPutNullValue() {
        Double key = 1.0;

        // Put key-null value pair
        assertNull(mapDecorator.put(key, null));

        // Get null value using key
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testRemove() {
        Double key1 = 1.0;
        Short value1 = 1;
        Double key2 = 2.0;
        Short value2 = 2;

        // Put key1-value1 pair
        assertNull(mapDecorator.put(key1, value1));
        // Put key2-value2 pair
        assertNull(mapDecorator.put(key2, value2));

        // Remove key1-value1 pair
        assertEquals(value1, mapDecorator.remove(key1));
        // Remove key2-value2 pair
        assertEquals(value2, mapDecorator.remove(key2));

        // Check if the map is empty
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testClear() {
        Double key = 1.0;
        Short value = 1;

        // Put key-value pair
        assertNull(mapDecorator.put(key, value));

        // Clear the map
        mapDecorator.clear();

        // Check if the map is empty
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        Double key = 1.0;
        Short value = 1;

        // Put key-value pair
        assertNull(mapDecorator.put(key, value));

        // Check if the map contains the key
        assertTrue(mapDecorator.containsKey(key));
        // Check if the map contains a non-existing key
        assertFalse(mapDecorator.containsKey(2.0));
    }

    @Test
    public void testContainsValue() {
        Double key = 1.0;
        Short value = 1;

        // Put key-value pair
        assertNull(mapDecorator.put(key, value));

        // Check if the map contains the value
        assertTrue(mapDecorator.containsValue(value));
        // Check if the map contains a non-existing value
        assertFalse(mapDecorator.containsValue(2));
    }

    @Test
    public void testSize() {
        Double key1 = 1.0;
        Short value1 = 1;
        Double key2 = 2.0;
        Short value2 = 2;
        Double key3 = 3.0;
        Short value3 = 3;

        // Check if the map is empty
        assertEquals(0, mapDecorator.size());

        // Put key1-value1 pair
        assertNull(mapDecorator.put(key1, value1));
        // Put key2-value2 pair
        assertNull(mapDecorator.put(key2, value2));

        // Check if the size is correct after adding key-value pairs
        assertEquals(2, mapDecorator.size());

        // Put key3-value3 pair
        assertNull(mapDecorator.put(key3, value3));

        // Check if the size is correct after adding another key-value pair
        assertEquals(3, mapDecorator.size());

        // Remove key2-value2 pair
        assertEquals(value2, mapDecorator.remove(key2));

        // Check if the size is correct after removing a key-value pair
        assertEquals(2, mapDecorator.size());

        // Clear the map
        mapDecorator.clear();

        // Check if the size is correct after clearing the map
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator sourceMapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 1;
        Double key2 = 2.0;
        Short value2 = 2;
        Double key3 = 3.0;
        Short value3 = 3;

        // Put key-value pairs in the source map decorator
        assertNull(sourceMapDecorator.put(key1, value1));
        assertNull(sourceMapDecorator.put(key2, value2));

        // Put all key-value pairs from the source map decorator to the target map decorator
        mapDecorator.putAll(sourceMapDecorator);

        // Check if the target map decorator contains the same key-value pairs as the source map decorator
        assertEquals(value1, mapDecorator.get(key1));
        assertEquals(value2, mapDecorator.get(key2));

        // Put another key-value pair in the source map decorator
        assertNull(sourceMapDecorator.put(key3, value3));

        // Put all key-value pairs from the source map decorator to the target map decorator
        mapDecorator.putAll(sourceMapDecorator);

        // Check if the target map decorator contains the newly added key-value pair
        assertEquals(value3, mapDecorator.get(key3));
    }

    // TODO: Add more test cases for other methods

}