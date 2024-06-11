import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;
    private TDoubleShortMap map;

    @Before
    public void setUp() {
        map = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        mapDecorator.put(1.0, (short) 1);
        assertEquals((short) 1, mapDecorator.get(1.0).shortValue());

        mapDecorator.put(2.0, (short) 2);
        assertEquals((short) 2, mapDecorator.get(2.0).shortValue());
    }

    @Test
    public void testGet() {
        map.put(1.0, (short) 1);
        assertEquals((short) 1, mapDecorator.get(1.0).shortValue());

        map.put(2.0, (short) 2);
        assertEquals((short) 2, mapDecorator.get(2.0).shortValue());
    }

    @Test
    public void testClear() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        mapDecorator.clear();

        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        map.put(1.0, (short) 1);
        mapDecorator.remove(1.0);

        assertNull(mapDecorator.get(1.0));

        map.put(2.0, (short) 2);
        assertEquals((short) 2, mapDecorator.remove(2.0).shortValue());
    }

    @Test
    public void testEntrySet() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        for (Map.Entry<Double, Short> entry : mapDecorator.entrySet()) {
            double key = entry.getKey();
            short value = entry.getValue();

            assertTrue(map.containsKey(key));
            assertEquals(map.get(key), Short.valueOf(value));
        }
    }

    @Test
    public void testContainsValue() {
        map.put(1.0, (short) 1);
        assertTrue(mapDecorator.containsValue((short) 1));

        mapDecorator.put(2.0, (short) 2);
        assertTrue(mapDecorator.containsValue((short) 2));
    }

    @Test
    public void testContainsKey() {
        map.put(1.0, (short) 1);
        assertTrue(mapDecorator.containsKey(1.0));

        mapDecorator.put(2.0, (short) 2);
        assertTrue(mapDecorator.containsKey(2.0));
    }

    @Test
    public void testSize() {
        map.put(1.0, (short) 1);
        assertEquals(1, mapDecorator.size());

        mapDecorator.put(2.0, (short) 2);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());

        mapDecorator.put(1.0, (short) 1);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        java.util.HashMap<Double, Short> hashMap = new java.util.HashMap<>();
        hashMap.put(1.0, (short) 1);
        hashMap.put(2.0, (short) 2);

        mapDecorator.putAll(hashMap);

        for (Map.Entry<Double, Short> entry : hashMap.entrySet()) {
            double key = entry.getKey();
            short value = entry.getValue();

            assertTrue(mapDecorator.containsKey(key));
            assertEquals(mapDecorator.get(key), Short.valueOf(value));
        }
    }
}