package gnu.trove.decorator;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Map.Entry;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGetValidKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 10.0;
        Short value = 5;
        
        mapDecorator.put(key, value);
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testPutAndGetNullKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = null;
        Short value = 5;
        
        mapDecorator.put(key, value);
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(10.0, (short)5);
        
        mapDecorator.clear();
        
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 10.0;
        Short value = 5;
        mapDecorator.put(key, value);
        
        Short removedValue = mapDecorator.remove(key);
        
        assertEquals(value, removedValue);
        assertNull(mapDecorator.get(key));
    }
    
    // Additional test cases for other methods and scenarios can be added here
    
}