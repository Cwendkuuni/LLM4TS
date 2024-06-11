package gnu.trove.decorator;

import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator decorator;
    private TDoubleShortMap testMap;

    @Before
    public void setUp() {
        testMap = new TDoubleShortHashMap();
        decorator = new TDoubleShortMapDecorator(testMap);
    }

    @Test
    public void testPut() {
        assertNull(decorator.put(1.0, (short) 1));
        assertEquals(1, decorator.put(2.0, (short) 2).shortValue());
    }

    @Test
    public void testGet() {
        assertNull(decorator.get(1.0));
        decorator.put(1.0, (short) 1);
        assertEquals(1, decorator.get(1.0).shortValue());
    }

    @Test
    public void testClear() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        assertNull(decorator.remove(1.0));
        decorator.put(1.0, (short) 1);
        assertEquals(1, decorator.remove(1.0).shortValue());
    }

    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        for (Map.Entry<Double, Short> entry : entrySet) {
            Double key = entry.getKey();
            Short value = entry.getValue();
            assertTrue(decorator.containsKey(key));
            assertEquals(value, decorator.get(key));
        }
    }

    @Test
    public void testContainsValue() {
        assertFalse(decorator.containsValue((short) 1));
        decorator.put(1.0, (short) 1);
        assertTrue(decorator.containsValue((short) 1));
    }

    @Test
    public void testContainsKey() {
        assertFalse(decorator.containsKey(1.0));
        decorator.put(1.0, (short) 1);
        assertTrue(decorator.containsKey(1.0));
    }

    @Test
    public void testSize() {
        assertEquals(0, decorator.size());
        decorator.put(1.0, (short) 1);
        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        decorator.putAll(map);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testWrapKey() {
        double key = 1.0;
        Double wrappedKey = decorator.wrapKey(key);
        assertEquals(key, wrappedKey, 0.0);
    }

    @Test
    public void testUnwrapKey() {
        double key = 1.0;
        Double wrappedKey = decorator.wrapKey(key);
        assertEquals(key, decorator.unwrapKey(wrappedKey), 0.0);
    }

    @Test
    public void testWrapValue() {
        short value = 1;
        Short wrappedValue = decorator.wrapValue(value);
        assertEquals(value, wrappedValue.shortValue());
    }

    @Test
    public void testUnwrapValue() {
        short value = 1;
        Short wrappedValue = decorator.wrapValue(value);
        assertEquals(value, decorator.unwrapValue(wrappedValue));
    }

    @Test
    public void testReadExternal() {
        try {
            decorator.readExternal(null);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e);
        }
    }

    @Test
    public void testWriteExternal() {
        try {
            decorator.writeExternal(null);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e);
        }
    }
}