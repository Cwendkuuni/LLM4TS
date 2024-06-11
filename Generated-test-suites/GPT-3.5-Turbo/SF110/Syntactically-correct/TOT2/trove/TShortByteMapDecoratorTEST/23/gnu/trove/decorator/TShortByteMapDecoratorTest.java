package gnu.trove.decorator;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), map.get((short) 1));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), map.remove((short) 1));
        assertNull(map.get((short) 2));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 10);
        assertTrue(map.containsValue((byte) 10));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 10);
        assertTrue(map.containsKey((short) 1));
    }

    @Test
    public void testSizeAndEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 10);
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
    }
}
