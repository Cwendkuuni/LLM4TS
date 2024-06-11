
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator map;
    
    @Before
    public void setUp() {
        map = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void putAndGetShouldReturnCorrectValues() {
        Double key = 1.0;
        Short value = 2;
        
        Short returnedValue = map.put(key, value);
        assertEquals(value, map.get(key));
        assertEquals(value, returnedValue);
    }
    
    @Test
    public void putWithNullKeyShouldReturnNull() {
        Double key = null;
        Short value = 2;
        
        Short returnedValue = map.put(key, value);
        assertNull(map.get(key));
        assertEquals(value, returnedValue);
    }
    
    @Test
    public void putWithNullValueShouldReturnNull() {
        Double key = 1.0;
        Short value = null;
        
        Short returnedValue = map.put(key, value);
        assertNull(map.get(key));
        assertEquals(value, returnedValue);
    }
    
    @Test
    public void putWithNullKeyAndValueShouldReturnNull() {
        Double key = null;
        Short value = null;
        
        Short returnedValue = map.put(key, value);
        assertNull(map.get(key));
        assertEquals(value, returnedValue);
    }
    
    @Test
    public void removeShouldRemoveKeyAndValue() {
        Double key = 1.0;
        Short value = 2;
        
        map.put(key, value);
        Short returnedValue = map.remove(key);
        assertNull(map.get(key));
        assertEquals(value, returnedValue);
    }
    
    @Test
    public void removeWithNullKeyShouldReturnNull() {
        Double key = null;
        
        Short returnedValue = map.remove(key);
        assertNull(returnedValue);
    }
    
    @Test
    public void getWithNonDoubleKeyShouldReturnNull() {
        Object key = "test";
        
        assertNull(map.get(key));
    }
    
    @Test
    public void clearShouldRemoveAllEntries() {
        map.put(1.0, (short) 2);
        map.put(2.0, (short) 3);
        
        map.clear();
        
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void entrySetShouldReturnCorrectValues() {
        map.put(1.0, (short) 2);
        map.put(2.0, (short) 3);
        
        assertEquals(2, map.entrySet().size());
        
        for (Entry<Double, Short> entry : map.entrySet()) {
            Double key = entry.getKey();
            Short value = entry.getValue();
            
            assertTrue(map.containsKey(key));
            assertEquals(value, map.get(key));
        }
    }
    
    @Test
    public void containsValueShouldReturnTrueIfValueExists() {
        map.put(1.0, (short) 2);
        
        assertTrue(map.containsValue((short) 2));
    }
    
    @Test
    public void containsValueShouldReturnFalseIfValueDoesNotExist() {
        assertFalse(map.containsValue((short) 2));
    }
    
    @Test
    public void containsKeyShouldReturnTrueIfKeyExists() {
        map.put(1.0, (short) 2);
        
        assertTrue(map.containsKey(1.0));
    }
    
    @Test
    public void containsKeyShouldReturnFalseIfKeyDoesNotExist() {
        assertFalse(map.containsKey(1.0));
    }
    
    @Test
    public void sizeShouldReturnCorrectSize() {
        map.put(1.0, (short) 2);
        map.put(2.0, (short) 3);
        
        assertEquals(2, map.size());
    }
    
    @Test
    public void isEmptyShouldReturnTrueIfMapIsEmpty() {
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void isEmptyShouldReturnFalseIfMapIsNotEmpty() {
        map.put(1.0, (short) 2);
        
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void putAllShouldPutAllEntriesFromGivenMap() {
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(1.0, (short) 2);
        otherMap.put(2.0, (short) 3);
        
        map.putAll(otherMap);
        
        assertEquals(otherMap.size(), map.size());
        
        for (Entry<Double, Short> entry : otherMap.entrySet()) {
            Double key = entry.getKey();
            Short value = entry.getValue();
            
            assertTrue(map.containsKey(key));
            assertEquals(value, map.get(key));
        }
    }
    
    @Test
    public void wrapKeyShouldWrapDoubleToDouble() {
        double d = 1.0;
        Double wrapped = map.wrapKey(d);
        
        assertEquals(d, wrapped, 0);
    }
    
    @Test
    public void unwrapKeyShouldUnwrapDoubleFromDouble() {
        double d = 1.0;
        Double wrapped = map.wrapKey(d);
        double unwrapped = map.unwrapKey(wrapped);
        
        assertEquals(d, unwrapped, 0);
    }
    
    @Test
    public void wrapValueShouldWrapShortToShort() {
        short s = 2;
        Short wrapped = map.wrapValue(s);
        
        assertEquals(s, wrapped.shortValue());
    }
    
    @Test
    public void unwrapValueShouldUnwrapShortFromShort() {
        short s = 2;
        Short wrapped = map.wrapValue(s);
        short unwrapped = map.unwrapValue(wrapped);
        
        assertEquals(s, unwrapped);
    }
}
