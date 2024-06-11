package gnu.trove.decorator;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        
        mapDecorator.put(key, value);
        mapDecorator.clear();
        
        assertNull(mapDecorator.get(key));
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
    }
    
    // Add more test cases for the remaining methods and edge cases
    
}