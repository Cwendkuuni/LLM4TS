package gnu.trove.decorator;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    // Test put method with valid parameters
    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = decorator.put(1.0, (short) 5);
        assertEquals((short) 5, value.shortValue());
    }
    
    // Test put method with null key
    @Test
    public void testPutNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = decorator.put(null, (short) 5);
        assertNull(value);
    }
    
    // Test get method with valid key
    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 5);
        Short value = decorator.get(1.0);
        assertEquals((short) 5, value.shortValue());
    }
    
    // Test get method with null key
    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = decorator.get(null);
        assertNull(value);
    }
    
    // Test clear method
    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 5);
        assertEquals(1, decorator.size());
        decorator.clear();
        assertEquals(0, decorator.size());
    }
    
    // Test remove method with valid key
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 5);
        Short value = decorator.remove(1.0);
        assertEquals((short) 5, value.shortValue());
    }
    
    // Test remove method with null key
    @Test
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Short value = decorator.remove(null);
        assertNull(value);
    }
    
    // Add more test cases for the remaining methods
    
}