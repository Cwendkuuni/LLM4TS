import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 5.0;
        Short value = 10;

        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(2.0, (short) 3);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Double key = 4.0;
        Short value = 7;

        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        assertNull(mapDecorator.get(key));
    }

    // Add more test cases for other methods as necessary

}