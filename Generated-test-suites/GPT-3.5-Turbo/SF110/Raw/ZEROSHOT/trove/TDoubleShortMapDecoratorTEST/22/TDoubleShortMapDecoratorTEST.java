import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator decorator;

    @Before
    public void setup() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        decorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        Double key = 1.0;
        Short value = 2;
        Short result = decorator.put(key, value);

        assertNull(result);

        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testGet() {
        Double key = 1.0;
        Short value = 2;

        decorator.put(key, value);

        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testClear() {
        Double key = 1.0;
        Short value = 2;

        decorator.put(key, value);
        decorator.clear();

        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(value));
        assertNull(decorator.get(key));
    }

    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 2;

        decorator.put(key, value);
        Short result = decorator.remove(key);

        assertEquals(value, result);
        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(value));
        assertNull(decorator.get(key));
    }

    @Test
    public void testSize() {
        Double key1 = 1.0;
        Short value1 = 2;
        Double key2 = 3.0;
        Short value2 = 4;

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());

        Double key = 1.0;
        Short value1 = 2;

        decorator.put(key, value1);

        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Double key1 = 1.0;
        Short value1 = 2;
        Double key2 = 3.0;
        Short value2 = 4;

        Map<Double, Short> map = new TDoubleShortHashMap();
        map.put(key1, value1);
        map.put(key2, value2);

        decorator.putAll(map);

        assertEquals(value1, decorator.get(key1));
        assertEquals(value2, decorator.get(key2));
    }

    @Test
    public void testWrapKey() {
        double key = 1.0;
        Double wrappedKey = decorator.wrapKey(key);

        assertEquals(key, wrappedKey, 0.0001);
    }

    @Test
    public void testUnwrapKey() {
        Double wrappedKey = 1.0;
        double key = decorator.unwrapKey(wrappedKey);

        assertEquals(wrappedKey, key, 0.0001);
    }

    @Test
    public void testWrapValue() {
        short value = 2;
        Short wrappedValue = decorator.wrapValue(value);

        assertEquals(value, (short) wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        Short wrappedValue = 2;
        short value = decorator.unwrapValue(wrappedValue);

        assertEquals(wrappedValue, (Short) value);
    }
}