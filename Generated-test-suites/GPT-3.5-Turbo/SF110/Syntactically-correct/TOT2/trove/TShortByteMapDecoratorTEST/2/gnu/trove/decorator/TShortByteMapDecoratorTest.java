package gnu.trove.decorator;
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte value = decorator.put((short)1, (byte)10);
        assertNotNull(value);
    }
    
    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        Byte value = decorator.get((short)1);
        assertEquals(Byte.valueOf((byte)10), value);
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        Byte value = decorator.remove((short)1);
        assertEquals(Byte.valueOf((byte)10), value);
    }
    
}