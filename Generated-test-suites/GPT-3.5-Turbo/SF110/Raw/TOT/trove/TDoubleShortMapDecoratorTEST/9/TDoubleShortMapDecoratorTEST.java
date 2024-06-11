import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMap map;
    private TDoubleShortMapDecorator decorator;

    @Before
    public void setUp() {
        map = new TDoubleShortMap();
        decorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);
        decorator.put(3.0, (short) 30);

        assertEquals((Short) 10, decorator.get(1.0));
        assertEquals((Short) 20, decorator.get(2.0));
        assertEquals((Short) 30, decorator.get(3.0));
    }

    @Test
    public void testPutAndGetWithNullKey() {
        decorator.put(null, (short) 10);
        decorator.put(2.0, (short) 20);
        decorator.put(3.0, (short) 30);

        assertEquals((Short) 10, decorator.get(null));
        assertEquals((Short) 20, decorator.get(2.0));
        assertEquals((Short) 30, decorator.get(3.0));
    }

    @Test
    public void testPutAndGetWithNullValue() {
        decorator.put(1.0, null);
        decorator.put(2.0, (short) 20);
        decorator.put(3.0, (short) 30);

        assertNull(decorator.get(1.0));
        assertEquals((Short) 20, decorator.get(2.0));
        assertEquals((Short) 30, decorator.get(3.0));
    }

    @Test
    public void testClear() {
        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);
        decorator.put(3.0, (short) 30);

        decorator.clear();

        assertNull(decorator.get(1.0));
        assertNull(decorator.get(2.0));
        assertNull(decorator.get(3.0));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);

        Short removed = decorator.remove(1.0);

        assertEquals((Short) 10, removed);
        assertNull(decorator.get(1.0));
        assertEquals((Short) 20, decorator.get(2.0));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testRemoveWithNullKey() {
        decorator.put(null, (short) 10);
        decorator.put(2.0, (short) 20);

        Short removed = decorator.remove(null);

        assertEquals((Short) 10, removed);
        assertNull(decorator.get(null));
        assertEquals((Short) 20, decorator.get(2.0));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);

        Map.Entry<Double, Short> entry1 = Map.entry(1.0, (short) 10);
        Map.Entry<Double, Short> entry2 = Map.entry(2.0, (short) 20);

        assertTrue(decorator.entrySet().contains(entry1));
        assertTrue(decorator.entrySet().contains(entry2));
        assertFalse(decorator.entrySet().contains(Map.entry(3.0, (short) 30)));
        assertEquals(2, decorator.entrySet().size());
    }

    @Test
    public void testEntrySetSize() {
        assertEquals(0, decorator.entrySet().size());

        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);

        assertEquals(2, decorator.entrySet().size());

        decorator.remove(1.0);

        assertEquals(1, decorator.entrySet().size());
    }

    @Test
    public void testContainsValue() {
        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);

        assertTrue(decorator.containsValue((short) 10));
        assertTrue(decorator.containsValue((short) 20));
        assertFalse(decorator.containsValue((short) 30));
    }

    @Test
    public void testContainsKey() {
        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(3.0));
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testSize() {
        assertEquals(0, decorator.size());

        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);

        assertEquals(2, decorator.size());

        decorator.remove(2.0);

        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short) 10);

        assertFalse(decorator.isEmpty());

        decorator.remove(1.0);

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> inputMap = Map.of(
                1.0, (short) 10,
                2.0, (short) 20,
                3.0, (short) 30
        );

        decorator.putAll(inputMap);

        assertEquals((Short) 10, decorator.get(1.0));
        assertEquals((Short) 20, decorator.get(2.0));
        assertEquals((Short) 30, decorator.get(3.0));
        assertEquals(3, decorator.size());
    }

    @Test
    public void testWrapKey() {
        double key = 1.0;
        Double wrappedKey = decorator.wrapKey(key);

        assertEquals(key, wrappedKey, 0.01);
    }

    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        double unwrappedKey = decorator.unwrapKey(key);

        assertEquals(key, unwrappedKey, 0.01);
    }

    @Test
    public void testWrapValue() {
        short value = 10;
        Short wrappedValue = decorator.wrapValue(value);

        assertEquals(value, wrappedValue, 0.01);
    }

    @Test
    public void testUnwrapValue() {
        Short value = 10;
        short unwrappedValue = decorator.unwrapValue(value);

        assertEquals(value, unwrappedValue, 0.01);
    }


    @Test
    public void testReadExternal() {
        // To be implemented
    }

    @Test
    public void testWriteExternal() {
        // To be implemented
    }
}