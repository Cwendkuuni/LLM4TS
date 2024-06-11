import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        mapDecorator = new TShortByteMapDecorator();
    }
    
    @After
    public void tearDown() {
        mapDecorator.clear();
    }
    
    @Test
    public void testPutAndGet() {
        mapDecorator.put((short)1, (byte)10);
        mapDecorator.put((short)2, (byte)20);
        
        assertEquals((byte)10, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)20, mapDecorator.get((short)2).byteValue());
    }
    
    @Test
    public void testPutAndGetWithNullKey() {
        mapDecorator.put(null, (byte)10);
        
        assertEquals((byte)10, mapDecorator.get(null).byteValue());
    }
    
    @Test
    public void testPutAndGetWithNullValue() {
        mapDecorator.put((short)1, null);
        
        assertNull(mapDecorator.get((short)1));
    }
    
    @Test
    public void testRemove() {
        mapDecorator.put((short)1, (byte)10);
        
        Byte removed = mapDecorator.remove((short)1);
        
        assertEquals((byte)10, removed.byteValue());
        assertNull(mapDecorator.get((short)1));
    }
    
    @Test
    public void testRemoveWithNullKey() {
        mapDecorator.put(null, (byte)10);
        
        Byte removed = mapDecorator.remove(null);
        
        assertEquals((byte)10, removed.byteValue());
        assertNull(mapDecorator.get(null));
    }
    
    @Test
    public void testContainsKey() {
        mapDecorator.put((short)1, (byte)10);
        
        assertTrue(mapDecorator.containsKey((short)1));
        assertFalse(mapDecorator.containsKey((short)2));
    }
    
    @Test
    public void testContainsKeyWithNullKey() {
        mapDecorator.put(null, (byte)10);
        
        assertTrue(mapDecorator.containsKey(null));
    }
    
    @Test
    public void testContainsValue() {
        mapDecorator.put((short)1, (byte)10);
        
        assertTrue(mapDecorator.containsValue((byte)10));
        assertFalse(mapDecorator.containsValue((byte)20));
    }
    
    @Test
    public void testClear() {
        mapDecorator.put((short)1, (byte)10);
        
        mapDecorator.clear();
        
        assertEquals(0, mapDecorator.size());
        assertNull(mapDecorator.get((short)1));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        
        mapDecorator.put((short)1, (byte)10);
        
        assertEquals(1, mapDecorator.size());
    }
    
}