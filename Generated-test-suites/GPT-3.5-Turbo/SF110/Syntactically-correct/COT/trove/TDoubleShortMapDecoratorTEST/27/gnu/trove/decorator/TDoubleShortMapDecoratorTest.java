
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator map;

    @Before
    public void setUp() {
        map = new TDoubleShortMapDecorator();
    }

    @Test
    public void testPut() {
        Double key1 = 1.0;
        Short value1 = 10;
        assertEquals(null, map.put(key1, value1));
        assertEquals(value1, map.put(key1, value1));

        Double key2 = null;
        Short value2 = 20;
        assertEquals(null, map.put(key2, value2));
        assertEquals(value2, map.put(key2, value2));
    }

    @Test
    public void testGet() {
        Double key = 2.0;
        Short value = 30;
        map.put(key, value);
        assertEquals(value, map.get(key));
    }

    @Test
    public void testClear() {
        Double key = 3.0;
        Short value = 40;
        map.put(key, value);
        map.clear();
        assertEquals(null, map.get(key));
    }

    @Test
    public void testRemove() {
        Double key = 4.0;
        Short value = 50;
        map.put(key, value);
        assertEquals(value, map.remove(key));
        assertEquals(null, map.remove(key));
    }

    @Test
    public void testEntrySet() {
        Double key1 = 5.0;
        Short value1 = 60;
        map.put(key1, value1);

        Double key2 = 6.0;
        Short value2 = 70;
        map.put(key2, value2);

        assertEquals(2, map.entrySet().size());
        assertTrue(map.entrySet().contains(new Entry<Double, Short>() {
            public Double getKey() { return key1; }
            public Short getValue() { return value1; }
            public Short setValue(Short value) { return null; }
        }));
        assertTrue(map.entrySet().contains(new Entry<Double, Short>() {
            public Double getKey() { return key2; }
            public Short getValue() { return value2; }
            public Short setValue(Short value) { return null; }
        }));

        assertFalse(map.entrySet().contains(new Entry<Double, Short>() {
            public Double getKey() { return 7.0; }
            public Short getValue() { return 80; }
            public Short setValue(Short value) { return null; }
        }));
    }

    @Test
    public void testContainsValue() {
        Short value = 90;
        assertFalse(map.containsValue(value));

        Double key = 8.0;
        map.put(key, value);
        assertTrue(map.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        Double key = 9.0;
        assertFalse(map.containsKey(key));
        
        Short value = 100;
        map.put(key, value);
        assertTrue(map.containsKey(key));

        assertFalse(map.containsKey(null));
    }

    @Test
    public void testSize() {
        Double key1 = 10.0;
        Short value1 = 110;
        map.put(key1, value1);

        Double key2 = 11.0;
        Short value2 = 120;
        map.put(key2, value2);

        assertEquals(2, map.size());

        map.remove(key1);

        assertEquals(1, map.size());

        map.clear();

        assertEquals(0, map.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(map.isEmpty());

        Double key = 12.0;
        Short value = 130;
        map.put(key, value);
        assertFalse(map.isEmpty());

        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> otherMap = new HashMap<Double, Short>();
        otherMap.put(13.0, 140);
        otherMap.put(14.0, 150);

        map.putAll(otherMap);

        assertEquals(2, map.size());
    }
}
