import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        Short result = map.put(key, value);
        assertEquals(value, result);
        assertEquals(value, map.get(key));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        map.put(key, value);
        Short result = map.get(key);
        assertEquals(value, result);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, 1);
        map.put(2.0, 2);
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 1;
        map.put(key, value);
        Short result = map.remove(key);
        assertEquals(value, result);
        assertNull(map.get(key));
    }

    // test entrySet()

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Short value = 1;
        map.put(1.0, value);
        assertTrue(map.containsValue(value));
        assertFalse(map.containsValue(2));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        map.put(key, 1);
        assertTrue(map.containsKey(key));
        assertFalse(map.containsKey(2.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, 1);
        map.put(2.0, 2);
        assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertTrue(map.isEmpty());
        map.put(1.0, 1);
        assertFalse(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(1.0, 1);
        otherMap.put(2.0, 2);
        map.putAll(otherMap);
        assertEquals(2, map.size());
        assertEquals(1, (short)map.get(1.0));
        assertEquals(2, (short)map.get(2.0));
    }
}