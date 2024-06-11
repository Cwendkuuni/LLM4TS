import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator map;

    @Before
    public void setUp() {
        map = new TShortByteMapDecorator();
    }

    @After
    public void tearDown() {
        map = null;
    }

    @Test
    public void testPutAndGetTypical() {
        map.put((short) 1, (byte) 10);
        assertEquals((Byte) 10, map.get((short) 1));
    }

    @Test
    public void testPutAndGetNullKey() {
        map.put(null, (byte) 10);
        assertEquals((Byte) 10, map.get(null));
    }

    @Test
    public void testPutAndGetNullValue() {
        map.put((short) 1, null);
        assertNull(map.get((short) 1));
    }

    @Test
    public void testPutAndGetNullKeyAndValue() {
        map.put(null, null);
        assertNull(map.get(null));
    }

    @Test
    public void testPutAndGetNoEntryKey() {
        map.put((short) -32768, (byte) 10);
        assertEquals((Byte) 10, map.get((short) -32768));
    }

    @Test
    public void testPutAndGetNoEntryValue() {
        map.put((short) 1, (byte) -128);
        assertEquals((Byte) -128, map.get((short) 1));
    }

    @Test
    public void testPutAndGetNoEntryKeyAndValue() {
        map.put((short) -32768, (byte) -128);
        assertEquals((Byte) -128, map.get((short) -32768));
    }

    @Test
    public void testRemoveTypical() {
        map.put((short) 1, (byte) 10);
        Byte removedValue = map.remove((short) 1);
        assertEquals((Byte) 10, removedValue);
        assertNull(map.get((short) 1));
    }

    @Test
    public void testRemoveNullKey() {
        map.put(null, (byte) 10);
        Byte removedValue = map.remove(null);
        assertEquals((Byte) 10, removedValue);
        assertNull(map.get(null));
    }

    @Test
    public void testRemoveNoEntryKey() {
        map.put((short) -32768, (byte) 10);
        Byte removedValue = map.remove((short) -32768);
        assertEquals((Byte) 10, removedValue);
        assertNull(map.get((short) -32768));
    }

    @Test
    public void testContainsValueTypical() {
        map.put((short) 1, (byte) 10);
        assertTrue(map.containsValue((byte) 10));
    }

    @Test
    public void testContainsValueNullValue() {
        map.put((short) 1, null);
        assertTrue(map.containsValue(null));
    }

    @Test
    public void testContainsValueNoEntryValue() {
        map.put((short) 1, (byte) -128);
        assertTrue(map.containsValue((byte) -128));
    }

    @Test
    public void testContainsKeyTypical() {
        map.put((short) 1, (byte) 10);
        assertTrue(map.containsKey((short) 1));
    }

    @Test
    public void testContainsKeyNullKey() {
        map.put(null, (byte) 10);
        assertTrue(map.containsKey(null));
    }

    @Test
    public void testContainsKeyNoEntryKey() {
        map.put((short) -32768, (byte) 10);
        assertTrue(map.containsKey((short) -32768));
    }

    @Test
    public void testSizeEmpty() {
        assertEquals(0, map.size());
    }

    @Test
    public void testSizeNonEmpty() {
        map.put((short) 1, (byte) 10);
        assertEquals(1, map.size());
    }

    @Test
    public void testSizeAfterClear() {
        map.put((short) 1, (byte) 10);
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAllTypical() {
        Map<Short, Byte> otherMap = new HashMap<>();
        otherMap.put((short) 1, (byte) 10);
        otherMap.put((short) 2, (byte) 20);
        map.putAll(otherMap);
        assertEquals((Byte) 10, map.get((short) 1));
        assertEquals((Byte) 20, map.get((short) 2));
    }

    @Test
    public void testWrapAndUnwrapKey() {
        short originalKey = (short) 1;
        Short wrappedKey = map.wrapKey(originalKey);
        short unwrappedKey = map.unwrapKey(wrappedKey);
        assertEquals(originalKey, unwrappedKey);
    }

    @Test
    public void testWrapAndUnwrapValue() {
        byte originalValue = 10;
        Byte wrappedValue = map.wrapValue(originalValue);
        byte unwrappedValue = map.unwrapValue(wrappedValue);
        assertEquals(originalValue, unwrappedValue);
    }

    @Test
    public void testSerialization() {
        map.put((short) 1, (byte) 10);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(map);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            TShortByteMapDecorator deserializedMap = (TShortByteMapDecorator) ois.readObject();

            assertEquals((Byte) 10, deserializedMap.get((short) 1));
        } catch (IOException | ClassNotFoundException e) {
            fail("Exception occurred during serialization: " + e.getMessage());
        }
    }
}