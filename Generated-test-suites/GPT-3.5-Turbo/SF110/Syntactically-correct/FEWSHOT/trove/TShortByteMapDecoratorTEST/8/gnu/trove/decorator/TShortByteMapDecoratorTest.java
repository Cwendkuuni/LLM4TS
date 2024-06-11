import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {
  
    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();
        
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);
        
        assertEquals(Byte.valueOf((byte)10), decorator.get((short)1));
        assertEquals(Byte.valueOf((byte)20), decorator.get((short)2));
        
        assertEquals((byte)10, map.get((short)1));
        assertEquals((byte)20, map.get((short)2));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();
        
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);
        decorator.clear();
        
        assertEquals(null, decorator.get((short)1));
        assertEquals(null, decorator.get((short)2));
        
        assertEquals(map.getNoEntryValue(), map.get((short)1));
        assertEquals(map.getNoEntryValue(), map.get((short)2));
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();
        
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);
        
        assertEquals(Byte.valueOf((byte)10), decorator.remove((short)1));
        assertNull(decorator.remove((short)1));
        
        assertEquals(map.getNoEntryValue(), map.get((short)1));
        assertEquals(Byte.valueOf((byte)20), decorator.remove((short)2));
        
        assertEquals(map.getNoEntryValue(), map.get((short)2));
    }
}