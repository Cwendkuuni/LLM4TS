import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 2);
        assertEquals((short) 2, map.get(1.0));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(2.0, (short) 3);
        assertEquals((short) 3, map.get(2.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(3.0, (short) 4);
        map.clear();
        assertNull(map.get(3.0));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(4.0, (short) 5);
        assertEquals((short) 5, map.remove(4.0));
    }

    // Add more test cases for other methods as well

}