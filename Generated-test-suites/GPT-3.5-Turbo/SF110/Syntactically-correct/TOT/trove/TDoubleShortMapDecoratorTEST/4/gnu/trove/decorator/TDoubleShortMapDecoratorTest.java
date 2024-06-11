import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {
    
    TDoubleShortMapDecorator mapDecorator;
    TDoubleShortMap map;
    
    @Before
    public void setUp() {
        map = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }
    
    @Test
    public void testPutAndGet_NormalKeysAndValues() {
        // test normal keys and values
        mapDecorator.put(0.1, (short) 1);
        mapDecorator.put(0.2, (short) 2);
        mapDecorator.put(0.3, (short) 3);

        assertEquals((short) 1, mapDecorator.get(0.1).shortValue());
        assertEquals((short) 2, mapDecorator.get(0.2).shortValue());
        assertEquals((short) 3, mapDecorator.get(0.3).shortValue());
    }

    @Test
    public void testPutAndGet_NullKeys() {
        // test null keys
        mapDecorator.put(null, (short) 1);
        mapDecorator.put(null, (short) 2);

        assertEquals((short) 1, mapDecorator.get(null).shortValue());
        assertEquals((short) 2, mapDecorator.get(null).shortValue());
    }

    @Test
    public void testPutAndGet_NullValues() {
        // test null values
        mapDecorator.put(0.1, null);
        mapDecorator.put(0.2, null);

        assertNull(mapDecorator.get(0.1));
        assertNull(mapDecorator.get(0.2));
    }

    @Test
    public void testPutAndGet_NullKeysAndValues() {
        // test null keys and values
        mapDecorator.put(null, null);
        mapDecorator.put(null, null);

        assertNull(mapDecorator.get(null));
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testPutAndGet_NoEntryKeysAndValues() {
        // test no entry keys and values
        mapDecorator.put(map.getNoEntryKey(), (short) 1);
        mapDecorator.put(map.getNoEntryKey(), (short) 2);

        assertEquals((short) 1, mapDecorator.get(map.getNoEntryKey()).shortValue());
        assertEquals((short) 2, mapDecorator.get(map.getNoEntryKey()).shortValue());
    }

    @Test
    public void testRemove_NormalKeys() {
        // test remove normal keys
        mapDecorator.put(0.1, (short) 1);
        mapDecorator.put(0.2, (short) 2);
        mapDecorator.put(0.3, (short) 3);

        assertEquals((short) 1, mapDecorator.remove(0.1).shortValue());
        assertNull(mapDecorator.get(0.1));

        assertNull(mapDecorator.remove(0.4));
    }

    @Test
    public void testRemove_NullKeys() {
        // test remove null keys
        mapDecorator.put(null, (short) 1);
        mapDecorator.put(null, (short) 2);

        assertEquals((short) 1, mapDecorator.remove(null).shortValue());
        assertNull(mapDecorator.get(null));

        assertNull(mapDecorator.remove(null));
    }

    @Test
    public void testSize() {
        // test size
        assertEquals(0, mapDecorator.size());
        
        mapDecorator.put(0.1, (short) 1);
        mapDecorator.put(0.2, (short) 2);
        mapDecorator.put(0.3, (short) 3);

        assertEquals(3, mapDecorator.size());

        mapDecorator.put(0.4, (short) 4);
        mapDecorator.put(0.5, (short) 5);

        assertEquals(5, mapDecorator.size());
        
        mapDecorator.remove(0.5);
        
        assertEquals(4, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        // test isEmpty
        assertTrue(mapDecorator.isEmpty());

        mapDecorator.put(0.1, (short) 1);
        mapDecorator.put(0.2, (short) 2);

        assertFalse(mapDecorator.isEmpty());

        mapDecorator.remove(0.1);
        mapDecorator.remove(0.2);

        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        // test containsKey
        assertFalse(mapDecorator.containsKey(0.1));

        mapDecorator.put(0.1, (short) 1);
        mapDecorator.put(0.2, (short) 2);

        assertTrue(mapDecorator.containsKey(0.1));
        assertTrue(mapDecorator.containsKey(0.2));
        assertFalse(mapDecorator.containsKey(0.3));

        mapDecorator.remove(0.1);

        assertFalse(mapDecorator.containsKey(0.1));
    }

    @Test
    public void testContainsValue() {
        // test containsValue
        assertFalse(mapDecorator.containsValue((short) 1));

        mapDecorator.put(0.1, (short) 1);
        mapDecorator.put(0.2, (short) 2);

        assertTrue(mapDecorator.containsValue((short) 1));
        assertTrue(mapDecorator.containsValue((short) 2));
        assertFalse(mapDecorator.containsValue((short) 3));

        mapDecorator.remove(0.1);

        assertFalse(mapDecorator.containsValue((short) 1));
    }

    @Test
    public void testPutAll() {
        // test putAll
        Map<Double, Short> inputMap = new HashMap<>();
        inputMap.put(0.1, (short) 1);
        inputMap.put(0.2, (short) 2);

        mapDecorator.putAll(inputMap);

        assertTrue(mapDecorator.containsKey(0.1) && mapDecorator.containsKey(0.2));
    }
}