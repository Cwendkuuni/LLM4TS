package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        assertEquals(1, decorator.getMap().get(1.0));
        assertEquals(2, decorator.getMap().get(2.0));
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        assertEquals((Short)1, decorator.get(1.0));
        assertEquals((Short)2, decorator.get(2.0));
        assertNull(decorator.get(3.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        decorator.clear();
        
        assertEquals(0, decorator.getMap().size());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        assertEquals((Short)1, decorator.remove(1.0));
        assertNull(decorator.remove(3.0));
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        assertEquals(true, decorator.containsValue((short)1));
        assertEquals(false, decorator.containsValue((short)3));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        assertEquals(true, decorator.containsKey(1.0));
        assertEquals(false, decorator.containsKey(3.0));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        assertEquals(true, decorator.isEmpty());
        
        decorator.put(1.0, (short)1);
        
        assertEquals(false, decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        
        TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
        decorator2.putAll(decorator);
        
        assertEquals(2, decorator2.size());
        assertEquals((Short)1, decorator.get(1.0));
        assertEquals((Short)2, decorator.get(2.0));
    }
}