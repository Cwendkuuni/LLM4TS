package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator decorator;
    
    @Before
    public void setUp() {
        decorator = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        Double key = 1.0;
        Short value = 2;

        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testPutNullKey() {
        Double key = null;
        Short value = 2;

        decorator.put(key, value);

        assertTrue(decorator.containsKey(null));
        assertTrue(decorator.containsValue(value));
        assertEquals(value, decorator.get(null));
    }

    @Test
    public void testPutNullValue() {
        Double key = 1.0;
        Short value = null;

        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(null));
        assertEquals(null, decorator.get(key));
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short)2);
        map.put(2.0, (short)3);

        decorator.putAll(map);

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsValue((short)2));
        assertTrue(decorator.containsValue((short)3));
        assertEquals((short)2, (short)decorator.get(1.0));
        assertEquals((short)3, (short)decorator.get(2.0));
    }

    @Test
    public void testClear() {
        decorator.put(1.0, (short)2);
        decorator.put(2.0, (short)3);

        decorator.clear();

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 2;

        decorator.put(key, value);

        Short removedValue = decorator.remove(key);

        assertTrue(decorator.isEmpty());
        assertEquals(value, removedValue);
    }

    @Test
    public void testRemoveNullKey() {
        Double key = null;
        Short value = 2;

        decorator.put(key, value);

        Short removedValue = decorator.remove(key);

        assertTrue(decorator.isEmpty());
        assertEquals(value, removedValue);
    }

    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short)2);
        decorator.put(2.0, (short)3);

        assertEquals(2, decorator.entrySet().size());
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        
        decorator.put(1.0, (short)2);

        assertTrue(!decorator.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, decorator.size());
        
        decorator.put(1.0, (short)2);

        assertEquals(1, decorator.size());
    }
    
}