package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;
    private TDoubleShortMap map;

    @Before
    public void setUp() {
        map = // Create an instance of TDoubleShortMap
        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        Double key1 = 1.0;
        Short value1 = 10;
        assertEquals(null, mapDecorator.put(key1, value1));
        Double key2 = 2.0;
        Short value2 = 20;
        assertEquals(null, mapDecorator.put(key2, value2));
    }

    @Test
    public void testGet() {
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.get(key1));
        Double key2 = 2.0;
        Short value2 = 20;
        mapDecorator.put(key2, value2);
        assertEquals(value2, mapDecorator.get(key2));
    }

    @Test
    public void testClear() {
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        Double key2 = 2.0;
        Short value2 = 20;
        mapDecorator.put(key2, value2);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.remove(key1));
        assertNull(mapDecorator.remove(key1));
    }

    @Test
    public void testEntrySet() {
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        Set<Map.Entry<Double, Short>> entrySet = mapDecorator.entrySet();
        Iterator<Map.Entry<Double, Short>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        Map.Entry<Double, Short> entry1 = iterator.next();
        assertEquals(key1, entry1.getKey());
        assertEquals(value1, entry1.getValue());
        assertFalse(iterator.hasNext());
        Double key2 = 2.0;
        Short value2 = 20;
        mapDecorator.put(key2, value2);
        entrySet = mapDecorator.entrySet();
        iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        Map.Entry<Double, Short> entry2 = iterator.next();
        assertEquals(key2, entry2.getKey());
        assertEquals(value2, entry2.getValue());
        assertTrue(iterator.hasNext());
        Map.Entry<Double, Short> entry3 = iterator.next();
        assertEquals(key1, entry3.getKey());
        assertEquals(value1, entry3.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsValue(value1));
        assertFalse(mapDecorator.containsValue(value1 + 1));
    }

    @Test
    public void testContainsKey() {
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsKey(key1));
        assertFalse(mapDecorator.containsKey(key1 + 1));
    }

    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        assertEquals(1, mapDecorator.size());
        Double key2 = 2.0;
        Short value2 = 20;
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        Double key1 = 1.0;
        Short value1 = 10;
        mapDecorator.put(key1, value1);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> otherMap = // Create another map
        mapDecorator.putAll(otherMap);
        assertEquals(otherMap.size(), mapDecorator.size());
    }

    @Test
    public void testWrapKey() {
        double key = 1.0;
        assertEquals(key, mapDecorator.wrapKey(key), 0.0);
    }

    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        assertEquals(key, mapDecorator.unwrapKey(key));
    }

    @Test
    public void testWrapValue() {
        short value = 10;
        assertEquals(value, mapDecorator.wrapValue(value), 0);
    }

    @Test
    public void testUnwrapValue() {
        Short value = 10;
        assertEquals(value, mapDecorator.unwrapValue(value));
    }

    @Test
    public void testReadExternal() {
        // Test readExternal method
    }

    @Test
    public void testWriteExternal() {
        // Test writeExternal method
    }
}