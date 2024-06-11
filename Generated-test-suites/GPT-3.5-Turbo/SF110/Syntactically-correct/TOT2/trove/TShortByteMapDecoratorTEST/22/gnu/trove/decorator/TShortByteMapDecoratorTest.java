package gnu.trove.decorator;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), decorator.get((short) 1));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), decorator.remove((short) 1));
        assertNull(decorator.get((short) 1));
    }

    // Additional test cases for edge cases and error scenarios can be added here

}