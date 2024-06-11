import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test putting a value
        decorator.put(1.0, (short) 1);
        assertEquals((short) 1, decorator.get(1.0));

        // Test putting a null key
        decorator.put(null, (short) 2);
        assertEquals((short) 2, decorator.get(null));

        // Test putting a null value
        decorator.put(2.0, null);
        assertNull(decorator.get(2.0));
    }

    @Test
    public void testGet() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test getting an existing value
        assertEquals((short) 1, decorator.get(1.0));

        // Test getting a null key
        assertNull(decorator.get(null));

        // Test getting a non-existing value
        assertNull(decorator.get(2.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        // Test removing an existing key
        assertEquals((short) 1, decorator.remove(1.0));
        assertNull(decorator.get(1.0));

        // Test removing a null key
        assertNull(decorator.remove(null));

        // Test removing a non-existing key
        assertNull(decorator.remove(2.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Set<Entry<Double, Short>> entrySet = decorator.entrySet();

        // Test size
        assertEquals(2, entrySet.size());

        // Test isEmpty
        assertFalse(entrySet.isEmpty());

        // Test contains
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 1)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 2)));

        // Test iterator
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey().doubleValue(), 0.0001);
        assertEquals((short) 1, entry.getValue().shortValue());

        // Test remove
        iterator.remove();
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 1)));

        // Test add, remove and clear
        Entry<Double, Short> newEntry = new AbstractMap.SimpleEntry<>(3.0, (short) 3);
        assertTrue(entrySet.add(newEntry));
        assertTrue(entrySet.contains(newEntry));
        assertTrue(entrySet.remove(newEntry));
        assertFalse(entrySet.contains(newEntry));
        entrySet.clear();
        assertTrue(entrySet.isEmpty());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertTrue(decorator.containsValue((short) 1));
        assertFalse(decorator.containsValue((short) 2));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertEquals(1, decorator.size());

        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());

        decorator.remove(1.0);
        assertEquals(1, decorator.size());

        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());

        decorator.remove(1.0);
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Map<Double, Short> testMap = new HashMap<>();
        testMap.put(1.0, (short) 1);
        testMap.put(2.0, (short) 2);
        testMap.put(3.0, (short) 3);

        decorator.putAll(testMap);

        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertEquals((short) 3, decorator.get(3.0));
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertEquals(1.0, decorator.wrapKey(1.0).doubleValue(), 0.0001);
        assertEquals(5.0, decorator.wrapKey(5.0).doubleValue(), 0.0001);
    }

    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertEquals(1.0, decorator.unwrapKey(1.0), 0.0001);
        assertEquals(5.0, decorator.unwrapKey(5.0), 0.0001);
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertEquals((short) 1, decorator.wrapValue((short) 1).shortValue());
        assertEquals((short) 5, decorator.wrapValue((short) 5).shortValue());
    }

    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertEquals((short) 1, decorator.unwrapValue((short) 1));
        assertEquals((short) 5, decorator.unwrapValue((short) 5));
    }
}