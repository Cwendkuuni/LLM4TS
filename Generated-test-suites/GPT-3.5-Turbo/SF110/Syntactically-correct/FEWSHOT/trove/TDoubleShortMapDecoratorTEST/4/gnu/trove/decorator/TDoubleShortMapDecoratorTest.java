import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);
        
        assertEquals((Short) 1, decorator.get(1.0));
        assertEquals((Short) 2, decorator.get(2.0));
        assertEquals((Short) 3, decorator.get(3.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        decorator.clear();
        
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        decorator.remove(1.0);
        
        assertNull(decorator.get(1.0));
        assertEquals((Short) 2, decorator.get(2.0));
    }
    
    @Test
    public void testContainsKeyAndValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsValue((short) 1));
        assertFalse(decorator.containsKey(3.0));
        assertFalse(decorator.containsValue((short) 3));
    }
    
    @Test
    public void testSizeAndIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
        
        decorator.put(1.0, (short) 1);
        
        assertEquals(1, decorator.size());
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        
        decorator.putAll(map);
        
        assertEquals((Short)1, decorator.get(1.0));
        assertEquals((Short)2, decorator.get(2.0));
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();
        
        for (Map.Entry<Double, Short> entry : entrySet) {
            Double key = entry.getKey();
            Short value = entry.getValue();
            
            if (key == 1.0) {
                assertEquals((Short)1, value);
            } else if (key == 2.0) {
                assertEquals((Short)2, value);
            }
        }
    }
    
}