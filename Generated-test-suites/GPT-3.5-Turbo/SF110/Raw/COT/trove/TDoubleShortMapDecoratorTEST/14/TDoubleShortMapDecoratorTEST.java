
import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testGet() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testClear() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        mapDecorator.clear();
        assertNull(mapDecorator.get(key));
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
    public void testEntrySet() {
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        Map.Entry<Double, Short> entry = mapDecorator.entrySet().iterator().next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
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
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(1.0, 2);
        otherMap.put(2.0, 3);
        mapDecorator.putAll(otherMap);
        assertEquals(2, mapDecorator.size());
    }
}
