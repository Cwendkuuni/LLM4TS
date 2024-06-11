package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPutAndGet() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertNull(decorator.get(3.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.clear();
        
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.remove(1.0);
        
        assertNull(decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(2.0));
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
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        TDoubleShortMap map2 = new TDoubleShortHashMap();
        map2.put(2.0, (short) 2);
        
        decorator.putAll(map2);
        
        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
    }
}