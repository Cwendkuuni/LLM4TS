
package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import gnu.trove.map.TDoubleShortMap;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPutAndGet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        assertNull(decorator.put(null, null));
        assertNull(decorator.get(null));
        
        Double key1 = 1.0;
        Short value1 = 2;
        assertEquals(value1, decorator.put(key1, value1));
        assertEquals(value1, decorator.get(key1));
        
        Double key2 = 3.0;
        Short value2 = 4;
        assertEquals(value2, decorator.put(key2, value2));
        assertEquals(value2, decorator.get(key2));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short)2);
        map.put(3.0, (short)4);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        decorator.clear();
        
        assertEquals(0, decorator.size());
        assertNull(decorator.get(1.0));
        assertNull(decorator.get(3.0));
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short)2);
        map.put(3.0, (short)4);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        assertNull(decorator.remove(null));
        assertNull(decorator.remove(5.0));
        
        Short value1 = decorator.remove(1.0);
        assertEquals((short)2, (short)value1);
        assertNull(decorator.get(1.0));
        
        Short value2 = decorator.remove(3.0);
        assertEquals((short)4, (short)value2);
        assertNull(decorator.get(3.0));
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short)2);
        map.put(3.0, (short)4);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        assertEquals(true, decorator.containsValue((short)2));
        assertEquals(true, decorator.containsValue((short)4));
        assertEquals(false, decorator.containsValue((short)6));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short)2);
        map.put(3.0, (short)4);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        assertEquals(true, decorator.containsKey(1.0));
        assertEquals(true, decorator.containsKey(3.0));
        assertEquals(false, decorator.containsKey(5.0));
        assertEquals(false, decorator.containsKey(null));
        assertEquals(false, decorator.containsKey("test"));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short)2);
        map.put(3.0, (short)4);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        assertEquals(2, decorator.size());
        
        decorator.clear();
        
        assertEquals(0, decorator.size());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMap map1 = new TDoubleShortMap();
        map1.put(1.0, (short)2);
        map1.put(3.0, (short)4);
        
        TDoubleShortMapDecorator decorator1 = new TDoubleShortMapDecorator(map1);
        
        TDoubleShortMap map2 = new TDoubleShortMap();
        map2.put(5.0, (short)6);
        map2.put(7.0, (short)8);
        
        decorator1.putAll(map2);
        
        assertEquals(4, decorator1.size());
        assertEquals((short)6, (short)decorator1.get(5.0));
        assertEquals((short)8, (short)decorator1.get(7.0));
    }
}
