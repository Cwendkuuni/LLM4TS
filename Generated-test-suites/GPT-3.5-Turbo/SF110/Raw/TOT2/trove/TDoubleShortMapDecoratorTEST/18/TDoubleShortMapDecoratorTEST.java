import gnu.trove.map.TDoubleShortMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    TDoubleShortMapDecorator tDoubleShortMapDecorator;

    @Test
    public void testPutAndGet() {
        tDoubleShortMapDecorator = new TDoubleShortMapDecorator();
        tDoubleShortMapDecorator.put(1.0, (short) 10);
        assertEquals((short) 10, (short) tDoubleShortMapDecorator.get(1.0));
    }

    @Test
    public void testRemove() {
        tDoubleShortMapDecorator = new TDoubleShortMapDecorator();
        tDoubleShortMapDecorator.put(2.0, (short) 20);
        assertEquals((short) 20, (short) tDoubleShortMapDecorator.remove(2.0));
    }

    @Test
    public void testSize() {
        tDoubleShortMapDecorator = new TDoubleShortMapDecorator();
        tDoubleShortMapDecorator.put(3.0, (short) 30);
        assertEquals(1, tDoubleShortMapDecorator.size());
    }

    @Test
    public void testContainsKey() {
        tDoubleShortMapDecorator = new TDoubleShortMapDecorator();
        tDoubleShortMapDecorator.put(4.0, (short) 40);
        assertTrue(tDoubleShortMapDecorator.containsKey(4.0));
    }

    @Test
    public void testPutAll() {
        tDoubleShortMapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(5.0, (short) 50);
        map.put(6.0, (short) 60);
        tDoubleShortMapDecorator.putAll(map);
        assertEquals(2, tDoubleShortMapDecorator.size());
    }

    @Test
    public void testClear() {
        tDoubleShortMapDecorator = new TDoubleShortMapDecorator();
        tDoubleShortMapDecorator.put(7.0, (short) 70);
        tDoubleShortMapDecorator.clear();
        assertEquals(0, tDoubleShortMapDecorator.size());
    }
}