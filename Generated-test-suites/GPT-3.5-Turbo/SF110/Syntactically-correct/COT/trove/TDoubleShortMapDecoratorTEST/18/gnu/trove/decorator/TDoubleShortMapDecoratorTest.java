package gnu.trove.decorator;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test putting and getting values
        decorator.put(1.0, (short) 1);
        assertEquals(1, decorator.get(1.0).shortValue());

        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.get(2.0).shortValue());

        decorator.put(3.0, (short) 3);
        assertEquals(3, decorator.get(3.0).shortValue());
    }

    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test clearing the map
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test removing a key
        assertEquals(1, decorator.remove(1.0).shortValue());
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test checking for a value
        assertTrue(decorator.containsValue((short) 1));
        assertFalse(decorator.containsValue((short) 4));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test checking for a key
        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(4.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test getting the size of the map
        assertEquals(3, decorator.size());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test putting all values from another map
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(4.0, (short) 4);
        otherMap.put(5.0, (short) 5);
        decorator.putAll(otherMap);

        assertEquals(5, decorator.size());
        assertEquals(4, decorator.get(4.0).shortValue());
        assertEquals(5, decorator.get(5.0).shortValue());
    }
}