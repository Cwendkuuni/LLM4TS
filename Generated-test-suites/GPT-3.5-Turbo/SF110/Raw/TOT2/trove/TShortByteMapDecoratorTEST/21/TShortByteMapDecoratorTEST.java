import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

public class TShortByteMapDecoratorTest {
    
    private TShortByteMapDecorator decorator;
    
    @Before
    public void setUp() {
        decorator = new TShortByteMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        Short key = 5;
        Byte value = 10;
        
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }
    
    @Test
    public void testPutAndGetNullKey() {
        Byte value = 10;
        
        decorator.put(null, value);
        assertNull(decorator.get(null));
    }
    
    @Test
    public void testPutAndGetNullValue() {
        Short key = 5;
        
        decorator.put(key, null);
        assertNull(decorator.get(key));
    }
    
    @Test
    public void testRemove() {
        Short key = 5;
        Byte value = 10;
        
        decorator.put(key, value);
        assertEquals(value, decorator.remove(key));
    }
    
    @Test
    public void testRemoveNonExistent() {
        assertNull(decorator.remove(10));
    }
    
    // Add more test cases for other methods
    
    @Test
    public void testPutAll() {
        HashMap<Short, Byte> map = new HashMap<>();
        map.put((short)1, (byte)2);
        map.put((short)3, (byte)4);
        
        decorator.putAll(map);
        
        assertTrue(decorator.containsKey((short)1));
        assertTrue(decorator.containsKey((short)3));
    }
}