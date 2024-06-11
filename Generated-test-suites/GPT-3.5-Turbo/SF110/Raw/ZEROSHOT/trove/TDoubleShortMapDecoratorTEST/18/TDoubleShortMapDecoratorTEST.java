import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        Short value1 = decorator.get(1.0);
        assertEquals((short)1, value1.shortValue());
        
        Short value2 = decorator.get(2.0);
        assertEquals((short)2, value2.shortValue());
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        
        Short removedValue = decorator.remove(1.0);
        assertEquals((short)1, removedValue.shortValue());
        assertNull(decorator.get(1.0));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        
        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(2.0));
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        
        assertTrue(decorator.containsValue((short)1));
        assertFalse(decorator.containsValue((short)2));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        HashMap<Double, Short> map = new HashMap<>();
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        
        decorator.putAll(map);
        
        Short value1 = decorator.get(1.0);
        assertEquals((short)1, value1.shortValue());
        
        Short value2 = decorator.get(2.0);
        assertEquals((short)2, value2.shortValue());
    }
}