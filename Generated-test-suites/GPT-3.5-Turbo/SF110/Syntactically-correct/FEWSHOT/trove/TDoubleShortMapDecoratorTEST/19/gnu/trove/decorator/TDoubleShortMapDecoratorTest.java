package gnu.trove.decorator;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        map.put(4.0, (short) 4);

        map.forEach(mapDecorator::put);

        assertEquals((short) 1, mapDecorator.get(1.0));
        assertEquals((short) 2, mapDecorator.get(2.0));
        assertEquals((short) 3, mapDecorator.get(3.0));
        assertEquals((short) 4, mapDecorator.get(4.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        map.put(4.0, (short) 4);

        map.forEach(mapDecorator::put);

        assertEquals(4, mapDecorator.size());

        mapDecorator.clear();

        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        map.put(4.0, (short) 4);

        map.forEach(mapDecorator::put);

        assertEquals((short) 2, mapDecorator.remove(2.0));
        assertNull(mapDecorator.get(2.0));
        assertEquals(3, mapDecorator.size());
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);

        map.forEach(mapDecorator::put);

        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(2.0));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);

        map.forEach(mapDecorator::put);

        assertTrue(mapDecorator.containsValue((short) 1));
        assertFalse(mapDecorator.containsValue((short) 2));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        map.put(4.0, (short) 4);

        map.forEach(mapDecorator::put);

        assertEquals(4, mapDecorator.size());

        mapDecorator.put(5.0, (short) 5);

        assertEquals(5, mapDecorator.size());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        map.put(4.0, (short) 4);

        mapDecorator.putAll(map);

        assertEquals((short) 1, mapDecorator.get(1.0));
        assertEquals((short) 2, mapDecorator.get(2.0));
        assertEquals((short) 3, mapDecorator.get(3.0));
        assertEquals((short) 4, mapDecorator.get(4.0));
    }
}