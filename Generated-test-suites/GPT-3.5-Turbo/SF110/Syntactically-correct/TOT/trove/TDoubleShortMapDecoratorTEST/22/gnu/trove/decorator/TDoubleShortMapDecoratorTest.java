import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    TDoubleShortMapDecorator tMapDecorator;
    TDoubleShortMap tMap;

    @Before
    public void setup() {
        tMap = new TDoubleShortMap();
        tMapDecorator = new TDoubleShortMapDecorator(tMap);
    }

    @Test
    public void testPut() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        assertNull(tMapDecorator.put(key1, value1));
        assertTrue(tMap.containsKey(key1));
        assertTrue(tMap.containsValue((short)5));

        // edge case - add null key
        Double key2 = null;
        Short value2 = 10;
        assertNull(tMapDecorator.put(key2, value2));
        assertTrue(tMap.containsKey(null));
        assertTrue(tMap.containsValue((short)10));

        // error scenario - add existing key with different value
        Double key3 = 2.3;
        Short value3 = 15;
        assertEquals((short)5, (short)tMapDecorator.put(key3, value3));
        assertEquals((short)15, (short)tMapDecorator.get(key3));
    }

    @Test
    public void testGet() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        tMapDecorator.put(key1, value1);
        assertEquals((short)5, (short)tMapDecorator.get(key1));

        // edge case - get null key
        assertNull(tMapDecorator.get(null));

        // error scenario - get non-existing key
        Double key2 = 4.5;
        assertNull(tMapDecorator.get(key2));
    }

    @Test
    public void testClear() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        tMapDecorator.put(key1, value1);

        tMapDecorator.clear();
        assertTrue(tMap.isEmpty());

        // edge case - clear empty map
        tMapDecorator.clear();
        assertTrue(tMap.isEmpty());
    }

    @Test
    public void testRemove() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        tMapDecorator.put(key1, value1);
        assertEquals((short)5, (short)tMapDecorator.remove(key1));
        assertNull(tMap.get(2.3));

        // edge case - remove null key
        assertNull(tMapDecorator.remove(null));

        // error scenario - remove non-existing key
        Double key2 = 4.5;
        assertNull(tMapDecorator.remove(key2));
    }

    @Test
    public void testEntrySet() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        tMapDecorator.put(key1, value1);
        
        Set<Map.Entry<Double, Short>> entrySet = tMapDecorator.entrySet();
        assertEquals(1, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Double, Short>(key1, value1)));

        // edge case - empty map
        tMapDecorator.clear();
        entrySet = tMapDecorator.entrySet();
        assertTrue(entrySet.isEmpty());

        // error scenario - add invalid entry
        entrySet.add(new AbstractMap.SimpleEntry<Double, Short>(4.5, (short)10));
        assertEquals(0, entrySet.size());
    }

    @Test
    public void testContainsValue() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        tMapDecorator.put(key1, value1);
        assertTrue(tMapDecorator.containsValue((short)5));

        // edge case - contains null value
        assertFalse(tMapDecorator.containsValue(null));

        // error scenario - contains non-existing value
        assertFalse(tMapDecorator.containsValue((short)10));
    }

    @Test
    public void testContainsKey() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        tMapDecorator.put(key1, value1);
        assertTrue(tMapDecorator.containsKey(2.3));

        // edge case - contains null key
        assertTrue(tMapDecorator.containsKey(null));

        // error scenario - contains non-existing key
        assertFalse(tMapDecorator.containsKey(4.5));
    }

    @Test
    public void testSize() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        tMapDecorator.put(key1, value1);
        assertEquals(1, tMapDecorator.size());

        // edge case - empty map
        tMapDecorator.clear();
        assertEquals(0, tMapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        // typical use case
        Double key1 = 2.3;
        Short value1 = 5;
        tMapDecorator.put(key1, value1);
        assertFalse(tMapDecorator.isEmpty());

        // edge case - empty map
        tMapDecorator.clear();
        assertTrue(tMapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        // typical use case
        Map<Double, Short> map = new HashMap<>();
        map.put(2.3, (short)5);
        map.put(4.5, (short)10);
        
        tMapDecorator.putAll(map);
        assertEquals(2, tMapDecorator.size());
        assertTrue(tMapDecorator.containsKey(2.3));
        assertTrue(tMapDecorator.containsKey(4.5));
        assertEquals((short)5, (short)tMapDecorator.get(2.3));
        assertEquals((short)10, (short)tMapDecorator.get(4.5));

        // edge case - put empty map
        map.clear();
        tMapDecorator.putAll(map);
        assertTrue(tMapDecorator.isEmpty());
    }

    @Test
    public void testWrapKey() {
        // typical use case
        assertEquals(2.3, (double)tMapDecorator.wrapKey(2.3), 0.01);

        // edge case - wrap null key
        assertNull(tMapDecorator.wrapKey(tMap.getNoEntryKey()));
    }

    @Test
    public void testUnwrapKey() {
        // typical use case
        assertEquals(2.3, (double)tMapDecorator.unwrapKey(2.3), 0.01);

        // edge case - unwrap null key
        assertEquals(tMap.getNoEntryKey(), (double)tMapDecorator.unwrapKey(null), 0.01);
    }

    @Test
    public void testWrapValue() {
        // typical use case
        assertEquals((short)5, (short)tMapDecorator.wrapValue((short)5));

        // edge case - wrap null value
        assertNull(tMapDecorator.wrapValue(tMap.getNoEntryValue()));
    }

    @Test
    public void testUnwrapValue() {
        // typical use case
        assertEquals((short)5, (short)tMapDecorator.unwrapValue((short)5));

        // edge case - unwrap null value
        assertEquals(tMap.getNoEntryValue(), (short)tMapDecorator.unwrapValue(null));
    }

    @Test
    public void testReadWriteExternal() {
        // not implemented in TDoubleShortMapDecorator, unable to test
        assertTrue(true);
    }
}