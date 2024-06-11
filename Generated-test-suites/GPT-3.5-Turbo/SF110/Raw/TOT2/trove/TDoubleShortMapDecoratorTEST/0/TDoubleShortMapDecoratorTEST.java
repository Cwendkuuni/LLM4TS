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
        assertNull(mapDecorator.put(1.0, (short) 1));
        assertEquals(Short.valueOf((short) 1), mapDecorator.put(1.0, (short) 2));
        assertEquals(Short.valueOf((short) 2), mapDecorator.get(1.0));
    }

    @Test
    public void testRemove() {
        assertNull(mapDecorator.remove(2.0));
        mapDecorator.put(2.0, (short) 3);
        assertEquals(Short.valueOf((short) 3), mapDecorator.remove(2.0));
        assertNull(mapDecorator.remove(2.0));
    }

    @Test
    public void testSizeAndIsEmpty() {
        assertEquals(0, mapDecorator.size());
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put(3.0, (short) 4);
        assertEquals(1, mapDecorator.size());
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertFalse(mapDecorator.containsKey(4.0));
        mapDecorator.put(4.0, (short) 5);
        assertTrue(mapDecorator.containsKey(4.0));
    }

    @Test
    public void testContainsValue() {
        assertFalse(mapDecorator.containsValue((short) 6));
        mapDecorator.put(5.0, (short) 6);
        assertTrue(mapDecorator.containsValue((short) 6));
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> testMap = new HashMap<>();
        testMap.put(6.0, (short) 7);
        testMap.put(7.0, (short) 8);
        mapDecorator.putAll(testMap);
        assertEquals(Short.valueOf((short) 7), mapDecorator.get(6.0));
        assertEquals(Short.valueOf((short) 8), mapDecorator.get(7.0));
    }

    // Add more test cases for edge cases and error scenarios here

}