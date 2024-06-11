import gnu.trove.map.TDoubleShortMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator doubleShortMapDecorator;

    @Before
    public void setUp() {
        TDoubleShortMap troveMap = new TDoubleShortHashMap();
        doubleShortMapDecorator = new TDoubleShortMapDecorator(troveMap);
    }

    @Test
    public void testPutAndGet_NormalCase() {
        // Putting a normal key-value pair
        Double key = 1.0;
        Short value = 2;
        Short result = doubleShortMapDecorator.put(key, value);

        // Check if the value returned matches the value we put
        Assert.assertEquals(value, result);

        // Check if the value can be retrieved correctly
        Assert.assertEquals(value, doubleShortMapDecorator.get(key));
    }

    @Test
    public void testPutAndGet_NullKeyAndValue() {
        // Putting a null key-value pair
        Double key = null;
        Short value = null;
        Short result = doubleShortMapDecorator.put(key, value);

        // Check if the value returned is null
        Assert.assertNull(result);

        // Check if the value can be retrieved correctly
        Assert.assertNull(doubleShortMapDecorator.get(key));
    }

    @Test
    public void testPutAndGet_NoEntryKeyAndValue() {
        // Putting a key-value pair with NoEntryKey and NoEntryValue
        Double key = doubleShortMapDecorator.getMap().getNoEntryKey();
        Short value = doubleShortMapDecorator.getMap().getNoEntryValue();
        Short result = doubleShortMapDecorator.put(key, value);

        // Check if the value returned matches the NoEntryValue we put
        Assert.assertEquals(value, result);

        // Check if the value can be retrieved correctly
        Assert.assertEquals(value, doubleShortMapDecorator.get(key));
    }

    @Test
    public void testRemove_NormalCase() {
        // Put a key-value pair
        Double key = 1.0;
        Short value = 2;
        doubleShortMapDecorator.put(key, value);

        // Remove the key-value pair
        Short result = doubleShortMapDecorator.remove(key);

        // Check if the value returned matches the value we put
        Assert.assertEquals(value, result);

        // Check if the key-value pair is removed from the map
        Assert.assertNull(doubleShortMapDecorator.get(key));
    }

    @Test
    public void testRemove_NotExistingKey() {
        // Try to remove a key-value pair that does not exist
        Double key = 1.0;
        Short result = doubleShortMapDecorator.remove(key);

        // Check if the value returned is null
        Assert.assertNull(result);
    }

    @Test
    public void testSize() {
        // Check if the size is initially 0
        Assert.assertEquals(0, doubleShortMapDecorator.size());

        // Put some key-value pairs
        doubleShortMapDecorator.put(1.0, (short) 2);
        doubleShortMapDecorator.put(2.0, (short) 3);
        doubleShortMapDecorator.put(3.0, (short) 4);

        // Check if the size is updated correctly
        Assert.assertEquals(3, doubleShortMapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        // Check if the map is initially empty
        Assert.assertTrue(doubleShortMapDecorator.isEmpty());

        // Put a key-value pair
        doubleShortMapDecorator.put(1.0, (short) 2);

        // Check if the map is not empty anymore
        Assert.assertFalse(doubleShortMapDecorator.isEmpty());
    }

    @Test
    public void testClear() {
        // Put some key-value pairs
        doubleShortMapDecorator.put(1.0, (short) 2);
        doubleShortMapDecorator.put(2.0, (short) 3);
        doubleShortMapDecorator.put(3.0, (short) 4);

        // Clear the map
        doubleShortMapDecorator.clear();

        // Check if the map is empty
        Assert.assertTrue(doubleShortMapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        // Put a key-value pair
        doubleShortMapDecorator.put(1.0, (short) 2);

        // Check if the map contains the key we put
        Assert.assertTrue(doubleShortMapDecorator.containsKey(1.0));

        // Check if the map does not contain a non-existing key
        Assert.assertFalse(doubleShortMapDecorator.containsKey(2.0));
    }

    @Test
    public void testContainsValue() {
        // Put a key-value pair
        doubleShortMapDecorator.put(1.0, (short) 2);

        // Check if the map contains the value we put
        Assert.assertTrue(doubleShortMapDecorator.containsValue((short) 2));

        // Check if the map does not contain a non-existing value
        Assert.assertFalse(doubleShortMapDecorator.containsValue((short) 3));
    }

    @Test
    public void testPutAll() {
        // Create a new map
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 2);
        map.put(2.0, (short) 3);
        map.put(3.0, (short) 4);

        // Put all key-value pairs from the new map to the decorator map
        doubleShortMapDecorator.putAll(map);

        // Check if the values can be retrieved correctly from the decorator map
        Assert.assertEquals((short) 2, doubleShortMapDecorator.get(1.0).shortValue());
        Assert.assertEquals((short) 3, doubleShortMapDecorator.get(2.0).shortValue());
        Assert.assertEquals((short) 4, doubleShortMapDecorator.get(3.0).shortValue());
    }

    @Test
    public void testEntrySet() {
        // Put some key-value pairs
        doubleShortMapDecorator.put(1.0, (short) 2);
        doubleShortMapDecorator.put(2.0, (short) 3);
        doubleShortMapDecorator.put(3.0, (short) 4);

        // Get the entry set
        Set<Entry<Double, Short>> entrySet = doubleShortMapDecorator.entrySet();

        // Check if the entry set contains the correct number of entries
        Assert.assertEquals(3, entrySet.size());

        // Check if each entry has the correct key-value pair
        for (Entry<Double, Short> entry : entrySet) {
            Double key = entry.getKey();
            Short value = entry.getValue();
            Assert.assertEquals(value, doubleShortMapDecorator.get(key));
        }
    }
}