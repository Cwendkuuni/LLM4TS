package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setup() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        assertEquals((Short) 1, mapDecorator.put(4.0, (short) 4));
        assertEquals((Short) 4, mapDecorator.get(4.0));
    }

    @Test
    public void testPutNullKey() {
        assertNull(mapDecorator.put(null, (short) 1));
        assertEquals((Short) 1, mapDecorator.get(null));
        assertTrue(mapDecorator.containsKey(null));
    }

    @Test
    public void testPutNullValue() {
        assertNull(mapDecorator.put(4.0, null));
        assertNull(mapDecorator.get(4.0));
        assertTrue(mapDecorator.containsValue(null));
    }

    @Test
    public void testGet() {
        assertEquals((Short) 1, mapDecorator.get(1.0));
        assertEquals((Short) 2, mapDecorator.get(2.0));
        assertEquals((Short) 3, mapDecorator.get(3.0));
        assertNull(mapDecorator.get(4.0));
    }

    @Test
    public void testClear() {
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testRemove() {
        assertEquals((Short) 1, mapDecorator.remove(1.0));
        assertNull(mapDecorator.get(1.0));
        assertEquals((Short) 2, mapDecorator.remove(2.0));
        assertNull(mapDecorator.get(2.0));
        assertEquals((Short) 3, mapDecorator.remove(3.0));
        assertNull(mapDecorator.get(3.0));
        assertNull(mapDecorator.remove(4.0));
        assertFalse(mapDecorator.containsKey(null));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Double, Short>> entrySet = mapDecorator.entrySet();
        assertEquals(3, entrySet.size());

        for (Map.Entry<Double, Short> entry : entrySet) {
            Double key = entry.getKey();
            Short value = entry.getValue();

            assertTrue(mapDecorator.containsKey(key));
            assertEquals(value, mapDecorator.get(key));
        }
    }

    @Test
    public void testContainsValue() {
        assertTrue(mapDecorator.containsValue((short) 1));
        assertTrue(mapDecorator.containsValue((short) 2));
        assertTrue(mapDecorator.containsValue((short) 3));
        assertFalse(mapDecorator.containsValue((short) 4));
    }

    @Test
    public void testContainsKey() {
        assertTrue(mapDecorator.containsKey(1.0));
        assertTrue(mapDecorator.containsKey(2.0));
        assertTrue(mapDecorator.containsKey(3.0));
        assertFalse(mapDecorator.containsKey(4.0));
    }

    @Test
    public void testSize() {
        assertEquals(3, mapDecorator.size());
        mapDecorator.put(4.0, (short) 4);
        assertEquals(4, mapDecorator.size());
        mapDecorator.remove(1.0);
        assertEquals(3, mapDecorator.size());
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put(1.0, (short) 1);
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.remove(1.0);
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> inputMap = new HashMap<>();
        inputMap.put(4.0, (short) 4);
        inputMap.put(5.0, (short) 5);

        mapDecorator.putAll(inputMap);

        assertEquals(5, mapDecorator.size());
        assertEquals((Short) 4, mapDecorator.get(4.0));
        assertEquals((Short) 5, mapDecorator.get(5.0));
    }

    @Test
    public void testWrapKey() {
        double key = 1.0;
        Double wrappedKey = mapDecorator.wrapKey(key);
        assertEquals(Double.valueOf(key), wrappedKey);
    }

    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        double unwrappedKey = mapDecorator.unwrapKey(key);
        assertEquals(Double.valueOf(key), Double.valueOf(unwrappedKey));
    }

    @Test
    public void testWrapValue() {
        short value = 1;
        Short wrappedValue = mapDecorator.wrapValue(value);
        assertEquals(Short.valueOf(value), wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        Short value = 1;
        short unwrappedValue = mapDecorator.unwrapValue(value);
        assertEquals(Short.valueOf(value), Short.valueOf(unwrappedValue));
    }
}