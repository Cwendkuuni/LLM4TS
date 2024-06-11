import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setup() {
        mapDecorator = new TDoubleShortMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testPutNullKey() {
        Double key = null;
        Short value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testPutNullValue() {
        Double key = 1.0;
        Short value = null;
        mapDecorator.put(key, value);
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testGetNullKey() {
        Double key = null;
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testGetNoEntryValue() {
        Double key = 1.0;
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testClear() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        mapDecorator.clear();
        assertNull(mapDecorator.get(key));
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        mapDecorator.remove(key);
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testRemoveNullKey() {
        Double key = null;
        assertNull(mapDecorator.remove(key));
    }

    @Test
    public void testContainsValue() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsKey(key));
    }

    @Test
    public void testSize() {
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        Double key2 = 2.0;
        Short value2 = 3;
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, 2);
        map.put(2.0, 3);
        mapDecorator.putAll(map);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testWrapKey() {
        double k = 1.0;
        assertEquals(k, mapDecorator.wrapKey(k), 0);
    }

    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        assertEquals(key, mapDecorator.unwrapKey(key), 0);
    }

    @Test
    public void testWrapValue() {
        short k = 2;
        assertEquals(k, mapDecorator.wrapValue(k), 0);
    }

    @Test
    public void testUnwrapValue() {
        Short value = 2;
        assertEquals(value, mapDecorator.unwrapValue(value), 0);
    }
}