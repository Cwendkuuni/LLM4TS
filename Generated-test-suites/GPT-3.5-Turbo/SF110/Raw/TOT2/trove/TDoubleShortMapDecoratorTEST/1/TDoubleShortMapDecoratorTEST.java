import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Short value = mapDecorator.put(1.0, (short) 2);
        assertEquals((short)2, (short)value);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(3.0, (short) 4);
        Short value = mapDecorator.get(3.0);
        assertEquals((short)4, (short)value);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(5.0, (short) 6);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    // Additional test cases for other public methods can be added here

}