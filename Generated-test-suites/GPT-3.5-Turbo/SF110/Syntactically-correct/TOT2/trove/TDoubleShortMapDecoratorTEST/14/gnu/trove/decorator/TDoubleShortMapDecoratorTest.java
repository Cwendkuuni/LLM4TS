package gnu.trove.decorator;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short) 10);
        assertEquals(10, map.get(1.0).shortValue());
        
        map.put(2.0, null);
        assertNull(map.get(2.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short) 10);
        map.clear();
        assertEquals(0, map.size());
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        
        Set<Entry<Double, Short>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());
        
        for (Entry<Double, Short> entry : entrySet) {
            Double key = entry.getKey();
            Short value = entry.getValue();
            assertTrue(map.containsKey(key));
            assertEquals(value, map.get(key));
        }
    }
    
    // Add more test cases for other methods
    
}