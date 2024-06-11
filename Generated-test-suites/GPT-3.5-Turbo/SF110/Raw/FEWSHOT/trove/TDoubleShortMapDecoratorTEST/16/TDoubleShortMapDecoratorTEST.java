package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        Double key1 = new Double(1.0);
        Short value1 = new Short((short) 1);
        
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.get(key1));
        
        Double key2 = new Double(2.0);
        Short value2 = new Short((short) 2);
        
        mapDecorator.put(key2, value2);
        assertEquals(value2, mapDecorator.get(key2));
    }
    
    @Test
    public void testClear() {
        Double key1 = new Double(1.0);
        Short value1 = new Short((short) 1);
        
        mapDecorator.put(key1, value1);
        assertEquals(1, mapDecorator.size());
        
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testRemove() {
        Double key1 = new Double(1.0);
        Short value1 = new Short((short) 1);
        
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.remove(key1));
        assertNull(mapDecorator.get(key1));
    }
    
    @Test
    public void testContainsKey() {
        Double key1 = new Double(1.0);
        Short value1 = new Short((short) 1);
        
        mapDecorator.put(key1, value1);
        assertEquals(true, mapDecorator.containsKey(key1));
        
        Double key2 = new Double(2.0);
        Short value2 = new Short((short) 2);
        assertEquals(false, mapDecorator.containsKey(key2));
    }
    
    @Test
    public void testContainsValue() {
        Double key1 = new Double(1.0);
        Short value1 = new Short((short) 1);
        
        mapDecorator.put(key1, value1);
        assertEquals(true, mapDecorator.containsValue(value1));
        
        Short value2 = new Short((short) 2);
        assertEquals(false, mapDecorator.containsValue(value2));
    }
    
    @Test
    public void testSize() {
        Double key1 = new Double(1.0);
        Short value1 = new Short((short) 1);
        
        mapDecorator.put(key1, value1);
        assertEquals(1, mapDecorator.size());
        
        Double key2 = new Double(2.0);
        Short value2 = new Short((short) 2);
        
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.size());
    }

}