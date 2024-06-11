import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Basic test
        assertNull(map.put(1.0, (short) 1));
        assertEquals(Short.valueOf((short) 1), map.get(1.0));
        
        // Test with null key
        assertNull(map.put(null, (short) 2));
        assertEquals(Short.valueOf((short) 2), map.get(null));
        
        // Test with null value
        assertEquals(Short.valueOf((short) 2), map.put(1.0, null));
        assertNull(map.get(1.0));
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Basic test
        assertNull(map.put(1.0, (short) 1));
        assertEquals(Short.valueOf((short) 1), map.get(1.0));
        
        // Test with null key
        assertNull(map.put(null, (short) 2));
        assertEquals(Short.valueOf((short) 2), map.get(null));
        
        // Test with non-existent key
        assertNull(map.get(2.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Add some elements
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        // Clear the map
        map.clear();
        
        // Check if the map is empty
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        // Add some elements
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        // Remove an existing element
        assertEquals(Short.valueOf((short) 1), map.remove(1.0));
        assertNull(map.get(1.0));
        
        // Try to remove a non-existent element
        assertNull(map.remove(3.0));
    }
    
    // Add additional test methods for the other public methods in TDoubleShortMapDecorator class as needed
    
}