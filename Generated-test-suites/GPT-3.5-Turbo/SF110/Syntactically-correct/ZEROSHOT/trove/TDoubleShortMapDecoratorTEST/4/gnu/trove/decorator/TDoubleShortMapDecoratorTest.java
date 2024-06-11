import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator mapDecorator;
    private TDoubleShortMap map;

    @Before
    public void setUp() {
        map = new TDoubleShortMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        Double key = 1.0;
        Short value = 2;
        assertNull(mapDecorator.put(key, value));
        assertEquals(1, mapDecorator.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(value));
        assertEquals(value, mapDecorator.put(key, value));
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testGet() {
        Double key = 1.0;
        Short value = 2;
        assertNull(mapDecorator.get(key));
        map.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testClear() {
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
        assertFalse(map.containsKey(key));
    }

    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 2;
        assertNull(mapDecorator.remove(key));
        map.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        assertTrue(mapDecorator.isEmpty());
        assertFalse(map.containsKey(key));
    }

    @Test
    public void testEntrySet() {
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        Map.Entry<Double, Short> entry = new HashMap.SimpleEntry<>(key, value);
        assertTrue(mapDecorator.entrySet().contains(entry));
        map.remove(key);
        assertFalse(mapDecorator.entrySet().contains(entry));
    }

    @Test
    public void testContainsValue() {
        Double key = 1.0;
        Short value = 2;
        assertFalse(mapDecorator.containsValue(value));
        map.put(key, value);
        assertTrue(mapDecorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        Double key = 1.0;
        Short value = 2;
        assertFalse(mapDecorator.containsKey(key));
        map.put(key, value);
        assertTrue(mapDecorator.containsKey(key));
    }

    @Test
    public void testSize() {
        Double key = 1.0;
        Short value = 2;
        assertEquals(0, mapDecorator.size());
        map.put(key, value);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(1.0, (short) 2);
        otherMap.put(2.0, (short) 3);
        mapDecorator.putAll(otherMap);
        assertEquals(otherMap, map);
    }
}