package gnu.trove.decorator;

import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 10.0;
        Short value = 5;
        
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }
    
    // Add more test cases here for other public methods
}
