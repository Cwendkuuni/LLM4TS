
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

public class TShortByteMapDecoratorTest {
    TShortByteMapDecorator map = new TShortByteMapDecorator();

    @Test
    public void testPutAndGet() {
        map.put((short)1, (byte)10);

        assertEquals(Byte.valueOf((byte)10), map.get((short)1));
    }

    @Test
    public void testClear() {
        map.put((short)1, (byte)10);
        map.clear();

        assertNull(map.get((short)1));
    }

    @Test
    public void testRemove() {
        map.put((short)1, (byte)10);

        assertEquals(Byte.valueOf((byte)10), map.remove((short)1));
        assertNull(map.remove((short)1));
    }

    @Test
    public void testContainsValue() {
        map.put((short)1, (byte)10);

        assertTrue(map.containsValue((byte)10));
        assertFalse(map.containsValue((byte)20));
    }

    @Test
    public void testContainsKey() {
        map.put((short)1, (byte)10);

        assertTrue(map.containsKey((short)1));
        assertFalse(map.containsKey((short)2));
    }

    @Test
    public void testSize() {
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);

        assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(map.isEmpty());

        map.put((short)1, (byte)10);

        assertFalse(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> otherMap = new HashMap<>();
        otherMap.put((short)1, (byte)10);
        otherMap.put((short)2, (byte)20);

        map.putAll(otherMap);

        assertEquals(Byte.valueOf((byte)10), map.get((short)1));
        assertEquals(Byte.valueOf((byte)20), map.get((short)2));
    }
}

