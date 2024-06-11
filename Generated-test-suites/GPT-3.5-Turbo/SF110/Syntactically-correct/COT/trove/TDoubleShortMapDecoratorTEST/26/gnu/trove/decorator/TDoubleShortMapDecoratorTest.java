
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Test putting a key-value pair and getting the value
        map.put(1.0, (short) 10);
        assertEquals((short) 10, map.get(1.0).shortValue());

        // Test putting a null key and value
        map.put(null, null);
        assertNull(map.get(null));

        // Test putting a key with already assigned value
        map.put(2.0, (short) 20);
        assertEquals((short) 20, map.get(2.0).shortValue());
        map.put(2.0, (short) 30);
        assertEquals((short) 30, map.get(2.0).shortValue());
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Test getting a value for an existing key
        map.put(1.0, (short) 10);
        assertEquals((short) 10, map.get(1.0).shortValue());
        
        // Test getting a value for a non-existing key
        assertNull(map.get(2.0));
        
        // Test getting a value for a null key
        map.put(null, null);
        assertNull(map.get(null));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Test clearing an empty map
        map.clear();
        assertTrue(map.isEmpty());
        
        // Test clearing a non-empty map
        map.put(1.0, (short) 10);
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Test removing a key-value pair and getting the value
        map.put(1.0, (short) 10);
        assertEquals((short) 10, map.remove(1.0).shortValue());
        assertNull(map.get(1.0));

        // Test removing a non-existing key and getting null
        assertNull(map.remove(2.0));

        // Test removing a null key
        map.put(null, null);
        assertNull(map.remove(null));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Test getting the entry set of an empty map
        assertTrue(map.entrySet().isEmpty());
        
        // Test getting the entry set of a non-empty map
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        assertEquals(2, map.entrySet().size());
        assertTrue(map.entrySet().contains(new AbstractMap.SimpleEntry<>(1.0, (short) 10)));
        assertTrue(map.entrySet().contains(new AbstractMap.SimpleEntry<>(2.0, (short) 20)));
        assertFalse(map.entrySet().contains(new AbstractMap.SimpleEntry<>(3.0, (short) 30)));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Test checking if a value exists in an empty map
        assertFalse(map.containsValue((short) 10));
        
        // Test checking if a value exists in a map with values
        map.put(1.0, (short) 10);
        assertTrue(map.containsValue((short) 10));
        assertFalse(map.containsValue((short) 20));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Test checking if a key exists in an empty map
        assertFalse(map.containsKey(1.0));
        
        // Test checking if a key exists in a map with keys
        map.put(1.0, (short) 10);
        assertTrue(map.containsKey(1.0));
        assertFalse(map.containsKey(2.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Test getting the size of an empty map
        assertEquals(0, map.size());
        
        // Test getting the size of a non-empty map
        map.put(1.0, (short) 10);
        assertEquals(1, map.size());
        map.put(2.0, (short) 20);
        assertEquals(2, map.size());
        map.remove(1.0);
        assertEquals(1, map.size());
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Map<Double, Short> other = new HashMap<>();
        other.put(1.0, (short) 10);
        other.put(2.0, (short) 20);
        
        // Test putting all key-value pairs from another map
        map.putAll(other);
        assertEquals((short) 10, map.get(1.0).shortValue());
        assertEquals((short) 20, map.get(2.0).shortValue());
    }
}
