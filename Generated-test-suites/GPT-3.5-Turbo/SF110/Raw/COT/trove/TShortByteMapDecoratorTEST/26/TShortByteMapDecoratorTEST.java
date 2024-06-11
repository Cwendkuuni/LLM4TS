import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;

    @Test
    public void testPutAndGet() {
        mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        assertNull(mapDecorator.put(key, value));
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testPutWithNullKey() {
        mapDecorator = new TShortByteMapDecorator();
        Byte value = 2;
        assertNull(mapDecorator.put(null, value));
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testPutWithNullValue() {
        mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        assertNull(mapDecorator.put(key, null));
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testGetWithInvalidKey() {
        mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertNull(mapDecorator.get(2));
    }

    @Test
    public void testClear() {
        mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);
        assertEquals(2, mapDecorator.size());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testRemoveWithInvalidKey() {
        mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertNull(mapDecorator.remove(2));
    }

    @Test
    public void testEntrySet() {
        mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);
        assertEquals(2, mapDecorator.entrySet().size());
    }

    @Test
    public void testContainsValue() {
        mapDecorator = new TShortByteMapDecorator();
        Byte value = 2;
        mapDecorator.put((short)1, value);
        assertTrue(mapDecorator.containsValue(value));
    }

    @Test
    public void testContainsValueWithInvalidValue() {
        mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        assertFalse(mapDecorator.containsValue((byte)3));
    }

    @Test
    public void testContainsKey() {
        mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        mapDecorator.put(key, (byte)2);
        assertTrue(mapDecorator.containsKey(key));
    }

    @Test
    public void testContainsKeyWithInvalidKey() {
        mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        assertFalse(mapDecorator.containsKey(2));
    }

    @Test
    public void testSizeAndIsEmpty() {
        mapDecorator = new TShortByteMapDecorator();
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short)1, (byte)2);
        assertFalse(mapDecorator.isEmpty());
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testPutAll() {
        mapDecorator = new TShortByteMapDecorator();
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)2);
        map.put((short)3, (byte)4);
        mapDecorator.putAll(map);
        assertEquals(2, mapDecorator.size());
        assertEquals((byte)2, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)4, mapDecorator.get((short)3).byteValue());
    }
}