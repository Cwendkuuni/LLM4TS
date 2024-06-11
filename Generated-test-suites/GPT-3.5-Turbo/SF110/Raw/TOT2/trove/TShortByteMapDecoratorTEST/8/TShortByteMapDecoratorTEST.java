import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator decorator;

    @Test
    public void testPutAndGet() {
        decorator = new TShortByteMapDecorator();
        
        Byte value = decorator.put((short) 1, (byte) 10);
        assertEquals(Byte.valueOf((byte)10), value);
        
        value = decorator.get((short) 1);
        assertEquals(Byte.valueOf((byte)10), value);
    }

    @Test
    public void testClear() {
        decorator = new TShortByteMapDecorator();
        
        decorator.put((short) 1, (byte) 10);
        decorator.clear();
        
        Byte value = decorator.get((short) 1);
        assertNull(value);
    }

    @Test
    public void testRemove() {
        decorator = new TShortByteMapDecorator();
        
        decorator.put((short) 1, (byte) 10);
        Byte value = decorator.remove((short) 1);
        
        assertEquals(Byte.valueOf((byte)10), value);
        
        value = decorator.remove((short) 1);
        assertNull(value);
    }

    @Test
    public void testContainsKey() {
        decorator = new TShortByteMapDecorator();
        
        decorator.put((short) 1, (byte) 10);
        
        assertTrue(decorator.containsKey((short) 1));
        assertFalse(decorator.containsKey((short) 2));
    }

    @Test
    public void testContainsValue() {
        decorator = new TShortByteMapDecorator();
        
        decorator.put((short) 1, (byte) 10);
        
        assertTrue(decorator.containsValue((byte) 10));
        assertFalse(decorator.containsValue((byte) 20));
    }

    @Test
    public void testSizeAndIsEmpty() {
        decorator = new TShortByteMapDecorator();
        
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
        
        decorator.put((short) 1, (byte) 10);
        
        assertEquals(1, decorator.size());
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        decorator = new TShortByteMapDecorator();
        
        Map<Short, Byte> map = new HashMap<>();
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        decorator.putAll(map);
        
        Byte value = decorator.get((short) 1);
        assertEquals(Byte.valueOf((byte)10), value);
        
        value = decorator.get((short) 2);
        assertEquals(Byte.valueOf((byte)20), value);
    }

}