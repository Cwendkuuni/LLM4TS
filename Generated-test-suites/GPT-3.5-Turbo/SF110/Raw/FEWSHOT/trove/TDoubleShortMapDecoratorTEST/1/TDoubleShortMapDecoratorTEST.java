import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;

        // Test put method
        assertNull(mapDecorator.put(null, null));
        assertNull(mapDecorator.put(key1, value1));
        assertNull(mapDecorator.put(key2, value2));

        // Test get method
        assertNull(mapDecorator.get(null));
        assertEquals(value1, mapDecorator.get(key1));
        assertEquals(value2, mapDecorator.get(key2));

        // Test putAll method
        Map<Double, Short> inputMap = new HashMap<>();
        inputMap.put(3.0, (short) 30);
        inputMap.put(4.0, (short) 40);
        inputMap.put(5.0, (short) 50);
        mapDecorator.putAll(inputMap);

        assertEquals((short) 30, mapDecorator.get(3.0));
        assertEquals((short) 40, mapDecorator.get(4.0));
        assertEquals((short) 50, mapDecorator.get(5.0));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;

        mapDecorator.put(key1, value1);
        mapDecorator.put(key2, value2);

        // Test remove method
        assertNull(mapDecorator.remove(null));
        assertEquals(value1, mapDecorator.remove(key1));
        assertEquals(value2, mapDecorator.remove(key2));
    }

    @Test
    public void testSizeAndIsEmpty() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;

        assertEquals(0, mapDecorator.size());
        assertEquals(true, mapDecorator.isEmpty());

        mapDecorator.put(key1, value1);
        mapDecorator.put(key2, value2);

        assertEquals(2, mapDecorator.size());
        assertEquals(false, mapDecorator.isEmpty());

        mapDecorator.remove(key1);

        assertEquals(1, mapDecorator.size());
        assertEquals(false, mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKeyAndContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;

        mapDecorator.put(key1, value1);
        mapDecorator.put(key2, value2);

        assertEquals(true, mapDecorator.containsKey(key1));
        assertEquals(true, mapDecorator.containsKey(key2));
        assertEquals(false, mapDecorator.containsKey(null));
        assertEquals(false, mapDecorator.containsKey(3.0));

        assertEquals(true, mapDecorator.containsValue(value1));
        assertEquals(true, mapDecorator.containsValue(value2));
        assertEquals(false, mapDecorator.containsValue(null));
        assertEquals(false, mapDecorator.containsValue((short) 30));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Short value1 = 10;
        Double key2 = 2.0;
        Short value2 = 20;

        mapDecorator.put(key1, value1);
        mapDecorator.put(key2, value2);

        mapDecorator.clear();

        assertEquals(0, mapDecorator.size());
        assertEquals(true, mapDecorator.isEmpty());
        assertEquals(false, mapDecorator.containsKey(key1));
        assertEquals(false, mapDecorator.containsKey(key2));
        assertEquals(false, mapDecorator.containsValue(value1));
        assertEquals(false, mapDecorator.containsValue(value2));
    }
}