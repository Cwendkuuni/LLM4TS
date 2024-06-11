import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.TDoubleShortMapDecorator;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator tDoubleShortMapDecorator;

    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortMap();
        tDoubleShortMapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        Double key = 2.0;
        Short value = 10;

        assertNull(tDoubleShortMapDecorator.put(key, value));
        assertEquals(1, tDoubleShortMapDecorator.size());
        assertTrue(tDoubleShortMapDecorator.containsKey(key));
        assertTrue(tDoubleShortMapDecorator.containsValue(value));
        assertEquals(value, tDoubleShortMapDecorator.get(key));
    }

    @Test
    public void testGet() {
        Double key = 2.0;
        Short value = 10;

        tDoubleShortMapDecorator.put(key, value);

        assertEquals(value, tDoubleShortMapDecorator.get(key));
    }

    @Test
    public void testClear() {
        Double key = 2.0;
        Short value = 10;

        tDoubleShortMapDecorator.put(key, value);
        tDoubleShortMapDecorator.clear();

        assertEquals(0, tDoubleShortMapDecorator.size());
        assertFalse(tDoubleShortMapDecorator.containsKey(key));
        assertFalse(tDoubleShortMapDecorator.containsValue(value));
        assertNull(tDoubleShortMapDecorator.get(key));
    }

    @Test
    public void testRemove() {
        Double key = 2.0;
        Short value = 10;

        tDoubleShortMapDecorator.put(key, value);

        assertEquals(value, tDoubleShortMapDecorator.remove(key));
        assertEquals(0, tDoubleShortMapDecorator.size());
        assertFalse(tDoubleShortMapDecorator.containsKey(key));
        assertFalse(tDoubleShortMapDecorator.containsValue(value));
        assertNull(tDoubleShortMapDecorator.get(key));
    }

    @Test
    public void testEntrySet() {
        Double key = 2.0;
        Short value = 10;

        tDoubleShortMapDecorator.put(key, value);

        Set<Map.Entry<Double, Short>> entrySet = tDoubleShortMapDecorator.entrySet();
        assertEquals(1, entrySet.size());

        Map.Entry<Double, Short> entry = entrySet.iterator().next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        Double key = 2.0;
        Short value = 10;

        tDoubleShortMapDecorator.put(key, value);

        assertTrue(tDoubleShortMapDecorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        Double key = 2.0;
        Short value = 10;

        tDoubleShortMapDecorator.put(key, value);

        assertTrue(tDoubleShortMapDecorator.containsKey(key));
    }

    @Test
    public void testSize() {
        Double key = 2.0;
        Short value = 10;

        tDoubleShortMapDecorator.put(key, value);

        assertEquals(1, tDoubleShortMapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tDoubleShortMapDecorator.isEmpty());

        Double key = 2.0;
        Short value = 10;

        tDoubleShortMapDecorator.put(key, value);

        assertFalse(tDoubleShortMapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new AbstractMap<Double, Short>() {
            @Override
            public Set<Entry<Double, Short>> entrySet() {
                return null;
            }
        };
        map.put(2.0, (short) 10);
        map.put(3.0, (short) 20);

        tDoubleShortMapDecorator.putAll(map);

        assertEquals(map.size(), tDoubleShortMapDecorator.size());
        assertTrue(tDoubleShortMapDecorator.containsKey(2.0));
        assertTrue(tDoubleShortMapDecorator.containsValue((short) 10));
        assertTrue(tDoubleShortMapDecorator.containsKey(3.0));
        assertTrue(tDoubleShortMapDecorator.containsValue((short) 20));
    }

    @Test
    public void testWrapKey() {
        double key = 2.0;
        Double wrappedKey = tDoubleShortMapDecorator.wrapKey(key);
        assertEquals(Double.valueOf(key), wrappedKey);
    }

    @Test
    public void testUnwrapKey() {
        Double key = 2.0;
        double unwrappedKey = tDoubleShortMapDecorator.unwrapKey(key);
        assertEquals(key.doubleValue(), unwrappedKey, 0);
    }

    @Test
    public void testWrapValue() {
        short value = 10;
        Short wrappedValue = tDoubleShortMapDecorator.wrapValue(value);
        assertEquals(Short.valueOf(value), wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        Short value = 10;
        short unwrappedValue = tDoubleShortMapDecorator.unwrapValue(value);
        assertEquals(value.shortValue(), unwrappedValue);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        // TODO: Implement test
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        // TODO: Implement test
    }
}