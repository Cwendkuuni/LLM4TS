import gnu.trove.map.TShortByteMap;
import gnu.trove.map.hash.TShortByteHashMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator decorator;
    private TShortByteMap map;

    @Before
    public void setUp() {
        map = new TShortByteHashMap();
        decorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        Short key = 1;
        Byte value = 2;

        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testPutNullKeyAndValue() {
        decorator.put(null, null);

        assertTrue(decorator.containsKey(null));
        assertTrue(decorator.containsValue(null));
        assertNull(decorator.get(null));
    }

    @Test
    public void testPutAndGetWithNoEntryKeyAndValue() {
        map.putNoEntryKey((short) -1);
        map.putNoEntryValue((byte) -1);

        Short key = 1;
        Byte value = 2;

        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
        assertEquals(value, decorator.get(key));

        assertFalse(decorator.containsKey((short) -1));
        assertFalse(decorator.containsValue((byte) -1));
        assertNull(decorator.get((short) -1));
    }

    @Test
    public void testGetWithNonShortKey() {
        Object key = new Object();

        assertNull(decorator.get(key));
    }

    @Test
    public void testClear() {
        for (int i = 0; i < 5; i++) {
            decorator.put((short) i, (byte) (i + 1));
        }

        assertFalse(decorator.isEmpty());

        decorator.clear();

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        Short key = 1;
        Byte value = 2;

        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
        assertEquals(value, decorator.remove(key));

        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(value));
        assertNull(decorator.remove(key));
    }

    @Test
    public void testEntrySet() {
        for (int i = 0; i < 5; i++) {
            decorator.put((short) i, (byte) (i + 1));
        }

        Map.Entry<Short, Byte> entry = decorator.entrySet().iterator().next();

        assertNotNull(entry);
        assertNotNull(entry.getKey());
        assertNotNull(entry.getValue());
        assertTrue(decorator.containsKey(entry.getKey()));
        assertEquals(entry.getValue(), decorator.get(entry.getKey()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAdd() {
        decorator.entrySet().add(null);
    }

    @Test
    public void testEntrySetRemove() {
        for (int i = 0; i < 5; i++) {
            decorator.put((short) i, (byte) (i + 1));
        }

        Map.Entry<Short, Byte> entry = decorator.entrySet().iterator().next();
        assertTrue(decorator.entrySet().remove(entry));

        assertFalse(decorator.containsKey(entry.getKey()));
        assertFalse(decorator.containsValue(entry.getValue()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAddAll() {
        decorator.entrySet().addAll(null);
    }

    @Test
    public void testEntrySetClear() {
        for (int i = 0; i < 5; i++) {
            decorator.put((short) i, (byte) (i + 1));
        }

        assertFalse(decorator.isEmpty());

        decorator.entrySet().clear();

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        Byte value = 2;

        assertFalse(decorator.containsValue(value));

        decorator.put((short) 1, value);

        assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        Short key = 1;

        assertFalse(decorator.containsKey(key));

        decorator.put(key, (byte) 2);

        assertTrue(decorator.containsKey(key));
    }

    @Test
    public void testSize() {
        assertEquals(0, decorator.size());

        decorator.put((short) 1, (byte) 2);

        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());

        decorator.put((short) 1, (byte) 2);

        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> inputMap = new TShortByteHashMap();
        for (int i = 0; i < 5; i++) {
            inputMap.put((short) i, (byte) (i + 1));
        }

        decorator.putAll(inputMap);

        for (Map.Entry<Short, Byte> entry : inputMap.entrySet()) {
            assertTrue(decorator.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), decorator.get(entry.getKey()));
        }
    }

    @Test
    public void testWrapAndUnwrapKey() {
        short originalKey = -1;

        Short wrappedKey = decorator.wrapKey(originalKey);
        short unwrappedKey = decorator.unwrapKey(wrappedKey);

        assertEquals(originalKey, unwrappedKey);
    }

    @Test
    public void testWrapAndUnwrapValue() {
        byte originalValue = -1;

        Byte wrappedValue = decorator.wrapValue(originalValue);
        byte unwrappedValue = decorator.unwrapValue(wrappedValue);

        assertEquals(originalValue, unwrappedValue);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        Byte value = 2;

        decorator.put((short) 1, value);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator cloneDecorator = new TShortByteMapDecorator();
        cloneDecorator.readExternal(objectInputStream);
        objectInputStream.close();

        assertTrue(cloneDecorator.containsKey((short) 1));
        assertEquals(value, cloneDecorator.get((short) 1));
    }

    @Test
    public void testWriteExternal() throws IOException, ClassNotFoundException {
        Byte value = 2;

        decorator.put((short) 1, value);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator cloneDecorator = new TShortByteMapDecorator();
        cloneDecorator.readExternal(objectInputStream);
        objectInputStream.close();

        assertTrue(cloneDecorator.containsKey((short) 1));
        assertEquals(value, cloneDecorator.get((short) 1));
    }
}