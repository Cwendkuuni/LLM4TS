import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import gnu.trove.decorator.TDoubleShortMapDecorator;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setup() {
        mapDecorator = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        mapDecorator.put(3.0, (short) 3);
        
        assertEquals((short) 1, (short) mapDecorator.get(1.0));
        assertEquals((short) 2, (short) mapDecorator.get(2.0));
        assertEquals((short) 3, (short) mapDecorator.get(3.0));
    }
    
    @Test
    public void testPutWithNullValue() {
        mapDecorator.put(1.0, null);
        
        assertNull(mapDecorator.get(1.0));
    }
    
    @Test
    public void testGetWithNullKey() {
        mapDecorator.put(1.0, (short) 1);
        
        assertNull(mapDecorator.get(null));
    }
    
    @Test
    public void testClear() {
        mapDecorator.put(1.0, (short) 1);
        
        assertFalse(mapDecorator.isEmpty());
        
        mapDecorator.clear();
        
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        mapDecorator.put(1.0, (short) 1);
        
        assertEquals((short) 1, (short) mapDecorator.remove(1.0));
        assertNull(mapDecorator.get(1.0));
    }
    
    @Test
    public void testEntrySet() {
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        mapDecorator.put(3.0, (short) 3);
        
        assertEquals(3, mapDecorator.entrySet().size());
        
        for (Map.Entry<Double, Short> entry : mapDecorator.entrySet()) {
            assertTrue(mapDecorator.containsKey(entry.getKey()));
            assertTrue(mapDecorator.containsValue(entry.getValue()));
        }
    }
    
    @Test
    public void testContainsValue() {
        mapDecorator.put(1.0, (short) 1);
        
        assertTrue(mapDecorator.containsValue((short) 1));
        assertFalse(mapDecorator.containsValue((short) 2));
    }
    
    @Test
    public void testContainsKey() {
        mapDecorator.put(1.0, (short) 1);
        
        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(2.0));
    }
    
    @Test
    public void testSize() {
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        
        assertEquals(2, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        
        mapDecorator.put(1.0, (short) 1);
        
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        mapDecorator.putAll(map);
        
        assertEquals((short) 1, (short) mapDecorator.get(1.0));
        assertEquals((short) 2, (short) mapDecorator.get(2.0));
    }
    
    @Test
    public void testWrapKey() {
        double key = 1.0;
        
        assertEquals(key, (double) mapDecorator.wrapKey(key), 0.0001);
    }
    
    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        
        assertEquals(key, (Double) mapDecorator.unwrapKey(key));
    }
    
    @Test
    public void testWrapValue() {
        short value = 1;
        
        assertEquals(value, (short) mapDecorator.wrapValue(value));
    }
    
    @Test
    public void testUnwrapValue() {
        Short value = 1;
        
        assertEquals(value, (Short) mapDecorator.unwrapValue(value));
    }
    
    @Test
    public void testReadExternal() {
        // TODO: Write the test for readExternal
    }
    
    @Test
    public void testWriteExternal() {
        // TODO: Write the test for writeExternal
    }
}