package gnu.trove.decorator;

import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Byte value = mapDecorator.put((short)1, (byte)10);
        assertEquals((byte)10, value.byteValue());
    }
    
    // Other test cases for public methods

}