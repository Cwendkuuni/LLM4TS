import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import java.util.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        assertNull(decorator.put(key, value));
        assertEquals(value, decorator.get(key));
        
        // Testing edge case with null key
        key = null;
        Byte value2 = 3;
        assertNull(decorator.put(key, value2));
        assertEquals(value2, decorator.get(key));
        
        // Testing edge case with null value
        key = 4;
        value = null;
        assertNull(decorator.put(key, value));
        assertNull(decorator.get(key));
        
        // Testing error scenario with duplicate key
        key = 1;
        value = 5;
        assertEquals(value, decorator.put(key, value));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testGet() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
        
        // Testing edge case with null key
        key = null;
        assertNull(decorator.get(key));
        
        // Testing error scenario with non-existent key
        key = 3;
        assertNull(decorator.get(key));
    }
    
    @Test
    public void testClear() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);
        decorator.clear();
        assertTrue(decorator.isEmpty());
        
        // Testing edge case with empty map
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);
        assertEquals(value, decorator.remove(key));
        assertNull(decorator.get(key));
        
        // Testing edge case with null key
        key = null;
        assertNull(decorator.remove(key));
        
        // Testing error scenario with non-existent key
        key = 3;
        assertNull(decorator.remove(key));
    }
    
    @Test
    public void testEntrySet() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);
        
        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertNotNull(entrySet);
        assertEquals(1, entrySet.size());
        
        Map.Entry<Short, Byte> entry = entrySet.iterator().next();
        assertNotNull(entry);
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
        
        // Testing edge case with empty map
        decorator.clear();
        entrySet = decorator.entrySet();
        assertNotNull(entrySet);
        assertEquals(0, entrySet.size());
        
        // Testing error scenario with modification of entrySet
        try {
            entrySet.add(new AbstractMap.SimpleEntry<Short, Byte>(1, 2));
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testContainsValue() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertTrue(decorator.containsValue(value));
        
        // Testing edge case with null value
        assertFalse(decorator.containsValue(null));
        
        // Testing error scenario with non-existent value
        assertFalse(decorator.containsValue(3));
    }
    
    @Test
    public void testContainsKey() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        
        // Testing edge case with null key
        assertFalse(decorator.containsKey(null));
        
        // Testing error scenario with non-existent key
        assertFalse(decorator.containsKey(3));
    }
    
    @Test
    public void testSize() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertEquals(1, decorator.size());
        
        // Testing edge case with empty map
        decorator.clear();
        assertEquals(0, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertFalse(decorator.isEmpty());
        
        // Testing edge case with empty map
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 2);
        map.put((short) 3, (byte) 4);
        decorator.putAll(map);
        assertEquals(map.size(), decorator.size());
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 3));
        assertEquals((byte) 2, decorator.get((short) 1).byteValue());
        assertEquals((byte) 4, decorator.get((short) 3).byteValue());
        
        // Testing edge case with empty map
        decorator.clear();
        decorator.putAll(map);
        assertEquals(map.size(), decorator.size());
        
        // Testing error scenario with duplicate keys
        map.put((short) 1, (byte) 5);
        decorator.putAll(map);
        assertEquals(map.size(), decorator.size());
        assertEquals((byte) 5, decorator.get((short) 1).byteValue());
    }
    
    @Test
    public void testWrapKey() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Short key = 1;
        assertEquals((short) key, decorator.unwrapKey(decorator.wrapKey((short) key)));
        
        // Testing null key
        key = null;
        assertNull(decorator.wrapKey(decorator.unwrapKey(key)));
    }
    
    @Test
    public void testWrapValue() {
        // Testing typical use case
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value = 1;
        assertEquals((byte) value, decorator.unwrapValue(decorator.wrapValue((byte) value)));
        
        // Testing null value
        value = null;
        assertNull(decorator.wrapValue(decorator.unwrapValue(value)));
    }
}