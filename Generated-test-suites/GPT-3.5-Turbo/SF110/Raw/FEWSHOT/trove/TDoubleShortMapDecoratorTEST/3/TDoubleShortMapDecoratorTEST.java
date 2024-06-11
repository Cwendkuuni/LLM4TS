package gnu.trove.decorator;

import gnu.trove.map.TDoubleShortMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
  
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        mapDecorator.put(1.0, (short) 2);
        mapDecorator.put(-0.5, (short) 10);
        
        assertEquals(2, mapDecorator.size());
        assertEquals((Short) 2, mapDecorator.get(1.0));
        assertEquals((Short) 10, mapDecorator.get(-0.5));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        mapDecorator.put(1.0, (short) 2);
        mapDecorator.put(-0.5, (short) 10);

        mapDecorator.remove(1.0);

        assertNull(mapDecorator.get(1.0));
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        mapDecorator.put(1.0, (short) 2);
        mapDecorator.put(-0.5, (short) 10);

        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(2.5));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        mapDecorator.put(1.0, (short) 2);
        mapDecorator.put(-0.5, (short) 10);

        assertTrue(mapDecorator.containsValue((short) 2));
        assertFalse(mapDecorator.containsValue((short) 5));
    }

    @Test
    public void testSizeAndClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        mapDecorator.put(1.0, (short) 2);
        mapDecorator.put(-0.5, (short) 10);

        assertEquals(2, mapDecorator.size());

        mapDecorator.clear();

        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator1 = new TDoubleShortMapDecorator();

        mapDecorator1.put(1.0, (short) 2);
        mapDecorator1.put(-0.5, (short) 10);

        TDoubleShortMapDecorator mapDecorator2 = new TDoubleShortMapDecorator();

        mapDecorator2.putAll(mapDecorator1);

        assertEquals(2, mapDecorator2.size());
        assertEquals((Short) 2, mapDecorator2.get(1.0));
        assertEquals((Short) 10, mapDecorator2.get(-0.5));
    }
}