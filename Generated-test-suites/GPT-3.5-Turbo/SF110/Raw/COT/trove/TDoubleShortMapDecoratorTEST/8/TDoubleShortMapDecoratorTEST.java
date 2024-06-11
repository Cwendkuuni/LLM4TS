import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator decorator;

    @Before
    public void setUp() {
        decorator = new TDoubleShortMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        decorator.put(1.0, (short) 1);
        assertEquals((short) 1, decorator.get(1.0));
    }

    @Test
    public void testClear() {
        decorator.put(1.0, (short) 1);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        decorator.put(1.0, (short) 1);
        assertEquals((short) 1, decorator.remove(1.0));
    }

    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short) 1);

        for (Map.Entry<Double, Short> entry : decorator.entrySet()) {
            assertEquals(1.0, entry.getKey(), 0.0);
            assertEquals((short) 1, entry.getValue());
        }
    }

    @Test
    public void testContainsValue() {
        decorator.put(1.0, (short) 1);
        assertTrue(decorator.containsValue((short) 1));
    }

    @Test
    public void testContainsKey() {
        decorator.put(1.0, (short) 1);
        assertTrue(decorator.containsKey(1.0));
    }

    @Test
    public void testSize() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        decorator.putAll(map);

        for (Map.Entry<Double, Short> entry : decorator.entrySet()) {
            double key = entry.getKey();
            short value = entry.getValue();
            assertTrue(map.containsKey(key));
            assertTrue(map.containsValue(value));
        }
    }
    
    @Test
    public void testPutNullKeyAndValue() {
        decorator.put(null, (short) 1);
        assertEquals((short) 1, decorator.get(null));
    }

    @Test
    public void testRemoveNullKey() {
        decorator.put(1.0, (short) 1);
        assertEquals((short) 1, decorator.remove(null));
    }

    @Test
    public void testGetKeyNotInMap() {
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testEntrySetEmptyMap() {
        for (Map.Entry<Double, Short> entry : decorator.entrySet()) {
            fail("EntrySet should be empty");
        }
    }

    @Test
    public void testContainsValueValueNotInMap() {
        decorator.put(1.0, (short) 1);
        assertFalse(decorator.containsValue((short) 2));
    }

    @Test
    public void testContainsKeyKeyNotInMap() {
        decorator.put(1.0, (short) 1);
        assertFalse(decorator.containsKey(2.0));
    }

    @Test
    public void testPutAllEmptyMap() {
        Map<Double, Short> map = new HashMap<>();
        decorator.putAll(map);
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAllDuplicateKeys() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(1.0, (short) 2);
        decorator.putAll(map);

        assertEquals((short) 2, decorator.get(1.0));
    }

    // Additional test cases for readExternal() and writeExternal() can be added here

}