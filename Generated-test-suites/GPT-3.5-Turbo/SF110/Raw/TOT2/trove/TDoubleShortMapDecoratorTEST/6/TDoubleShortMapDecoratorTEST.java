import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        mapDecorator.put(1.0, (short) 10);
        assertEquals((Short) 10, mapDecorator.get(1.0));
    }
    
    @Test
    public void testPutNullKey() {
        mapDecorator.put(null, (short) 20);
        assertEquals((Short) 20, mapDecorator.get(mapDecorator._map.getNoEntryKey()));
    }
    
    @Test
    public void testPutNullValue() {
        mapDecorator.put(2.0, null);
        assertEquals(null, mapDecorator.get(2.0));
    }
    
    @Test
    public void testPutAndGetNoEntryValue() {
        mapDecorator._map.setNoEntryValue((short) -1);
        mapDecorator.put(3.0, (short) -1);
        assertEquals(null, mapDecorator.get(3.0)); // No entry value should be converted to null
    }
    
    @Test
    public void testRemove() {
        mapDecorator.put(4.0, (short) 30);
        assertEquals((Short) 30, mapDecorator.remove(4.0));
    }
    
    @Test
    public void testRemoveNonExistingKey() {
        assertEquals(null, mapDecorator.remove(5.0));
    }
    
    // Add more test cases for the remaining methods
    
}