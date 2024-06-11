
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        // Test put
        assertNull(mapDecorator.put((short)1, (byte)10));
        assertNull(mapDecorator.put((short)2, (byte)20));
        assertEquals((byte)10, mapDecorator.put((short)1, (byte)30).byteValue());

        // Test get
        assertEquals((byte)30, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)20, mapDecorator.get((short)2).byteValue());
        assertNull(mapDecorator.get((short)3));
    }

    @Test
    public void testClearAndIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        // Test isEmpty
        assertTrue(mapDecorator.isEmpty());

        // Test put
        mapDecorator.put((short)1, (byte)10);
        mapDecorator.put((short)2, (byte)20);
        assertFalse(mapDecorator.isEmpty());

        // Test clear
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        // Test put
        mapDecorator.put((short)1, (byte)10);
        mapDecorator.put((short)2, (byte)20);

        // Test remove
        assertEquals((byte)10, mapDecorator.remove((short)1).byteValue());
        assertNull(mapDecorator.remove((short)1));
        assertEquals((byte)20, mapDecorator.remove((short)2).byteValue());
        assertNull(mapDecorator.remove((short)2));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        // Test put
        mapDecorator.put((short)1, (byte)10);
        mapDecorator.put((short)2, (byte)20);

        // Test containsKey
        assertTrue(mapDecorator.containsKey((short)1));
        assertTrue(mapDecorator.containsKey((short)2));
        assertFalse(mapDecorator.containsKey((short)3));
        assertFalse(mapDecorator.containsKey(null));
        assertFalse(mapDecorator.containsKey("test"));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        // Test put
        mapDecorator.put((short)1, (byte)10);
        mapDecorator.put((short)2, (byte)20);

        // Test containsValue
        assertTrue(mapDecorator.containsValue((byte)10));
        assertTrue(mapDecorator.containsValue((byte)20));
        assertFalse(mapDecorator.containsValue((byte)30));
        assertFalse(mapDecorator.containsValue(null));
        assertFalse(mapDecorator.containsValue("test"));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        // Test put
        mapDecorator.put((short)1, (byte)10);
        mapDecorator.put((short)2, (byte)20);

        // Test size
        assertEquals(2, mapDecorator.size());

        // Test remove
        mapDecorator.remove((short)1);
        assertEquals(1, mapDecorator.size());
        mapDecorator.remove((short)2);
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        // Test putAll
        Map<Short, Byte> map = new HashMap<>();
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        mapDecorator.putAll(map);

        assertEquals((byte)10, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)20, mapDecorator.get((short)2).byteValue());
        assertNull(mapDecorator.get((short)3));
    }
}

