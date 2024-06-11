import gnu.trove.map.TDoubleShortMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        decorator.put(1.0, (short) 10);
        assertEquals(10, decorator.get(1.0).shortValue());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        decorator.put(2.0, (short) 20);
        assertEquals(20, decorator.remove(2.0).shortValue());
    }
    
    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        decorator.put(3.0, (short) 30);
        decorator.clear();
        assertNull(decorator.get(3.0));
    }
    
    // Add more individual test cases for each method
    
}