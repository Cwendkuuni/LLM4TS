
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        mapDecorator.put(1.0, (short) 1);
        assertEquals((short) 1, mapDecorator.get(1.0).shortValue());
        
        mapDecorator.put(2.0, (short) 2);
        assertEquals((short) 2, mapDecorator.get(2.0).shortValue());
    }
    
    @Test
    public void testClear() {
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        
        mapDecorator.remove(1.0);
        assertNull(mapDecorator.get(1.0));
        assertEquals((short) 2, mapDecorator.get(2.0).shortValue());
    }
    
    @Test
    public void testEntrySet() {
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        
        Set<Map.Entry<Double, Short>> entrySet = mapDecorator.entrySet();
        
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new Map.Entry<Double, Short>() {
            public Double getKey() {
                return 1.0;
            }
            public Short getValue() {
                return (short) 1;
            }
            public Short setValue(Short value) {
                return (short) 1;
            }
        }));
    }
    
    @Test
    public void testContainsValue() {
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        
        assertTrue(mapDecorator.containsValue((short) 1));
        assertFalse(mapDecorator.containsValue((short) 3));
    }
    
    @Test
    public void testContainsKey() {
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        
        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(3.0));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        
        assertEquals(2, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        
        mapDecorator.put(1.0, (short) 1);
        
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        mapDecorator.putAll(map);
        
        assertEquals((short) 1, mapDecorator.get(1.0).shortValue());
        assertEquals((short) 2, mapDecorator.get(2.0).shortValue());
    }
    
    //Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testPutNullKeyAndValue() {
        assertNull(mapDecorator.put(null, null));
        
        mapDecorator.put(1.0, (short) 1);
        assertNull(mapDecorator.put(null, (short) 2));
        assertNull(mapDecorator.put(2.0, null));
    }
    
    @Test
    public void testGetNullKey() {
        mapDecorator.put(1.0, (short) 1);
        
        assertNull(mapDecorator.get(null));
    }
    
    @Test
    public void testRemoveNullKey() {
        mapDecorator.put(1.0, (short) 1);
        
        assertNull(mapDecorator.remove(null));
        assertEquals((short) 1, mapDecorator.get(1.0).shortValue());
    }
}
