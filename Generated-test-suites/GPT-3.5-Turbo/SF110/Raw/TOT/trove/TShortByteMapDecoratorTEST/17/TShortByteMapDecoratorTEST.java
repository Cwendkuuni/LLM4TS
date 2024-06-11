import static org.junit.Assert.*;
import org.junit.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put((short) 1, (byte) 10);
        assertEquals((byte) 10, mapDecorator.get((short) 1).byteValue());

        mapDecorator.put((short) 2, (byte) 20);
        assertEquals((byte) 20, mapDecorator.get((short) 2).byteValue());

        mapDecorator.put((short) 3, (byte) 30);
        assertEquals((byte) 30, mapDecorator.get((short) 3).byteValue());
    }

    @Test
    public void testPutNullKey() {
        mapDecorator.put(null, (byte) 10);
        assertNull(mapDecorator.get(null));

        mapDecorator.put(null, (byte) 20);
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testPutNullValue() {
        mapDecorator.put((short) 1, null);
        assertNull(mapDecorator.get((short) 1));

        mapDecorator.put((short) 2, null);
        assertNull(mapDecorator.get((short) 2));
    }

    @Test
    public void testGetSize() {
        assertEquals(0, mapDecorator.size());

        mapDecorator.put((short) 1, (byte) 10);
        assertEquals(1, mapDecorator.size());

        mapDecorator.put((short) 2, (byte) 20);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testContainsKey() {
        assertFalse(mapDecorator.containsKey((short) 1));

        mapDecorator.put((short) 1, (byte) 10);
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testContainsValue() {
        assertFalse(mapDecorator.containsValue((byte) 10));

        mapDecorator.put((short) 1, (byte) 10);
        assertTrue(mapDecorator.containsValue((byte) 10));
        assertFalse(mapDecorator.containsValue((byte) 20));
    }

    @Test
    public void testRemove() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);

        assertEquals((byte) 10, mapDecorator.remove((short) 1).byteValue());
        assertNull(mapDecorator.get((short) 1));

        assertEquals((byte) 20, mapDecorator.remove((short) 2).byteValue());
        assertNull(mapDecorator.get((short) 2));
    }

    @After
    public void tearDown() {
        mapDecorator.clear();
    }

}