package gnu.trove.decorator;
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    TShortByteMapDecorator decorator = new TShortByteMapDecorator();

    @Test
    public void testPutAndGet() {
        Short key = 1;
        Byte value = 10;

        decorator.put(key, value);

        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testClear() {
        Short key = 1;
        Byte value = 10;

        decorator.put(key, value);
        decorator.clear();

        assertNull(decorator.get(key));
    }

    // Other test cases for the rest of the methods
    
}