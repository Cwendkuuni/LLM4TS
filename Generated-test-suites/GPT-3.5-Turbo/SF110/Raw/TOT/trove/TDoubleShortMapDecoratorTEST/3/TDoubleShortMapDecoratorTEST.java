import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Short result1 = decorator.put(2.0, (short) 3);
        assertNull(result1);
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsValue((short) 3));
        
        // Edge case
        Short result2 = decorator.put(Double.POSITIVE_INFINITY, Short.MAX_VALUE);
        assertNull(result2);
        assertTrue(decorator.containsKey(Double.POSITIVE_INFINITY));
        assertTrue(decorator.containsValue(Short.MAX_VALUE));
        
        // Error scenario
        Short result3 = decorator.put(null, null);
        assertNull(result3);
        assertFalse(decorator.containsKey(null));
        assertFalse(decorator.containsValue(null));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        decorator.put(2.0, (short) 3);
        Short result1 = decorator.get(2.0);
        assertNotNull(result1);
        assertEquals((short) 3, result1.shortValue());
        
        // Edge case
        decorator.put(Double.POSITIVE_INFINITY, Short.MAX_VALUE);
        Short result2 = decorator.get(Double.POSITIVE_INFINITY);
        assertNotNull(result2);
        assertEquals(Short.MAX_VALUE, result2.shortValue());
        
        // Error scenario
        Short result3 = decorator.get(null);
        assertNull(result3);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(2.0, (short) 3);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(2.0, (short) 3);
        
        // Typical use case
        Short result1 = decorator.remove(2.0);
        assertNotNull(result1);
        assertEquals((short) 3, result1.shortValue());
        assertFalse(decorator.containsKey(2.0));
        assertFalse(decorator.containsValue((short) 3));
        
        // Edge case
        decorator.put(Double.POSITIVE_INFINITY, Short.MAX_VALUE);
        Short result2 = decorator.remove(Double.POSITIVE_INFINITY);
        assertNotNull(result2);
        assertEquals(Short.MAX_VALUE, result2.shortValue());
        assertFalse(decorator.containsKey(Double.POSITIVE_INFINITY));
        assertFalse(decorator.containsValue(Short.MAX_VALUE));
        
        // Error scenario
        Short result3 = decorator.remove(null);
        assertNull(result3);
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        decorator.put(2.0, (short) 3);
        decorator.put(4.0, (short) 5);
        decorator.put(6.0, (short) 7);
        
        // Size
        assertEquals(3, decorator.entrySet().size());
        
        // Contains
        assertTrue(decorator.entrySet().contains(new SimpleEntry<>(2.0, (short) 3)));
        assertTrue(decorator.entrySet().contains(new SimpleEntry<>(4.0, (short) 5)));
        assertTrue(decorator.entrySet().contains(new SimpleEntry<>(6.0, (short) 7)));
        
        // Remove
        assertTrue(decorator.entrySet().remove(new SimpleEntry<>(2.0, (short) 3)));
        assertFalse(decorator.entrySet().remove(new SimpleEntry<>(8.0, (short) 9)));
        
        // Clear
        decorator.entrySet().clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        decorator.put(2.0, (short) 3);
        assertTrue(decorator.containsValue((short) 3));
        assertFalse(decorator.containsValue((short) 4));
        
        // Edge case
        decorator.put(Double.POSITIVE_INFINITY, Short.MAX_VALUE);
        assertTrue(decorator.containsValue(Short.MAX_VALUE));
        
        // Error scenario
        assertFalse(decorator.containsValue(null));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        decorator.put(2.0, (short) 3);
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(4.0));
        
        // Edge case
        decorator.put(Double.POSITIVE_INFINITY, Short.MAX_VALUE);
        assertTrue(decorator.containsKey(Double.POSITIVE_INFINITY));
        
        // Error scenario
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        decorator.put(2.0, (short) 3);
        decorator.put(4.0, (short) 5);
        assertEquals(2, decorator.size());
        
        // Edge case
        decorator.put(Double.POSITIVE_INFINITY, Short.MAX_VALUE);
        assertEquals(3, decorator.size());
        
        // Error scenario
        decorator.put(null, null);
        assertEquals(3, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertTrue(decorator.isEmpty());
        
        decorator.put(2.0, (short) 3);
        assertFalse(decorator.isEmpty());
        
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Typical use case
        Map<Double, Short> map1 = new HashMap<>();
        map1.put(2.0, (short) 3);
        map1.put(4.0, (short) 5);
        decorator.putAll(map1);
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsKey(4.0));
        assertTrue(decorator.containsValue((short) 3));
        assertTrue(decorator.containsValue((short) 5));
        assertEquals(map1.size(), decorator.size());
        
        // Edge case
        Map<Double, Short> map2 = new HashMap<>();
        map2.put(Double.POSITIVE_INFINITY, Short.MAX_VALUE);
        decorator.putAll(map2);
        assertTrue(decorator.containsKey(Double.POSITIVE_INFINITY));
        assertTrue(decorator.containsValue(Short.MAX_VALUE));
        assertEquals(map1.size() + map2.size(), decorator.size());
        
        // Error scenario
        Map<Double, Short> map3 = new HashMap<>();
        map3.put(null, null);
        decorator.putAll(map3);
        assertFalse(decorator.containsKey(null));
        assertFalse(decorator.containsValue(null));
        assertEquals(map1.size() + map2.size(), decorator.size());
    }
    
    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        double key1 = 2.0;
        Double wrappedKey1 = decorator.wrapKey(key1);
        assertNotNull(wrappedKey1);
        assertEquals(key1, wrappedKey1.doubleValue(), 0);
        
        double key2 = Double.POSITIVE_INFINITY;
        Double wrappedKey2 = decorator.wrapKey(key2);
        assertNotNull(wrappedKey2);
        assertEquals(key2, wrappedKey2.doubleValue(), 0);
    }
    
    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        Double key1 = 2.0;
        double unwrappedKey1 = decorator.unwrapKey(key1);
        assertEquals(key1.doubleValue(), unwrappedKey1, 0);
        
        Double key2 = Double.POSITIVE_INFINITY;
        double unwrappedKey2 = decorator.unwrapKey(key2);
        assertEquals(key2.doubleValue(), unwrappedKey2, 0);
    }
    
    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        short value1 = 3;
        Short wrappedValue1 = decorator.wrapValue(value1);
        assertNotNull(wrappedValue1);
        assertEquals(value1, wrappedValue1.shortValue());
        
        short value2 = Short.MAX_VALUE;
        Short wrappedValue2 = decorator.wrapValue(value2);
        assertNotNull(wrappedValue2);
        assertEquals(value2, wrappedValue2.shortValue());
    }
    
    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        Short value1 = 3;
        short unwrappedValue1 = decorator.unwrapValue(value1);
        assertEquals(value1.shortValue(), unwrappedValue1);
        
        Short value2 = Short.MAX_VALUE;
        short unwrappedValue2 = decorator.unwrapValue(value2);
        assertEquals(value2.shortValue(), unwrappedValue2);
    }
    
    @Test
    public void testReadExternal() {
        // Test reading external data
    }
    
    @Test
    public void testWriteExternal() {
        // Test writing external data
    }

}