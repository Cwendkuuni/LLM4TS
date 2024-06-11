import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.Iterator;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator map;

    @Before
    public void setUp() {
        map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
    }

    @Test
    public void testPut() {
        // Typical put scenario
        assertNull(map.put(4.0, (short) 4));
        assertEquals((short) 4, map.get(4.0).shortValue());

        // Edge case - replace existing key
        assertEquals((short) 2, map.put(2.0, (short) 5).shortValue());

        // Error scenario - null key
        assertNull(map.put(null, (short) 6));
        assertEquals((short) 6, map.get(null).shortValue());
    }

    @Test
    public void testGet() {
        // Typical get scenario
        assertEquals((short) 1, map.get(1.0).shortValue());

        // Edge case - non-existing key
        assertNull(map.get(4.0));

        // Error scenario - invalid key type
        assertNull(map.get("2.0"));
    }

    @Test
    public void testClear() {
        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        // Typical remove scenario
        assertEquals((short) 2, map.remove(2.0).shortValue());
        assertNull(map.get(2.0));

        // Edge case - non-existing key
        assertNull(map.remove(4.0));

        // Error scenario - invalid key type
        assertNull(map.remove("3.0"));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();

        // Typical entry set scenario
        assertEquals(3, entrySet.size());
        
        Iterator<Map.Entry<Double, Short>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Double, Short> entry = iterator.next();
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
        }
    }

    @Test
    public void testContainsValue() {
        // Typical contains value scenario
        assertTrue(map.containsValue((short) 3));

        // Edge case - non-existing value
        assertFalse(map.containsValue((short) 4));

        // Error scenario - invalid value type
        assertFalse(map.containsValue("1"));
    }

    @Test
    public void testContainsKey() {
        // Typical contains key scenario
        assertTrue(map.containsKey(1.0));

        // Edge case - non-existing key
        assertFalse(map.containsKey(4.0));

        // Error scenario - invalid key type
        assertFalse(map.containsKey("2.0"));
    }

    @Test
    public void testSize() {
        assertEquals(3, map.size());

        // Test size after adding new entry
        map.put(4.0, (short) 4);
        assertEquals(4, map.size());

        // Test size after removing an entry
        map.remove(2.0);
        assertEquals(3, map.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(map.isEmpty());

        // Test empty after clear
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(4.0, (short) 4);
        otherMap.put(5.0, (short) 5);

        // Typical put all scenario
        map.putAll(otherMap);
        assertEquals((short) 4, map.get(4.0).shortValue());
        assertEquals((short) 5, map.get(5.0).shortValue());

        // Error scenario - null map
        try {
            map.putAll(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        double key = 2.0;
        Double wrappedKey = decorator.wrapKey(key);
        assertEquals(key, wrappedKey, 0.0);
    }

    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Double key = 2.0;
        double unwrappedKey = decorator.unwrapKey(key);
        assertEquals(key, unwrappedKey, 0.0);
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        short value = 2;
        Short wrappedValue = decorator.wrapValue(value);
        assertEquals(value, wrappedValue.shortValue());
    }

    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = 2;
        short unwrappedValue = decorator.unwrapValue(value);
        assertEquals(value, unwrappedValue);
    }

    @Test
    public void testReadExternal() {
        // Additional test required for the Externalizable interface
        // Not implemented in the provided code
        fail("Not implemented");
    }

    @Test
    public void testWriteExternal() {
        // Additional test required for the Externalizable interface
        // Not implemented in the provided code
        fail("Not implemented");
    }
}