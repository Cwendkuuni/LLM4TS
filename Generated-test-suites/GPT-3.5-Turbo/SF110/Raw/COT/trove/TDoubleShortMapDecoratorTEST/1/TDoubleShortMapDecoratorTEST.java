import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.put(1.0, (short) 1));
        assertEquals(1, map.size());
        assertEquals((short) 1, map.get(1.0).shortValue());
        
        assertNull(map.put(null, (short) 2));
        assertEquals(1, map.size());
        assertEquals((short) 2, map.get(null).shortValue());

        Short previous = map.put(1.0, (short) 3);
        assertEquals((short) 1, previous.shortValue());
        assertEquals(1, map.size());
        assertEquals((short) 3, map.get(1.0).shortValue());
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.get(1.0));
        
        map.put(1.0, (short) 1);
        assertEquals((short) 1, map.get(1.0).shortValue());
        
        map.put(null, (short) 2);
        assertEquals((short) 2, map.get(null).shortValue());
        
        assertNull(map.get(2.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.clear();
        assertEquals(0, map.size());
        assertNull(map.get(1.0));
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        assertEquals((short) 1, map.remove(1.0).shortValue());
        assertEquals(0, map.size());
        assertNull(map.get(1.0));
        
        map.put(null, (short) 2);
        assertEquals((short) 2, map.remove(null).shortValue());
        assertEquals(0, map.size());
        assertNull(map.get(null));
    }
    
    // Additional edge cases and exceptions to consider
    // - Test with duplicate keys
    // - Test with large number of entries to check performance
    
    // Test with duplicate keys
    @Test
    public void testDuplicateKeys() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(1.0, (short) 2);
        assertEquals((short) 2, map.get(1.0).shortValue());
        assertEquals(1, map.size());
    }
    
    // Test with large number of entries
    @Test
    public void testPerformance() { 
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        int count = 1000000;
        for (int i = 0; i < count; i++) {
            map.put((double) i, (short) i);
        }
        assertEquals(count, map.size());
        for (int i = 0; i < count; i++) {
            assertEquals((short) i, map.get((double) i).shortValue());
        }
    }

}