import org.junit.*;
import static org.junit.Assert.*;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

    // Test case for put method
    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        assertNull(mapDecorator.put(key1, value1));
        
        // Edge case: key is null
        Double nullKey = null;
        Short nullValue = 3;
        assertNull(mapDecorator.put(nullKey, nullValue));
        
        // Error scenario: value is null
        Double key2 = 2.0;
        Short nullValue2 = null;
        assertNull(mapDecorator.put(key2, nullValue2));
    }

    // Test case for get method
    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.get(key1));
        
        // Edge case: key is null
        Double nullKey = null;
        Short nullValue = 3;
        mapDecorator.put(nullKey, nullValue);
        assertEquals(nullValue, mapDecorator.get(nullKey));
    }

    // Test case for clear method
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        mapDecorator.clear();
        assertNull(mapDecorator.get(key1));
        
        // Edge case: map is empty
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    // Test case for remove method
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.remove(key1));
        assertNull(mapDecorator.get(key1));
        
        // Edge case: key is null
        Double nullKey = null;
        Short nullValue = 3;
        mapDecorator.put(nullKey, nullValue);
        assertEquals(nullValue, mapDecorator.remove(nullKey));
        assertNull(mapDecorator.get(nullKey));
    }

    // Test case for entrySet method
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        
        // Edge case: map is empty
        mapDecorator.clear();
        
        // Error scenario: empty map, try to remove entry
        assertFalse(mapDecorator.entrySet().remove(null));
    }

    // Test case for containsValue method
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsValue(value1));
        
        // Edge case: value is null
        Double key2 = 2.0;
        Short nullValue = null;
        mapDecorator.put(key2, nullValue);
        assertTrue(mapDecorator.containsValue(nullValue));
        
        // Error scenario: value is not Short type
        String notShortValue = "not short";
        assertFalse(mapDecorator.containsValue(notShortValue));
        
        // Error scenario: value is Short type but not in the map
        Short notInMapValue = 4;
        assertFalse(mapDecorator.containsValue(notInMapValue));
    }

    // Test case for containsKey method
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsKey(key1));
        
        // Edge case: key is null
        Double nullKey = null;
        Short nullValue = 3;
        mapDecorator.put(nullKey, nullValue);
        assertTrue(mapDecorator.containsKey(nullKey));
        
        // Error scenario: key is not Double type
        String notDoubleKey = "not double";
        assertFalse(mapDecorator.containsKey(notDoubleKey));
        
        // Error scenario: key is Double type but not in the map
        Double notInMapKey = 2.5;
        assertFalse(mapDecorator.containsKey(notInMapKey));
    }

    // Test case for size method
    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertEquals(1, mapDecorator.size());
        
        // Edge case: map is empty
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    // Test case for isEmpty method
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertFalse(mapDecorator.isEmpty());
        
        // Edge case: map is empty
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    // Test case for putAll method
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Double key1 = 1.0;
        Short value1 = 2;
        Double key2 = 2.0;
        Short value2 = 3;
        
        Map<Double, Short> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        
        mapDecorator.putAll(map);
        
        assertEquals(value1, mapDecorator.get(key1));
        assertEquals(value2, mapDecorator.get(key2));
        
        // Error scenario: null map
        mapDecorator.clear();
        try {
            mapDecorator.putAll(null);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected error
        }
    }

    // Test case for wrapKey method
    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        double key = 1.0;
        assertEquals(key, mapDecorator.unwrapKey(mapDecorator.wrapKey(key)), 0.001);
        
        // Edge case: key is 0
        double zeroKey = 0.0;
        assertEquals(zeroKey, mapDecorator.unwrapKey(mapDecorator.wrapKey(zeroKey)), 0.001);
    }

    // Test case for unwrapKey method
    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        Double key = 1.0;
        assertEquals(key, mapDecorator.wrapKey(mapDecorator.unwrapKey(key)));
        
        // Edge case: key is null
        Double nullKey = null;
        assertNull(mapDecorator.wrapKey(mapDecorator.unwrapKey(nullKey)));
    }

    // Test case for wrapValue method
    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        short value = 2;
        assertEquals(value, (short) mapDecorator.unwrapValue(mapDecorator.wrapValue(value)));
        
        // Edge case: value is 0
        short zeroValue = 0;
        assertEquals(zeroValue, (short) mapDecorator.unwrapValue(mapDecorator.wrapValue(zeroValue)));
    }

    // Test case for unwrapValue method
    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        Short value = 2;
        assertEquals(value, mapDecorator.wrapValue(mapDecorator.unwrapValue(value)));
        
        // Edge case: value is null
        Short nullValue = null;
        assertNull(mapDecorator.wrapValue(mapDecorator.unwrapValue(nullValue)));
    }

    // Test case for readExternal method
    @Test
    public void testReadExternal() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Cannot be tested directly as it requires ObjectInput parameter
    }

    // Test case for writeExternal method
    @Test
    public void testWriteExternal() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        
        // Cannot be tested directly as it requires ObjectOutput parameter
    }
}