import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        Double key = 5.0;
        Short value = 10;
        mapDecorator.put(key, value);
        Short retrievedValue = mapDecorator.get(key);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testPutWithNullKey() {
        Short value = 20;
        Short retrievedValue = mapDecorator.put(null, value);
        assertNull(retrievedValue);
    }

    @Test
    public void testPutWithNullValue() {
        Double key = 10.0;
        Short retrievedValue = mapDecorator.put(key, null);
        assertNull(retrievedValue);
    }

    @Test
    public void testGetWithNonDoubleKey() {
        Object key = "notDouble";
        Short retrievedValue = mapDecorator.get(key);
        assertNull(retrievedValue);
    }

    @Test
    public void testRemove() {
        Double key = 15.0;
        Short value = 30;
        mapDecorator.put(key, value);
        Short removedValue = mapDecorator.remove(key);
        assertEquals(value, removedValue);
    }

    @Test
    public void testRemoveWithNonDoubleKey() {
        Object key = "notDouble";
        Short removedValue = mapDecorator.remove(key);
        assertNull(removedValue);
    }

    @Test
    public void testClear() {
        mapDecorator.put(25.0, 40);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        Double key = 30.0;
        mapDecorator.put(key, 50);
        assertTrue(mapDecorator.containsKey(key));
        assertFalse(mapDecorator.containsKey(35.0));
    }

    @Test
    public void testContainsValue() {
        Short value = 60;
        mapDecorator.put(35.0, value);
        assertTrue(mapDecorator.containsValue(value));
        assertFalse(mapDecorator.containsValue((short) 70));
    }
}