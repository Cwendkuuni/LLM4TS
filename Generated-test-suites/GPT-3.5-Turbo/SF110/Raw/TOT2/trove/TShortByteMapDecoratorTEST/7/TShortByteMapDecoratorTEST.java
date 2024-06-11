
package gnu.trove.decorator;

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testPutNull() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = null;
        Byte value = null;
        mapDecorator.put(key, value);
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
    }

    @Test
    public void testWrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        short key = 1;
        assertEquals((Short) key, mapDecorator.wrapKey(key));
    }

    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        byte value = 2;
        assertEquals((byte) value, mapDecorator.unwrapValue(value));
    }

    // More test cases for other methods can be added here

}
