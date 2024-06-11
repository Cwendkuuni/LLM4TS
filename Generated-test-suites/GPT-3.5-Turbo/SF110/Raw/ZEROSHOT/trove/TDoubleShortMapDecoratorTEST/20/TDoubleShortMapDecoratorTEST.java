import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator decorator;
    
    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortMap();
        decorator = new TDoubleShortMapDecorator(map);
    }
    
    @Test
    public void testPut() {
        Double key = 1.0;
        Short value = 2;

        Short result = decorator.put(key, value);

        assertEquals(value, result);
        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
    }
    
    @Test
    public void testGet() {
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        
        Short result = decorator.get(key);
        
        assertEquals(value, result);
    }
    
    @Test
    public void testClear() {
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        
        decorator.clear();
        
        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(value));
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        
        Short result = decorator.remove(key);
        
        assertEquals(value, result);
        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(value));
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testEntrySet() {
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        
        Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();
        
        assertEquals(1, entrySet.size());
        
        Map.Entry<Double, Short> entry = entrySet.iterator().next();
        
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }
    
    @Test
    public void testContainsValue() {
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        
        assertTrue(decorator.containsValue(value));
        
        assertFalse(decorator.containsValue(3));
    }
    
    @Test
    public void testContainsKey() {
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        
        assertTrue(decorator.containsKey(key));
        
        assertFalse(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey("key"));
    }
    
    @Test
    public void testSize() {
        Double key1 = 1.0;
        Short value1 = 2;
        Double key2 = 2.0;
        Short value2 = 3;
        
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short)2);
        map.put(2.0, (short)3);
        
        decorator.putAll(map);
        
        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertEquals((short)2, decorator.get(1.0).shortValue());
        assertEquals((short)3, decorator.get(2.0).shortValue());
    }
    
    @Test
    public void testWrapKey() {
        double key = 1.0;
        
        Double result = decorator.wrapKey(key);
        
        assertEquals(key, result.doubleValue(), 0);
    }
    
    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        
        double result = decorator.unwrapKey(key);
        
        assertEquals(key, result, 0);
    }
    
    @Test
    public void testWrapValue() {
        short value = 2;
        
        Short result = decorator.wrapValue(value);
        
        assertEquals(value, result.shortValue());
    }
    
    @Test
    public void testUnwrapValue() {
        Short value = 2;
        
        short result = decorator.unwrapValue(value);
        
        assertEquals(value, result);
    }
    
    @Test
    public void testReadExternal() {
        // Test externalization
    }
    
    @Test
    public void testWriteExternal() {
        // Test externalization
    }
}