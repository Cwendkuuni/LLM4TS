import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Short value = mapDecorator.put(1.0, (short) 5);
        assertEquals((short) 5, value);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 5);
        Short value = mapDecorator.get(1.0);
        assertEquals((short) 5, value);
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 5);
        Short value = mapDecorator.remove(1.0);
        assertEquals((short) 5, value);
    }
}