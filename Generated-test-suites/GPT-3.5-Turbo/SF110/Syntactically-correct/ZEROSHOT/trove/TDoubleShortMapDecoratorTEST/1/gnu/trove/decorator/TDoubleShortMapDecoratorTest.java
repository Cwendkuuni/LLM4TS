import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }
    
    @Test
    public void testPutAndGet() {
        mapDecorator.put(1.0, (short) 10);
        mapDecorator.put(2.0, (short) 20);
        
        assertEquals(10, (short) mapDecorator.get(1.0));
        assertEquals(20, (short) mapDecorator.get(2.0));
    }
    
    @Test
    public void testClear() {
        mapDecorator.put(1.0, (short) 10);
        mapDecorator.put(2.0, (short) 20);
        
        mapDecorator.clear();
        
        assertNull(mapDecorator.get(1.0));
        assertNull(mapDecorator.get(2.0));
    }
    
    @Test
    public void testRemove() {
        mapDecorator.put(1.0, (short) 10);
        mapDecorator.put(2.0, (short) 20);
        
        assertEquals(10, (short) mapDecorator.remove(1.0));
        assertNull(mapDecorator.get(1.0));
        assertEquals(20, (short) mapDecorator.remove(2.0));
        assertNull(mapDecorator.get(2.0));
    }
    
    @Test
    public void testEntrySet() {
        mapDecorator.put(1.0, (short) 10);
        mapDecorator.put(2.0, (short) 20);
        
        assertEquals(2, mapDecorator.entrySet().size());
    }
    
    @Test
    public void testContainsValue() {
        mapDecorator.put(1.0, (short) 10);
        mapDecorator.put(2.0, (short) 20);
        
        assertTrue(mapDecorator.containsValue((short) 10));
        assertTrue(mapDecorator.containsValue((short) 20));
        assertFalse(mapDecorator.containsValue((short) 30));
    }
    
    @Test
    public void testContainsKey() {
        mapDecorator.put(1.0, (short) 10);
        mapDecorator.put(2.0, (short) 20);
        
        assertTrue(mapDecorator.containsKey(1.0));
        assertTrue(mapDecorator.containsKey(2.0));
        assertFalse(mapDecorator.containsKey(3.0));
    }
    
    @Test
    public void testSize() {
        mapDecorator.put(1.0, (short) 10);
        mapDecorator.put(2.0, (short) 20);
        
        assertEquals(2, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        
        mapDecorator.put(1.0, (short) 10);
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMap anotherMap = new TDoubleShortMap();
        anotherMap.put(3.0, (short) 30);
        anotherMap.put(4.0, (short) 40);
        
        mapDecorator.putAll(anotherMap);
        
        assertEquals(30, (short) mapDecorator.get(3.0));
        assertEquals(40, (short) mapDecorator.get(4.0));
    }
    
    @Test
    public void testWrapAndUnwrap() {
        double key = 5.0;
        short value = 50;
        
        Double wrappedKey = mapDecorator.wrapKey(key);
        short wrappedValue = mapDecorator.unwrapValue(value);
        
        assertEquals(key, (double) wrappedKey, 0.0001);
        assertEquals(value, wrappedValue);
    }
    
    @Test
    public void testExternalizable() {
        // TODO: Implement test for readExternal() and writeExternal()
    }
}