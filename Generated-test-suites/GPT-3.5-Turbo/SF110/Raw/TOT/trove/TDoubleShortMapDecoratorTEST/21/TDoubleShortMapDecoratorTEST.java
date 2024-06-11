import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        mapDecorator.put(4.0, (short) 4);
        assertEquals(4, mapDecorator.getMap().get(4.0));
    }

    @Test
    public void testGet() {
        assertEquals(1, mapDecorator.get(1.0));
        assertEquals(2, mapDecorator.get(2.0));
        assertEquals(3, mapDecorator.get(3.0));
        assertNull(mapDecorator.get(4.0));
    }

    @Test
    public void testClear() {
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        assertNotNull(mapDecorator.remove(1.0));
        assertNull(mapDecorator.get(1.0));
        assertNull(mapDecorator.remove(4.0));
    }

    @Test
    public void testContainsKey() {
        assertTrue(mapDecorator.containsKey(1.0));
        assertTrue(mapDecorator.containsKey(2.0));
        assertTrue(mapDecorator.containsKey(3.0));
        assertFalse(mapDecorator.containsKey(4.0));
    }

    @Test
    public void testContainsValue() {
        assertTrue(mapDecorator.containsValue((short) 1));
        assertTrue(mapDecorator.containsValue((short) 2));
        assertTrue(mapDecorator.containsValue((short) 3));
        assertFalse(mapDecorator.containsValue((short) 4));
    }

    @Test
    public void testSize() {
        assertEquals(3, mapDecorator.size());
        mapDecorator.put(4.0, (short) 4);
        assertEquals(4, mapDecorator.size());
        mapDecorator.remove(4.0);
        assertEquals(3, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap anotherMap = new TDoubleShortHashMap();
        anotherMap.put(4.0, (short) 4);
        anotherMap.put(5.0, (short) 5);
        anotherMap.put(6.0, (short) 6);
        mapDecorator.putAll(anotherMap);

        assertEquals(4, mapDecorator.getMap().get(4.0));
        assertEquals(5, mapDecorator.getMap().get(5.0));
        assertEquals(6, mapDecorator.getMap().get(6.0));
    }

    // Edge case: Key is the no entry key and value is the no entry value
    @Test
    public void testWrapUnwrapKeyAndValue() {
        double key = mapDecorator.getMap().getNoEntryKey();
        Object wrappedKey = mapDecorator.wrapKey(key);
        double unwrappedKey = mapDecorator.unwrapKey(wrappedKey);
        assertEquals(key, unwrappedKey, 0.0001);

        short value = mapDecorator.getMap().getNoEntryValue();
        Object wrappedValue = mapDecorator.wrapValue(value);
        short unwrappedValue = mapDecorator.unwrapValue(wrappedValue);
        assertEquals(value, unwrappedValue);
    }
}