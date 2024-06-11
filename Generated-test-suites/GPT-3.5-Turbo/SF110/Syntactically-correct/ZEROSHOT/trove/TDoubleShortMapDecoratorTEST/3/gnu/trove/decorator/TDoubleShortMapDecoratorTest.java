import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        decorator.put(3.0, (short)3);
        
        Short value1 = decorator.get(1.0);
        Short value2 = decorator.get(2.0);
        Short value3 = decorator.get(3.0);
        
        assertEquals((short)1, value1.shortValue());
        assertEquals((short)2, value2.shortValue());
        assertEquals((short)3, value3.shortValue());
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        decorator.put(3.0, (short)3);
        
        assertFalse(decorator.isEmpty());
        
        decorator.clear();
        
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        decorator.put(3.0, (short)3);
        
        Short value = decorator.remove(2.0);
        assertEquals((short)2, value.shortValue());
        
        assertNull(decorator.get(2.0));
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        decorator.put(3.0, (short)3);
        
        assertTrue(decorator.containsValue((short)2));
        assertFalse(decorator.containsValue((short)4));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        decorator.put(3.0, (short)3);
        
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(4.0));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        decorator.put(3.0, (short)3);
        
        assertEquals(3, decorator.size());
        
        decorator.remove(2.0);
        
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator1 = new TDoubleShortMapDecorator();
        decorator1.put(1.0, (short)1);
        decorator1.put(2.0, (short)2);
        
        TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
        decorator2.putAll(decorator1);
        
        assertEquals((short)1, decorator2.get(1.0).shortValue());
        assertEquals((short)2, decorator2.get(2.0).shortValue());
    }

}