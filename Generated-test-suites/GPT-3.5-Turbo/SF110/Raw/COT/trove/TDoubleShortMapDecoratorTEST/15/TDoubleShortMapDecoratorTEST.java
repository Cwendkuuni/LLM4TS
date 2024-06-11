import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.5, (short) 2);
        
        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.5).shortValue());
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.5, (short) 2);
        
        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.5).shortValue());
        assertNull(decorator.get(3.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.5, (short) 2);
        
        assertFalse(decorator.isEmpty());
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.5, (short) 2);
        
        assertNotNull(decorator.remove(1.0));
        assertNull(decorator.get(1.0));
        assertNull(decorator.remove(3.0));
    }
    
    // Add more test cases for the remaining public methods
}