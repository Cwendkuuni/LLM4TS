package gnu.trove.decorator;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        assertNull(mapDecorator.put(key, value));
        
        key = null;
        assertNull(mapDecorator.put(key, value));
        
        value = null;
        assertNull(mapDecorator.put(key, value));
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
        
        key = null;
        assertEquals(null, mapDecorator.get(key));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        
        key = null;
        assertEquals(null, mapDecorator.remove(key));
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        Set<Entry<Double, Short>> entrySet = mapDecorator.entrySet();
        
        assertEquals(1, entrySet.size());
        
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        
        Entry<Double, Short> entry = iterator.next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
        
        iterator.remove();
        assertEquals(0, entrySet.size());
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsValue(value));
        
        assertFalse(mapDecorator.containsValue(null));
        assertFalse(mapDecorator.containsValue(3));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsKey(key));
        
        assertFalse(mapDecorator.containsKey(null));
        assertFalse(mapDecorator.containsKey(2.0));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertEquals(1, mapDecorator.size());
        
        mapDecorator.remove(key);
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertTrue(mapDecorator.isEmpty());
        
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        assertFalse(mapDecorator.isEmpty());
        
        mapDecorator.remove(key);
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        Double key = 1.0;
        Short value = 2;
        map.put(key, value);
        
        mapDecorator.putAll(map);
        assertEquals(value, mapDecorator.get(key));
    }
}