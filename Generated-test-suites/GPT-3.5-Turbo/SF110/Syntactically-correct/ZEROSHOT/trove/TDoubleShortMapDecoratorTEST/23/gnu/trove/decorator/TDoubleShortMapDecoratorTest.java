import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        // Create a TDoubleShortMapDecorator object
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        // Put values
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        // Verify size
        assertEquals(3, map.size());

        // Get values
        Short value1 = map.get(1.0);
        assertEquals((short) 1, value1.shortValue());

        Short value2 = map.get(2.0);
        assertEquals((short) 2, value2.shortValue());

        Short value3 = map.get(3.0);
        assertEquals((short) 3, value3.shortValue());
    }

    @Test
    public void testRemove() {
        // Create a TDoubleShortMapDecorator object
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        // Put values
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        // Verify size
        assertEquals(3, map.size());

        // Remove a value
        Short removedValue = map.remove(2.0);
        assertEquals((short) 2, removedValue.shortValue());

        // Verify size after removal
        assertEquals(2, map.size());

        // Verify that removed key-value is no longer present in the map
        assertFalse(map.containsKey(2.0));
        assertNull(map.get(2.0));
    }

    @Test
    public void testClear() {
        // Create a TDoubleShortMapDecorator object
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        // Put values
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        // Verify size
        assertEquals(3, map.size());

        // Clear the map
        map.clear();

        // Verify size after clearing
        assertEquals(0, map.size());

        // Verify that all keys and values are removed
        assertFalse(map.containsKey(1.0));
        assertFalse(map.containsKey(2.0));
        assertFalse(map.containsKey(3.0));
        assertNull(map.get(1.0));
        assertNull(map.get(2.0));
        assertNull(map.get(3.0));
    }

    @Test
    public void testContainsKey() {
        // Create a TDoubleShortMapDecorator object
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        // Put values
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        // Verify containsKey for existing and non-existing keys
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsKey(2.0));
        assertTrue(map.containsKey(3.0));
        assertFalse(map.containsKey(4.0));
    }

    @Test
    public void testContainsValue() {
        // Create a TDoubleShortMapDecorator object
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        // Put values
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        // Verify containsValue for existing and non-existing values
        assertTrue(map.containsValue((short) 1));
        assertTrue(map.containsValue((short) 2));
        assertTrue(map.containsValue((short) 3));
        assertFalse(map.containsValue((short) 4));
    }

    @Test
    public void testSize() {
        // Create a TDoubleShortMapDecorator object
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        // Put values
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        // Verify size
        assertEquals(3, map.size());
    }

    @Test
    public void testPutAll() {
        // Create a TDoubleShortMapDecorator object
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        // Create a HashMap
        Map<Double, Short> hashMap = new HashMap<>();
        hashMap.put(1.0, (short) 1);
        hashMap.put(2.0, (short) 2);

        // PutAll from HashMap to TDoubleShortMapDecorator
        map.putAll(hashMap);

        // Verify size and values
        assertEquals(2, map.size());
        assertEquals((short) 1, map.get(1.0).shortValue());
        assertEquals((short) 2, map.get(2.0).shortValue());
    }

    @Test
    public void testEntrySet() {
        // Create a TDoubleShortMapDecorator object
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        // Put values
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        // Get EntrySet
        Set<Entry<Double, Short>> entrySet = map.entrySet();

        // Verify size
        assertEquals(3, entrySet.size());

        // Verify EntrySet contains correct key-value pairs
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 1)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2.0, (short) 2)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(3.0, (short) 3)));
    }
}