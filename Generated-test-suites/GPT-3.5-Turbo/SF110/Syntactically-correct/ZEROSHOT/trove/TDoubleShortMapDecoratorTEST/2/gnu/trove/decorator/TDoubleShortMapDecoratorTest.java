import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;
import java.util.AbstractMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator decorator;

    @Before
    public void setUp() {
        decorator = new TDoubleShortMapDecorator();
    }

    @Test
    public void put() {
        decorator.put(1.0, (short) 1);
        assertTrue(decorator.containsKey(1.0));
        assertEquals((short) 1, (short) decorator.get(1.0));
    }

    @Test
    public void get() {
        decorator.put(1.0, (short) 1);
        assertEquals((short) 1, (short) decorator.get(1.0));
        assertNull(decorator.get(2.0));
    }

    @Test
    public void clear() {
        decorator.put(1.0, (short) 1);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void remove() {
        decorator.put(1.0, (short) 1);
        assertNotNull(decorator.remove(1.0));
        assertNull(decorator.remove(2.0));
    }

    @Test
    public void entrySet_size() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.entrySet().size());
    }

    @Test
    public void entrySet_isEmpty() {
        assertTrue(decorator.entrySet().isEmpty());
    }

    @Test
    public void entrySet_contains() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Map.Entry<Double, Short> entry = new AbstractMap.SimpleEntry<>(1.0, (short) 1);
        assertTrue(decorator.entrySet().contains(entry));
    }

    @Test
    public void entrySet_iterator_next() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Map.Entry<Double, Short> entry = decorator.entrySet().iterator().next();
        assertEquals((short) 1, (short) entry.getValue());
    }

    @Test
    public void containsValue() {
        decorator.put(1.0, (short) 1);
        assertTrue(decorator.containsValue((short) 1));
        assertFalse(decorator.containsValue((short) 2));
    }

    @Test
    public void containsKey() {
        decorator.put(1.0, (short) 1);
        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(2.0));
    }

    @Test
    public void size() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void isEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void putAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        decorator.putAll(map);
        assertEquals((short) 1, (short) decorator.get(1.0));
        assertEquals((short) 2, (short) decorator.get(2.0));
    }

    @Test
    public void wrapKey() {
        double key = 1.0;
        assertEquals(key, decorator.wrapKey(key), 0.0);
    }

    @Test
    public void unwrapKey() {
        Double key = 1.0;
        assertEquals(key, (Double) decorator.unwrapKey(key));
    }

    @Test
    public void wrapValue() {
        short value = 1;
        assertEquals(value, (short) decorator.wrapValue(value));
    }

    @Test
    public void unwrapValue() {
        Short value = 1;
        assertEquals(value, (Short) decorator.unwrapValue(value));
    }

    @Test
    public void readExternal() {
        // TODO: write test case for readExternal() method
    }

    @Test
    public void writeExternal() {
        // TODO: write test case for writeExternal() method
    }
}