import org.junit.Test;
import java.util.Map;
import java.util.HashMap;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGetValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        assertEquals(value, map.get(key));
    }

    @Test
    public void testPutAndGetNoEntryValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = 1.0;
        Short value = null;
        map.put(key, value);
        assertEquals(map.getMap().getNoEntryValue(), map.get(key));
    }

    @Test
    public void testPutAndGetNoEntryKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = null;
        Short value = 2;
        map.put(key, value);
        assertEquals(map.getMap().getNoEntryValue(), map.get(key));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        map.clear();
        assertNull(map.get(key));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        map.remove(key);
        assertNull(map.get(key));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        Map.Entry<Double,Short> entry = map.entrySet().iterator().next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        assertTrue(map.containsValue(value));
        assertFalse(map.containsValue(3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        assertTrue(map.containsKey(key));
        assertFalse(map.containsKey(2.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        assertEquals(1, map.size());
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        assertTrue(map.isEmpty());
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        assertFalse(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        Map<Double, Short> values = new HashMap<>();
        values.put(1.0, (short)2);
        values.put(2.0, (short)3);
        values.put(3.0, (short)4);
        map.putAll(values);
        assertEquals(values.size(), map.size());
    }
}