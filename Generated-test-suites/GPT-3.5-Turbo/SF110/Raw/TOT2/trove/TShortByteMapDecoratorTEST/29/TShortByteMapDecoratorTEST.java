import gnu.trove.map.hash.TShortByteHashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteHashMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = 1;
        Byte value = 10;

        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testClear() {
        TShortByteHashMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = 1;
        Byte value = 10;

        decorator.put(key, value);
        decorator.clear();
        assertNull(decorator.get(key));
    }

    @Test
    public void testRemove() {
        TShortByteHashMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = 1;
        Byte value = 10;

        decorator.put(key, value);
        assertEquals(value, decorator.remove(key));
    }

    // Additional test cases for edge cases and exceptions can be included here

}