
import static org.junit.Assert.*;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.impl.Constants;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals((byte) 2, mapDecorator.get((short) 1).byteValue());
        assertEquals(null, mapDecorator.get((short) 2));
    }

    @Test
    public void testPutNullKeyAndGet() {
        mapDecorator.put(null, (byte) 2);
        assertEquals((byte) 2, mapDecorator.get(Constants.DEFAULT_SHORT_NO_ENTRY_KEY).byteValue());
    }

    @Test
    public void testPutNullValueAndGet() {
        mapDecorator.put((short) 1, null);
        assertEquals(null, mapDecorator.get((short) 1));
        assertEquals(Constants.DEFAULT_BYTE_NO_ENTRY_VALUE, mapDecorator.get((short) 2).byteValue());
    }

    @Test
    public void testRemove() {
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals((byte) 2, mapDecorator.remove((short) 1).byteValue());
        assertEquals(null, mapDecorator.remove((short) 2));
    }

    @Test
    public void testRemoveNullKey() {
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(null, mapDecorator.remove(null));
        assertEquals(null, mapDecorator.get(Constants.DEFAULT_SHORT_NO_ENTRY_KEY));
    }

    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(1, mapDecorator.size());
        mapDecorator.remove((short) 1);
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short) 1, (byte) 2);
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.remove((short) 1);
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        mapDecorator.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testContainsValue() {
        mapDecorator.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsValue((byte) 2));
        assertFalse(mapDecorator.containsValue((byte) 3));
    }

    @Test
    public void testClear() {
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 2);
        map.put((short) 3, (byte) 4);
        mapDecorator.putAll(map);
        assertEquals((byte) 2, mapDecorator.get((short) 1).byteValue());
        assertEquals((byte) 4, mapDecorator.get((short) 3).byteValue());
    }
}

