import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    TDoubleShortMapDecorator decorator;

    @Test
    public void testPutAndGet() {
        decorator = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 10;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testClear() {
        decorator = new TDoubleShortMapDecorator();
        decorator.put(2.0, 20);
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        decorator = new TDoubleShortMapDecorator();
        Double key = 3.0;
        Short value = 30;
        decorator.put(key, value);
        assertEquals(value, decorator.remove(key));
    }

    @Test
    public void testContainsValue() {
        decorator = new TDoubleShortMapDecorator();
        Short value = 40;
        decorator.put(4.0, value);
        assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        decorator = new TDoubleShortMapDecorator();
        Double key = 5.0;
        decorator.put(key, (short)50);
        assertTrue(decorator.containsKey(key));
    }

    @Test
    public void testSize() {
        decorator = new TDoubleShortMapDecorator();
        decorator.put(6.0, (short)60);
        assertEquals(1, decorator.size());
    }

    @Test
    public void testPutAll() {
        decorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(7.0, (short)70);
        map.put(8.0, (short)80);
        decorator.putAll(map);
        assertEquals(2, decorator.size());
    }

    // Additional test cases for edge cases and exception handling can be added here

}