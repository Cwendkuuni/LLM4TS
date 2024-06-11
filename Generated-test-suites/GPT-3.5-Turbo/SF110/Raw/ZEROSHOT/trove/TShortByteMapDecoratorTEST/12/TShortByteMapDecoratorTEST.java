import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator map;

    @Before
    public void setUp() {
        map = new TShortByteMapDecorator(new TShortByteMap());
    }

    @Test
    public void testPutAndGet() {
        map.put((short) 1, (byte) 2);
        assertEquals((byte) 2, map.get((short) 1));
    }

    @Test
    public void testPutAndGetWithNullKey() {
        map.put(null, (byte) 2);
        assertEquals((byte) 2, map.get(null));
    }

    @Test
    public void testPutAndGetWithNullValue() {
        map.put((short) 1, null);
        assertNull(map.get((short) 1));
    }

    @Test
    public void testPutAndGetWithNullKeyAndValue() {
        map.put(null, null);
        assertNull(map.get(null));
    }

    @Test
    public void testRemove() {
        map.put((short) 1, (byte) 2);
        assertEquals(1, map.size());
        assertEquals((byte) 2, map.remove((short) 1));
        assertEquals(0, map.size());
        assertNull(map.get((short) 1));
    }

    @Test
    public void testRemoveWithNullKey() {
        map.put(null, (byte) 2);
        assertEquals(1, map.size());
        assertEquals((byte) 2, map.remove(null));
        assertEquals(0, map.size());
        assertNull(map.get(null));
    }

    @Test
    public void testClear() {
        map.put((short) 1, (byte) 2);
        map.put((short) 3, (byte) 4);
        assertEquals(2, map.size());
        map.clear();
        assertEquals(0, map.size());
        assertNull(map.get((short) 1));
        assertNull(map.get((short) 3));
    }

    @Test
    public void testContainsKey() {
        map.put((short) 1, (byte) 2);
        assertTrue(map.containsKey((short) 1));
        assertFalse(map.containsKey((short) 2));
    }

    @Test
    public void testContainsKeyWithNullKey() {
        map.put(null, (byte) 2);
        assertTrue(map.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        map.put((short) 1, (byte) 2);
        assertTrue(map.containsValue((byte) 2));
        assertFalse(map.containsValue((byte) 3));
    }

    @Test
    public void testSize() {
        map.put((short) 1, (byte) 2);
        map.put((short) 3, (byte) 4);
        assertEquals(2, map.size());
        map.remove((short) 1);
        assertEquals(1, map.size());
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(map.isEmpty());
        map.put((short) 1, (byte) 2);
        assertFalse(map.isEmpty());
        map.remove((short) 1);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> input = new HashMap<>();
        input.put((short) 1, (byte) 2);
        input.put((short) 3, (byte) 4);
        map.putAll(input);
        assertEquals(2, map.size());
        assertEquals((byte) 2, map.get((short) 1));
        assertEquals((byte) 4, map.get((short) 3));
    }

    @Test
    public void testClone() {
        map.put((short) 1, (byte) 2);
        TShortByteMapDecorator clonedMap = (TShortByteMapDecorator) map.clone();
        assertEquals(map.size(), clonedMap.size());
        assertEquals(map.containsKey((short) 1), clonedMap.containsKey((short) 1));
        assertEquals(map.containsValue((byte) 2), clonedMap.containsValue((byte) 2));
    }

    @Test
    public void testEntrySet() {
        map.put((short) 1, (byte) 2);
        map.put((short) 3, (byte) 4);
        assertEquals(2, map.entrySet().size());
        for (Map.Entry<Short, Byte> entry : map.entrySet()) {
            assertTrue(map.containsKey(entry.getKey()));
            assertTrue(map.containsValue(entry.getValue()));
        }
    }
}