
package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setup() {
        mapDecorator = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        mapDecorator.put(1.0, (short)1);
        mapDecorator.put(2.0, (short)2);
        mapDecorator.put(3.0, (short)3);
        
        assertEquals((short)1, mapDecorator.get(1.0).shortValue());
        assertEquals((short)2, mapDecorator.get(2.0).shortValue());
        assertEquals((short)3, mapDecorator.get(3.0).shortValue());
    }
    
    @Test
    public void testPutAndGetWithNull() {
        mapDecorator.put(null, (short)1);
        mapDecorator.put(2.0, null);
        
        assertEquals((short)1, mapDecorator.get(null).shortValue());
        assertNull(mapDecorator.get(2.0));
    }
    
    @Test
    public void testRemove() {
        mapDecorator.put(1.0, (short)1);
        assertTrue(mapDecorator.containsKey(1.0));
        
        mapDecorator.remove(1.0);
        assertFalse(mapDecorator.containsKey(1.0));
    }
    
    @Test
    public void testClear() {
        mapDecorator.put(1.0, (short)1);
        mapDecorator.put(2.0, (short)2);
        mapDecorator.put(3.0, (short)3);
        
        assertFalse(mapDecorator.isEmpty());
        
        mapDecorator.clear();
        
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testSize() {
        mapDecorator.put(1.0, (short)1);
        mapDecorator.put(2.0, (short)2);
        mapDecorator.put(3.0, (short)3);
        
        assertEquals(3, mapDecorator.size());
    }
    
    @Test
    public void testContainsKey() {
        mapDecorator.put(1.0, (short)1);
        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(2.0));
    }
    
    @Test
    public void testContainsValue() {
        mapDecorator.put(1.0, (short)1);
        assertTrue(mapDecorator.containsValue((short)1));
        assertFalse(mapDecorator.containsValue((short)2));
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator anotherMapDecorator = new TDoubleShortMapDecorator();
        
        anotherMapDecorator.put(4.0, (short)4);
        anotherMapDecorator.put(5.0, (short)5);
        
        mapDecorator.putAll(anotherMapDecorator);
        
        assertTrue(mapDecorator.containsKey(4.0));
        assertTrue(mapDecorator.containsKey(5.0));
    }
}
