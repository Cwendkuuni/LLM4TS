import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put(1.0, (short) 5);
        assertEquals((short) 5, mapDecorator.get(1.0));
    }

    @Test
    public void testClear() {
        mapDecorator.put(2.0, (short) 10);
        mapDecorator.clear();
        assertNull(mapDecorator.get(2.0));
    }

    @Test
    public void testRemove() {
        mapDecorator.put(3.0, (short) 15);
        assertEquals((short) 15, mapDecorator.remove(3.0));
    }

    // Add more test cases for other methods and edge cases

}