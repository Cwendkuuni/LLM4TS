
package gnu.trove.decorator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        Double key1 = 1.0;
        Short value1 = 1;
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.get(key1));

        // Test with null key
        Short value2 = 2;
        mapDecorator.put(null, value2);
        assertEquals(value2, mapDecorator.get(null));

        // Test with null value
        Double key3 = 3.0;
        mapDecorator.put(key3, null);
        assertNull(mapDecorator.get(key3));
    }

    @Test
    public void testClear() {
        Double key1 = 1.0;
        Short value1 = 1;
        mapDecorator.put(key1, value1);

        Double key2 = 2.0;
        Short value2 = 2;
        mapDecorator.put(key2, value2);

        mapDecorator.clear();
        assertNull(mapDecorator.get(key1));
        assertNull(mapDecorator.get(key2));
    }

    @Test
    public void testRemove() {
        Double key1 = 1.0;
        Short value1 = 1;
        mapDecorator.put(key1, value1);

        Double key2 = 2.0;
        Short value2 = 2;
        mapDecorator.put(key2, value2);

        // Test removing existing key
        assertEquals(value1, mapDecorator.remove(key1));
        assertNull(mapDecorator.get(key1));
        
        // Test removing non-existing key
        assertNull(mapDecorator.remove(3.0));
    }

    @Test
    public void testContainsKey() {
        Double key1 = 1.0;
        Short value1 = 1;
        mapDecorator.put(key1, value1);

        Double key2 = 2.0;
        Short value2 = 2;
        mapDecorator.put(key2, value2);

        // Test with existing key
        assertTrue(mapDecorator.containsKey(key1));

        // Test with non-existing key
        assertFalse(mapDecorator.containsKey(3.0));
    }

    @Test
    public void testContainsValue() {
        Double key1 = 1.0;
        Short value1 = 1;
        mapDecorator.put(key1, value1);

        Double key2 = 2.0;
        Short value2 = 2;
        mapDecorator.put(key2, value2);

        // Test with existing value
        assertTrue(mapDecorator.containsValue(value1));

        // Test with non-existing value
        assertFalse(mapDecorator.containsValue(3));
    }

    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());

        Double key1 = 1.0;
        Short value1 = 1;
        mapDecorator.put(key1, value1);

        Double key2 = 2.0;
        Short value2 = 2;
        mapDecorator.put(key2, value2);

        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());

        Double key1 = 1.0;
        Short value1 = 1;
        mapDecorator.put(key1, value1);

        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);

        mapDecorator.putAll(map);

        assertEquals((short)1, (short)mapDecorator.get(1.0));
        assertEquals((short)2, (short)mapDecorator.get(2.0));
    }
}
