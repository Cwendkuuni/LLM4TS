import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        
        assertNull(mapDecorator.put(key, value));
        assertEquals(value, mapDecorator.get(key));
        
        Double keyNull = null;
        Short valueNull = null;
        
        assertNull(mapDecorator.put(keyNull, valueNull));
        assertNull(mapDecorator.get(keyNull));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short)10);
        
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator(map);
        mapDecorator.clear();
        
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short)10);
        
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator(map);
        Double key = 1.0;
        
        assertEquals((Short)10, mapDecorator.remove(key));
    }

    // Additional test cases for other public methods should go here

}