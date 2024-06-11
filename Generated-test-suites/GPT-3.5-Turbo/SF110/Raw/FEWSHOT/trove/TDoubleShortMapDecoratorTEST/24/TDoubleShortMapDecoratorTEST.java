package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.put(null, null));
        assertNull(map.put(1.0, null));
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertEquals((short) 1, map.put(1.0, (short) 4));
        assertEquals((short) 2, map.put(2.0, (short) 5));
        assertEquals((short) 3, map.put(3.0, (short) 6));
        assertEquals(3, map.size());
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.get(null));
        assertNull(map.get(1.0));
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertEquals((short) 1, map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
        assertEquals((short) 3, map.get(3.0));
        assertNull(map.get(4.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        map.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.remove(null));
        assertNull(map.remove(1.0));
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertEquals((short) 1, map.remove(1.0));
        assertEquals((short) 2, map.remove(2.0));
        assertEquals((short) 3, map.remove(3.0));
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertFalse(map.containsValue(null));
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertTrue(map.existsValue((short)1));
        assertTrue(map.existsValue((short)2));
        assertTrue(map.existsValue((short)3));
        assertFalse(map.existsValue((short)4));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertFalse(map.containsKey(null));
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsKey(2.0));
        assertTrue(map.containsKey(3.0));
        assertFalse(map.containsKey(4.0));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertEquals(3, map.size());
    }
    
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertTrue(map.isEmpty());
        map.put(1.0, (short) 1);
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Map<Double, Short> temp = new HashMap<>(); 
        temp.put(1.0, (short) 1);
        temp.put(2.0, (short) 2);
        map.putAll(temp);
        assertEquals((short) 1, map.get(1.0));
        assertEquals((short) 2, map.get(2.0));
    }
}