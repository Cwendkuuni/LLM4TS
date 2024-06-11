package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import gnu.trove.map.TDoubleShortMap;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 10);
        decorator.put(2.0, (short) 20);
        decorator.put(3.0, (short) 30);
        
        assertEquals((Short) 10, decorator.get(1.0));
        assertEquals((Short) 20, decorator.get(2.0));
        assertEquals((Short) 30, decorator.get(3.0));
        assertNull(decorator.get(4.0));
        assertNull(decorator.get(null));
    }

    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        map.put(3.0, (short) 30);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertEquals((Short) 10, decorator.remove(1.0));
        assertNull(decorator.get(1.0));
        assertNull(map.get(1.0));
        assertNull(decorator.remove(4.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        map.put(3.0, (short) 30);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.clear();

        assertNull(decorator.get(1.0));
        assertNull(decorator.get(2.0));
        assertNull(decorator.get(3.0));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 20);
        map.put(3.0, (short) 30);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertEquals(true, decorator.containsKey(1.0));
        assertEquals(true, decorator.containsKey(2.0));
        assertEquals(true, decorator.containsKey(3.0));
        assertEquals(false, decorator.containsKey(4.0));
        assertEquals(true, decorator.containsKey(null));
        assertEquals(false, decorator.containsKey("test"));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 10);
        map.put(3.0, (short) 20);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertEquals(true, decorator.containsValue((short) 10));
        assertEquals(true, decorator.containsValue((short) 20));
        assertEquals(false, decorator.containsValue((short) 30));
        assertEquals(false, decorator.containsValue(null));
        assertEquals(false, decorator.containsValue("test"));
    }

    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 10);
        map.put(2.0, (short) 10);
        map.put(3.0, (short) 20);
        
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertEquals(3, decorator.size());

        decorator.put(4.0, (short) 30);
        decorator.put(5.0, (short) 40);

        assertEquals(5, decorator.size());
    }

}