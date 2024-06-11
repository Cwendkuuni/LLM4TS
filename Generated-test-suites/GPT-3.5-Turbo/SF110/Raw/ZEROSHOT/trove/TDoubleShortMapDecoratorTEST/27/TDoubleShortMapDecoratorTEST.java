import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        assertEquals((short) 1, mapDecorator.get(1.0).shortValue());

        mapDecorator.put(null, null);
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        assertEquals((short) 1, mapDecorator.get(1.0).shortValue());

        assertNull(mapDecorator.get(2.0));
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);

        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);

        assertEquals((short) 1, mapDecorator.remove(1.0).shortValue());
        assertNull(mapDecorator.remove(2.0));
        assertNull(mapDecorator.remove(null));
    }

    // Repeat for other methods

}