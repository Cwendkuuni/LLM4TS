package gnu.trove.decorator;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        
        assertNull(map.put(null, null));
        assertNull(map.put(key, null));
        assertNull(map.put(null, value));
        
        assertEquals(value, map.put(key, value));
        assertEquals(value, map.get(key));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 2);
        
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        
        map.put(key, value);
        assertEquals(value, map.remove(key));
        assertNull(map.remove(key));
    }
    
    // More test cases can be added for other methods
    
}