import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        Short retrievedValue = decorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        decorator.clear();
        
        assertNull(decorator.get(key));
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        Double key = 1.0;
        Short value = 2;
        
        decorator.put(key, value);
        Short removedValue = decorator.remove(key);
        
        assertEquals(value, removedValue);
        assertNull(decorator.get(key));
    }
    
    // Add more test cases for the remaining methods
    
}