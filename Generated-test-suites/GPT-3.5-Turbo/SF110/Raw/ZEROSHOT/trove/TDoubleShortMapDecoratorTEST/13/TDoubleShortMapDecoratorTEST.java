import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator decorator;
    
    @Before
    public void setUp() {
        decorator = new TDoubleShortMapDecorator();
    }
    
    @After
    public void tearDown() {
        decorator.clear();
    }
    
    @Test
    public void testPutAndGet() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.0).shortValue());
    }
    
    @Test
    public void testContainsKey() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(3.0));
    }
    
    @Test
    public void testContainsValue() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertFalse(decorator.containsValue((short) 3));
    }
    
    @Test
    public void testRemove() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertEquals((short) 1, decorator.remove(1.0).shortValue());
        assertEquals((short) 2, decorator.remove(2.0).shortValue());
        assertNull(decorator.remove(3.0));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, decorator.size());
        
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertEquals(2, decorator.size());
        
        decorator.remove(1.0);
        
        assertEquals(1, decorator.size());
    }
    
    @Test
    public void testClear() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertFalse(decorator.isEmpty());
        
        decorator.clear();
        
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        Map.Entry<Double, Short> entry = decorator.entrySet().iterator().next();
        
        assertEquals((Double) 1.0, entry.getKey());
        assertEquals((Short) (short) 1, entry.getValue());
        
        decorator.remove(1.0);
        
        assertTrue(decorator.entrySet().isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        decorator.putAll(map);
        
        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.0).shortValue());
    }
}