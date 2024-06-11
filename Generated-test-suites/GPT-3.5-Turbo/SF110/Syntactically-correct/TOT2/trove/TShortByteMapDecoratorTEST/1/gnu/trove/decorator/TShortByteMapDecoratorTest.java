package gnu.trove.decorator;
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertNull(mapDecorator.put((short) 1, (byte) 10));
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.put((short) 1, (byte) 20));
    }
    
    @Test
    public void testGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.get((short) 1));
        assertNull(mapDecorator.get((short) 2));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.remove((short) 1));
        assertNull(mapDecorator.remove((short) 2));
    }

    // Add more tests for edge cases and exception handling scenarios

}