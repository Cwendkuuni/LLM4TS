
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        // Test basic functionality
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.put((short) 1, (byte) 2));
        assertEquals(Byte.valueOf((byte) 2), decorator.put((short) 1, (byte) 3));

        // Test null key
        assertNull(decorator.put(null, (byte) 4));
        assertEquals(Byte.valueOf((byte) 4), decorator.get(null));

        // Test null value
        decorator.clear();
        decorator.put((short) 1, (byte) 2);
        assertNull(decorator.put((short) 2, null));
        assertNull(decorator.get((short) 2));
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        decorator.put((short) 3, (byte) 4);

        // Test existing key
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 3), decorator.get((short) 2));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));

        // Test null key
        assertNull(decorator.get(null));

        // Test non-existing key
        assertNull(decorator.get((short) 4));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);

        // Test existing key
        assertEquals(Byte.valueOf((byte) 2), decorator.remove((short) 1));

        // Test null key
        decorator.clear();
        decorator.put((short) 1, (byte) 2);
        assertNull(decorator.remove(null));

        // Test non-existing key
        assertNull(decorator.remove((short) 3));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);

        // Test size
        assertEquals(2, decorator.entrySet().size());

        // Test isEmpty
        assertFalse(decorator.entrySet().isEmpty());

        // Test contains
        assertTrue(decorator.entrySet().contains(new AbstractMap.SimpleEntry<>((short) 1, (byte) 2)));

        // Test iterator
        Iterator<Map.Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
        assertTrue(iterator.hasNext());
        Map.Entry<Short, Byte> entry = iterator.next();
        assertEquals((short) 1, (short) entry.getKey());
        assertEquals(Byte.valueOf((byte) 2), entry.getValue());
        assertTrue(iterator.hasNext());
        entry = iterator.next();
        assertEquals((short) 2, (short) entry.getKey());
        assertEquals(Byte.valueOf((byte) 3), entry.getValue());
        assertFalse(iterator.hasNext());

        // Test remove
        decorator.entrySet().remove(new AbstractMap.SimpleEntry<>((short) 1, (byte) 2));
        assertEquals(1, decorator.entrySet().size());
        assertFalse(decorator.entrySet().contains(new AbstractMap.SimpleEntry<>((short) 1, (byte) 2)));

        // Test clear
        decorator.entrySet().clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);

        // Test existing value
        assertTrue(decorator.containsValue((byte) 2));

        // Test null value
        decorator.clear();
        decorator.put((short) 1, (byte) 2);
        assertFalse(decorator.containsValue(null));

        // Test non-existing value
        assertFalse(decorator.containsValue((byte) 3));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);

        // Test existing key
        assertTrue(decorator.containsKey((short) 1));

        // Test null key
        assertTrue(decorator.containsKey(null));

        // Test non-existing key
        assertFalse(decorator.containsKey((short) 3));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
        decorator.put((short) 1, (byte) 2);
        assertFalse(decorator.isEmpty());
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);
        map.put((short) 3, (byte) 4);

        decorator.putAll(map);
        assertEquals(3, decorator.size());
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 3), decorator.get((short) 2));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));
    }

    @Test
    public void testWrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals((short) 1, (short) decorator.wrapKey((short) 1));
    }

    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals((short) 1, decorator.unwrapKey((short) 1));
    }

    @Test
    public void testWrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(Byte.valueOf((byte) 2), decorator.wrapValue((byte) 2));
    }

    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals((byte) 3, decorator.unwrapValue((byte) 3));
    }

    @Test
    public void testReadExternal() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            decorator.writeExternal(objectOutputStream);
            objectOutputStream.flush();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            TShortByteMapDecorator clone = new TShortByteMapDecorator();
            clone.readExternal(objectInputStream);

            assertEquals(decorator.size(), clone.size());
            assertEquals(decorator.get((short) 1), clone.get((short) 1));
            assertEquals(decorator.get((short) 2), clone.get((short) 2));

        } catch (IOException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteExternal() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            decorator.writeExternal(objectOutputStream);
            objectOutputStream.flush();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            TShortByteMapDecorator clone = new TShortByteMapDecorator();
            clone.readExternal(objectInputStream);

            assertEquals(decorator.size(), clone.size());
            assertEquals(decorator.get((short) 1), clone.get((short) 1));
            assertEquals(decorator.get((short) 2), clone.get((short) 2));

        } catch (IOException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }
}
