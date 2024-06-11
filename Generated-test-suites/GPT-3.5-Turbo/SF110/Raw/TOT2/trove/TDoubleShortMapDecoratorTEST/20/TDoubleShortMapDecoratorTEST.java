import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        
        mapDecorator.put(key, value);
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 2.0;
        Short value = 20;
        
        mapDecorator.put(key, value);
        mapDecorator.clear();
        Short retrievedValue = mapDecorator.get(key);
        
        assertNull(retrievedValue);
    }
    
    @Test
    public void testContains() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 3.0;
        Short value = 30;
        
        mapDecorator.put(key, value);
        
        assertTrue(mapDecorator.containsKey(key));
        assertTrue(mapDecorator.containsValue(value));
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(4.0, (short)40);
        
        mapDecorator.putAll(map);
        Short retrievedValue = mapDecorator.get(4.0);
        
        assertEquals((short)40, (short)retrievedValue);
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(null, (short)50);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.get(null);
    }
    
}