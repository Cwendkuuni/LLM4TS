import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;
    private TDoubleShortMap map;

    @Before
    public void setUp() {
        map = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        // Typical case
        assertNull(mapDecorator.put(1.5, (short) 2));
        assertTrue(mapDecorator.containsKey(1.5));
        assertEquals(2, mapDecorator.get(1.5).shortValue());

        // Test null key
        assertNull(mapDecorator.put(null, (short) 3));
        assertTrue(mapDecorator.containsKey(null));
        assertEquals(3, mapDecorator.get(null).shortValue());

        // Test null value
        assertNull(mapDecorator.put(4.2, null));
        assertTrue(mapDecorator.containsKey(4.2));
        assertNull(mapDecorator.get(4.2));

        // Test existing key
        assertEquals(3, mapDecorator.put(null, (short) 5).shortValue());
        assertTrue(mapDecorator.containsKey(null));
        assertEquals(5, mapDecorator.get(null).shortValue());
    }

    @Test
    public void testGet() {
        // Typical case
        map.put(2.3, (short) 4);
        assertEquals(4, mapDecorator.get(2.3).shortValue());

        // Test null key
        map.put(map.getNoEntryKey(), (short) 6);
        assertNull(mapDecorator.get(null));

        // Test non-existing key
        assertNull(mapDecorator.get(8.7));
    }

    @Test
    public void testClear() {
        map.put(1.1, (short) 1);
        map.put(2.2, (short) 2);
        mapDecorator.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        // Typical case
        map.put(3.3, (short) 3);
        assertEquals(3, mapDecorator.remove(3.3).shortValue());
        assertFalse(mapDecorator.containsKey(3.3));

        // Test null key
        map.put(map.getNoEntryKey(), (short) 7);
        assertNull(mapDecorator.remove(null));
        assertFalse(mapDecorator.containsKey(null));

        // Test non-existing key
        assertNull(mapDecorator.remove(9.0));
    }

    @Test
    public void testContainsValue() {
        // Typical case
        map.put(4.4, (short) 4);
        assertTrue(mapDecorator.containsValue((short) 4));

        // Test non-existing value
        assertFalse(mapDecorator.containsValue((short) 5));
    }

    @Test
    public void testContainsKey() {
        // Typical case
        map.put(5.5, (short) 5);
        assertTrue(mapDecorator.containsKey(5.5));

        // Test null key
        map.put(map.getNoEntryKey(), (short) 8);
        assertTrue(mapDecorator.containsKey(null));

        // Test non-existing key
        assertFalse(mapDecorator.containsKey(10.0));
    }

    @Test
    public void testSize() {
        // Typical case
        assertEquals(0, mapDecorator.size());
        map.put(6.6, (short) 6);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        // Typical case
        assertTrue(mapDecorator.isEmpty());
        map.put(7.7, (short) 7);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        // Typical case
        map.put(8.8, (short) 8);
        map.put(9.9, (short) 9);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        decorator.putAll(map);

        assertTrue(decorator.containsKey(8.8));
        assertTrue(decorator.containsKey(9.9));
        assertEquals(8, decorator.get(8.8).shortValue());
        assertEquals(9, decorator.get(9.9).shortValue());
    }

    @Test
    public void testEntrySet() {
        // Typical case
        map.put(10.1, (short) 10);
        map.put(11.2, (short) 11);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        assertEquals(2, decorator.entrySet().size());

        // Test empty entry set
        decorator.clear();
        assertEquals(0, decorator.entrySet().size());

        // Test removing entries from entry set
        assertTrue(decorator.entrySet().remove(decorator.entrySet().iterator().next()));
        assertEquals(0, decorator.entrySet().size());
    }

    @Test
    public void testWrapUnwrapKey() {
        double key = 12.3;
        Double wrappedKey = mapDecorator.wrapKey(key);
        assertEquals(key, mapDecorator.unwrapKey(wrappedKey), 0);

        // Test null key
        wrappedKey = mapDecorator.wrapKey(map.getNoEntryKey());
        assertEquals(map.getNoEntryKey(), mapDecorator.unwrapKey(wrappedKey), 0);
    }

    @Test
    public void testWrapUnwrapValue() {
        short value = 13;
        Short wrappedValue = mapDecorator.wrapValue(value);
        assertEquals(value, mapDecorator.unwrapValue(wrappedValue));

        // Test null value
        wrappedValue = mapDecorator.wrapValue(map.getNoEntryValue());
        assertEquals(map.getNoEntryValue(), mapDecorator.unwrapValue(wrappedValue));
    }

    @Test
    public void testExternalization() throws IOException, ClassNotFoundException {
        // Write object to output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(mapDecorator);
        objectOutputStream.close();

        // Read object from input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        TDoubleShortMapDecorator deserializedMapDecorator = (TDoubleShortMapDecorator) objectInputStream.readObject();

        // Test deserialized object
        assertNotNull(deserializedMapDecorator);
        assertEquals(mapDecorator.size(), deserializedMapDecorator.size());
        for (Double key : mapDecorator.keySet()) {
            assertTrue(deserializedMapDecorator.containsKey(key));
            assertEquals(mapDecorator.get(key), deserializedMapDecorator.get(key));
        }
    }
}