import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator decorator;
    
    @Before
    public void setUp() {
        decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
    }
    
    @Test
    public void testPutAndGet() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        Short value1 = decorator.get(1.0);
        Short value2 = decorator.get(2.0);
        
        assertEquals((short) 1, value1.shortValue());
        assertEquals((short) 2, value2.shortValue());
    }
    
    @Test
    public void testPutNullAndGetNull() {
        decorator.put(null, null);
        
        Short value = decorator.get(null);
        
        assertNull(value);
    }
    
    @Test
    public void testClear() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        decorator.clear();
        
        assertEquals(0, decorator.size());
    }
    
    @Test
    public void testRemove() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        Short value1 = decorator.remove(1.0);
        Short value2 = decorator.remove(2.0);
        Short value3 = decorator.remove(3.0);
        
        assertEquals((short) 1, value1.shortValue());
        assertEquals((short) 2, value2.shortValue());
        assertNull(value3);
    }
    
    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short) 1)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Double, Short>(2.0, (short) 2)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<Double, Short>(3.0, (short) 3)));
    }
    
    @Test
    public void testContainsValue() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertTrue(decorator.containsValue((short) 1));
        assertFalse(decorator.containsValue((short) 3));
    }
    
    @Test
    public void testContainsKey() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(3.0));
    }
    
    @Test
    public void testSize() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        
        decorator.put(1.0, (short) 1);
        
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        decorator.putAll(map);
        
        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.0).shortValue());
    }
    
    @Test
    public void testWrapKey() {
        double key = 1.0;
        
        Double wrappedKey = decorator.wrapKey(key);
        
        assertEquals(key, wrappedKey, 0.001);
    }
    
    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        
        double unwrappedKey = decorator.unwrapKey(key);
        
        assertEquals(key, unwrappedKey, 0.001);
    }
    
    @Test
    public void testWrapValue() {
        short value = 1;
        
        Short wrappedValue = decorator.wrapValue(value);
        
        assertEquals(value, wrappedValue.shortValue());
    }
    
    @Test
    public void testUnwrapValue() {
        Short value = 1;
        
        short unwrappedValue = decorator.unwrapValue(value);
        
        assertEquals(value, unwrappedValue);
    }
}