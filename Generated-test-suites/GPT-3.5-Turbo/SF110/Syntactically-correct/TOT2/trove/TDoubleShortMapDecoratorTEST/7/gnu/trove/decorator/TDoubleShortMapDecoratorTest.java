package gnu.trove.decorator;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        
        assertNull(map.put(key, value));
        assertEquals(value, map.get(key));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        
        map.put(key, value);
        assertEquals(value, map.remove(key));
        assertNull(map.get(key));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        
        map.put(key, value);
        map.clear();
        assertNull(map.get(key));
        assertEquals(0, map.size());
    }

    // Additional test cases for other methods can be added here

}