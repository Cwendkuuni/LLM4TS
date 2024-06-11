import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator createDecorator() {
        return new TDoubleShortMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator decorator = createDecorator();
        Double key = 1.0;
        Short value = 2;
        decorator.put(key, value);
        Short retrievedValue = decorator.get(key);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = createDecorator();
        Double key = 1.0;
        Short value = 2;
        decorator.put(key, value);
        Short removedValue = decorator.remove(key);
        assertNull(decorator.get(key));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = createDecorator();
        Double key1 = 1.0;
        Double key2 = 2.0;
        Short value1 = 1;
        Short value2 = 2;
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = createDecorator();
        Double key = 1.0;
        Short value = 2;
        decorator.put(key, value);
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    // Additional edge cases and error scenarios should be tested here

}