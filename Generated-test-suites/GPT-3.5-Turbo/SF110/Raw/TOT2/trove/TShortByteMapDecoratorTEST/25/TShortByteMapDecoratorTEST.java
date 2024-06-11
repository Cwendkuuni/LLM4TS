import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testGetMap() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        assertEquals(map, decorator.getMap());
    }
    
    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 10;
        Byte value = 5;
        Byte returnedValue = decorator.put(key, value);
        
        assertEquals(value, returnedValue);
        assertTrue(map.containsKey((short)10));
        assertTrue(map.containsValue((byte)5));
    }
}