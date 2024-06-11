package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import gnu.trove.map.TDoubleShortMap;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPut() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        assertNull(map.put(1.0, (short) 10));
        assertEquals(1, map.size());
        
        assertNull(map.put(2.0, (short) 20));
        assertEquals(2, map.size());
        
        assertEquals((short) 10, map.put(1.0, (short) 30));
        assertEquals(2, map.size());
    }
    
    @Test
    public void testGet() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        assertNull(map.get(1.0));
        
        map.put(1.0, (short) 10);
        assertEquals((short) 10, map.get(1.0));
        
        assertNull(map.get(2.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        
        assertFalse(map.isEmpty());
        
        map.clear();
        
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        
        assertEquals((short) 10, map.remove(1.0));
        assertNull(map.get(1.0));
        assertEquals(1, map.size());
        
        assertNull(map.remove(3.0));
        assertEquals(1, map.size());
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        
        assertEquals(2, map.entrySet().size());
        
        assertTrue(map.entrySet().contains(new Entry<Double, Short>() {
            public boolean equals(Object obj) {
                if (obj instanceof Entry) {
                    Entry<?, ?> entry = (Entry<?, ?>) obj;
                    return entry.getKey().equals(1.0) && entry.getValue().equals((short) 10);
                }
                return false;
            }
            
            public Double getKey() {
                return 1.0;
            }
            
            public Short getValue() {
                return (short) 10;
            }
            
            public Short setValue(Short value) {
                return (short) 20;
            }
        }));
        
        assertFalse(map.entrySet().contains(new Entry<Double, Short>() {
            public boolean equals(Object obj) {
                if (obj instanceof Entry) {
                    Entry<?, ?> entry = (Entry<?, ?>) obj;
                    return entry.getKey().equals(3.0) && entry.getValue().equals((short) 30);
                }
                return false;
            }
            
            public Double getKey() {
                return 3.0;
            }
            
            public Short getValue() {
                return (short) 30;
            }
            
            public Short setValue(Short value) {
                return (short) 40;
            }
        }));
        
        map.entrySet().iterator().next().remove();
        assertEquals(1, map.entrySet().size());
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        
        assertTrue(map.containsValue((short) 10));
        assertFalse(map.containsValue((short) 30));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        
        assertTrue(map.containsKey(1.0));
        assertFalse(map.containsKey(3.0));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        
        map.put(1.0, (short) 10);
        assertEquals(1, map.size());
        
        map.remove(1.0);
        assertEquals(0, map.size());
    }
    
    @Test
    public void testIsEmpty() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        assertTrue(map.isEmpty());
        
        map.put(1.0, (short) 10);
        assertFalse(map.isEmpty());
        
        map.remove(1.0);
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMap map = new TDoubleShortMapDecorator();
        
        Map<Double, Short> values = new HashMap<>();
        values.put(1.0, (short) 10);
        values.put(2.0, (short) 20);
        
        map.putAll(values);
        
        assertEquals((short) 10, map.get(1.0));
        assertEquals((short) 20, map.get(2.0));
    }
}