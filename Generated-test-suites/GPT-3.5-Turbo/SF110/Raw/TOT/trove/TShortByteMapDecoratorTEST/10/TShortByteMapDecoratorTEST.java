import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(Byte.valueOf((byte) 2), mapDecorator.get((short) 1));
        assertNull(mapDecorator.get((short) 2));
    }

    @Test
    public void testPutAndGetWithNull() {
        mapDecorator.put(null, null);
        assertEquals(Byte.valueOf((byte) 0), mapDecorator.get(null));
    }

    @Test
    public void testPutWithExistingValue() {
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(Byte.valueOf((byte) 2), mapDecorator.put((short) 1, (byte) 3));
        assertEquals(Byte.valueOf((byte) 3), mapDecorator.get((short) 1));
    }

    @Test
    public void testGetWithNonShortKey() {
        assertNull(mapDecorator.get("key"));
        assertNull(mapDecorator.get(1));
    }

    @Test
    public void testClear() {
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testRemoveWithKey() {
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(Byte.valueOf((byte) 2), mapDecorator.remove((short) 1));
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testRemoveWithNonShortKey() {
        assertNull(mapDecorator.remove("key"));
        assertNull(mapDecorator.remove(1));
    }

    @Test
    public void testEntrySet() {
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.put((short) 3, (byte) 4);

        Map.Entry<Short, Byte> entry1 = new TShortByteMapDecorator.ShortByteEntry((short) 1, (byte) 2);
        Map.Entry<Short, Byte> entry2 = new TShortByteMapDecorator.ShortByteEntry((short) 3, (byte) 4);

        assertTrue(mapDecorator.entrySet().contains(entry1));
        assertTrue(mapDecorator.entrySet().contains(entry2));

        mapDecorator.entrySet().remove(entry1);
        assertFalse(mapDecorator.entrySet().contains(entry1));
        assertEquals(1, mapDecorator.size());

        mapDecorator.entrySet().clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        mapDecorator.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsValue((byte) 2));
        assertFalse(mapDecorator.containsValue((byte) 3));
    }

    @Test
    public void testContainsKey() {
        mapDecorator.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(1, mapDecorator.size());
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short) 1, (byte) 2);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> testMap = new TShortByteHashMap();
        testMap.put((short) 1, (byte) 2);
        testMap.put((short) 3, (byte) 4);

        mapDecorator.putAll(testMap);

        assertEquals(Byte.valueOf((byte) 2), mapDecorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 4), mapDecorator.get((short) 3));
    }

    @Test
    public void testWrapAndUnwrapKey() {
        short key1 = 1;
        short key2 = -1;
        Short wrappedKey1 = mapDecorator.wrapKey(key1);
        Short wrappedKey2 = mapDecorator.wrapKey(key2);

        assertEquals(key1, mapDecorator.unwrapKey(wrappedKey1));
        assertEquals(key2, mapDecorator.unwrapKey(wrappedKey2));
    }

    @Test
    public void testWrapAndUnwrapValue() {
        byte value1 = 2;
        byte value2 = -2;
        Byte wrappedValue1 = mapDecorator.wrapValue(value1);
        Byte wrappedValue2 = mapDecorator.wrapValue(value2);

        assertEquals(value1, mapDecorator.unwrapValue(wrappedValue1));
        assertEquals(value2, mapDecorator.unwrapValue(wrappedValue2));
    }

    @Test
    public void testExternalization() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            mapDecorator.put((short) 1, (byte) 2);
            mapDecorator.put((short) 3, (byte) 4);

            objectOutputStream.writeObject(mapDecorator);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            TShortByteMapDecorator deserializedMapDecorator = (TShortByteMapDecorator) objectInputStream.readObject();

            assertEquals(Byte.valueOf((byte) 2), deserializedMapDecorator.get((short) 1));
            assertEquals(Byte.valueOf((byte) 4), deserializedMapDecorator.get((short) 3));
        } catch (Exception e) {
            fail("Exception occurred during externalization: " + e.getMessage());
        }
    }
}