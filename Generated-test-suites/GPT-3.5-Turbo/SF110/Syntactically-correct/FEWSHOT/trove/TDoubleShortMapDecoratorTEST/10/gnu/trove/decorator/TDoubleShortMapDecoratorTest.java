package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator decorator;
    
    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        decorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        decorator.put(4.0, (short) 4);
        assertTrue(decorator.containsKey(4.0));
        assertEquals((short) 4, decorator.get(4.0).shortValue());
    }

    @Test
    public void testPutNullKey() {
        decorator.put(null, (short) 0);
        assertTrue(decorator.containsKey(null));
        assertEquals((short) 0, decorator.get(null).shortValue());
    }

    @Test
    public void testPutNullValue() {
        decorator.put(5.0, null);
        assertTrue(decorator.containsKey(5.0));
        assertNull(decorator.get(5.0));
    }

    @Test
    public void testGet() {
        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.0).shortValue());
        assertEquals((short) 3, decorator.get(3.0).shortValue());
    }

    @Test
    public void testGetNullKey() {
        assertNull(decorator.get(null));
    }

    @Test
    public void testGetNonExistingKey() {
        assertNull(decorator.get(4.0));
    }

    @Test
    public void testRemove() {
        decorator.remove(1.0);
        assertFalse(decorator.containsKey(1.0));
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testRemoveNullKey() {
        decorator.remove(null);
        assertFalse(decorator.containsKey(null));
        assertNull(decorator.get(null));
    }

    @Test
    public void testRemoveNonExistingKey() {
        assertNull(decorator.remove(4.0));
    }

    @Test
    public void testClear() {
        decorator.clear();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertTrue(decorator.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsKey(3.0));
    }

    @Test
    public void testSize() {
        assertEquals(3, decorator.size());
        decorator.put(4.0, (short) 4);
        assertEquals(4, decorator.size());
        decorator.remove(1.0);
        assertEquals(3, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(decorator.isEmpty());
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(4.0, (short) 4);
        map.put(5.0, (short) 5);
        decorator.putAll(map);
        assertTrue(decorator.containsKey(4.0));
        assertTrue(decorator.containsKey(5.0));
        assertEquals((short) 4, decorator.get(4.0).shortValue());
        assertEquals((short) 5, decorator.get(5.0).shortValue());
    }
}