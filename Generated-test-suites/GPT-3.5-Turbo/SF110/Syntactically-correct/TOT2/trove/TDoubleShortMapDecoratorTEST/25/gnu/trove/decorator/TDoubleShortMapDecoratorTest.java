package gnu.trove.decorator;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        
        assertNull(mapDecorator.put(key, value));
        assertEquals(value, mapDecorator.get(key));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 2);
        mapDecorator.clear();
        
        assertEquals(0, mapDecorator.size());
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        assertNull(mapDecorator.get(key));
    }
    
    // Add more test cases here to cover edge cases and exceptions
    
}