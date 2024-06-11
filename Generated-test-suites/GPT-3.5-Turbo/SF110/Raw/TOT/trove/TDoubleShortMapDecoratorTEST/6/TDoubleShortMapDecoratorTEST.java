
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    TDoubleShortMapDecorator mapDecorator;

    @Test
    public void testPutAndGet() {
        mapDecorator = new TDoubleShortMapDecorator();
        assertEquals(0, mapDecorator.size());

        mapDecorator.put(1.0, (short) 1);
        assertEquals(1, mapDecorator.size());
        assertEquals(1, mapDecorator.get(1.0).shortValue());

        mapDecorator.put(2.0, (short) 2);
        assertEquals(2, mapDecorator.size());
        assertEquals(2, mapDecorator.get(2.0).shortValue());

        mapDecorator.put(null, (short) 3);
        assertEquals(3, mapDecorator.size());
        assertEquals(3, mapDecorator.get(null).shortValue());

        mapDecorator.put(3.0, null);
        assertEquals(4, mapDecorator.size());
        assertNull(mapDecorator.get(3.0));

        mapDecorator.put(null, null);
        assertEquals(5, mapDecorator.size());
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testRemove() {
        mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        mapDecorator.put(3.0, (short) 3);

        assertEquals(3, mapDecorator.size());
        assertEquals(1, mapDecorator.remove(1.0).shortValue());
        assertEquals(2, mapDecorator.size());

        assertEquals(2, mapDecorator.remove(2.0).shortValue());
        assertEquals(1, mapDecorator.size());

        assertNull(mapDecorator.remove(null));
        assertEquals(1, mapDecorator.size());
        assertEquals(3, mapDecorator.remove(3.0).shortValue());
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        mapDecorator = new TDoubleShortMapDecorator();
        assertFalse(mapDecorator.containsKey(1.0));

        mapDecorator.put(1.0, (short) 1);
        assertTrue(mapDecorator.containsKey(1.0));

        assertFalse(mapDecorator.containsKey(2.0));
        mapDecorator.put(2.0, (short) 2);
        assertTrue(mapDecorator.containsKey(2.0));

        assertTrue(mapDecorator.containsKey(null));
        mapDecorator.put(null, (short) 3);
        assertTrue(mapDecorator.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        mapDecorator = new TDoubleShortMapDecorator();
        assertFalse(mapDecorator.containsValue((short) 1));

        mapDecorator.put(1.0, (short) 1);
        assertTrue(mapDecorator.containsValue((short) 1));

        assertFalse(mapDecorator.containsValue((short) 2));
        mapDecorator.put(2.0, (short) 2);
        assertTrue(mapDecorator.containsValue((short) 2));

        assertTrue(mapDecorator.containsValue(null));
        mapDecorator.put(3.0, null);
        assertTrue(mapDecorator.containsValue(null));
    }

    @Test
    public void testSizeAndClear() {
        mapDecorator = new TDoubleShortMapDecorator();
        assertEquals(0, mapDecorator.size());

        mapDecorator.put(1.0, (short) 1);
        assertEquals(1, mapDecorator.size());

        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertTrue(mapDecorator.isEmpty());
    }
}

