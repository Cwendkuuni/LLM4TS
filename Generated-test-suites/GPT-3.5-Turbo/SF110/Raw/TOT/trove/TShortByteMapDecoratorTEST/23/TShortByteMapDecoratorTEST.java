import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPut() {
        // Typical use case
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.put((short) 1, (byte) 10));

        // Adding null key
        assertEquals(Byte.valueOf((byte) 20), mapDecorator.put(null, (byte) 20));

        // Adding null value
        assertNull(mapDecorator.put((short) 2, null));

        // Edge case: key being the minimum value
        assertEquals(Byte.valueOf((byte) 30), mapDecorator.put(Short.MIN_VALUE, (byte) 30));

        // Edge case: value being the minimum value
        assertEquals(Byte.valueOf(Byte.MIN_VALUE), mapDecorator.put((short) 3, Byte.MIN_VALUE));
    }

    @Test
    public void testGet() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);

        // Typical use case
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.get((short) 1));

        // Null key
        assertEquals(Byte.valueOf((byte) 20), mapDecorator.get(null));

        // Non-existing key
        assertNull(mapDecorator.get((short) 3));
    }

    @Test
    public void testClear() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);

        // Typical use case
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);

        // Typical use case
        assertEquals(Byte.valueOf((byte) 10), mapDecorator.remove((short) 1));

        // Null key
        assertEquals(Byte.valueOf((byte) 20), mapDecorator.remove(null));

        // Non-existing key
        assertNull(mapDecorator.remove((short) 3));
    }

    @Test
    public void testEntrySet() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);

        // Typical use case
        assertEquals(2, mapDecorator.entrySet().size());

        // Contains an entry
        assertTrue(mapDecorator.entrySet().contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 10)));

        // Entry iteration
        for (Map.Entry<Short, Byte> entry : mapDecorator.entrySet()) {
            Short key = entry.getKey();
            Byte value = entry.getValue();
            assertNotNull(key);
            assertNotNull(value);
        }

        // Clear entry set
        mapDecorator.entrySet().clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);

        // Typical use case
        assertTrue(mapDecorator.containsValue((byte) 10));

        // Non-existing value
        assertFalse(mapDecorator.containsValue((byte) 30));
    }

    @Test
    public void testContainsKey() {
        mapDecorator.put((short) 1, (byte) 10);
        mapDecorator.put((short) 2, (byte) 20);

        // Typical use case
        assertTrue(mapDecorator.containsKey((short) 1));

        // Null key
        assertTrue(mapDecorator.containsKey(null));

        // Non-existing key
        assertFalse(mapDecorator.containsKey((short) 3));
    }

    @Test
    public void testSize() {
        // Empty map
        assertEquals(0, mapDecorator.size());

        // Non-empty map
        mapDecorator.put((short) 1, (byte) 10);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        // Empty map
        assertTrue(mapDecorator.isEmpty());

        // Non-empty map
        mapDecorator.put((short) 1, (byte) 10);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator otherMapDecorator = new TShortByteMapDecorator();
        otherMapDecorator.put((short) 3, (byte) 30);
        otherMapDecorator.put((short) 4, (byte) 40);

        // Adding all entries from another map
        mapDecorator.putAll(otherMapDecorator);

        assertTrue(mapDecorator.containsKey((short) 3));
        assertTrue(mapDecorator.containsKey((short) 4));
        assertEquals(Byte.valueOf((byte) 30), mapDecorator.get((short) 3));
        assertEquals(Byte.valueOf((byte) 40), mapDecorator.get((short) 4));
    }

    @Test
    public void testWrapKeyAndUnwrapKey() {
        // Typical use case
        short key = 10;
        Short wrappedKey = mapDecorator.wrapKey(key);
        assertEquals(key, mapDecorator.unwrapKey(wrappedKey));

        // Edge case: minimum value
        key = Short.MIN_VALUE;
        wrappedKey = mapDecorator.wrapKey(key);
        assertEquals(key, mapDecorator.unwrapKey(wrappedKey));
    }

    @Test
    public void testWrapValueAndUnwrapValue() {
        // Typical use case
        byte value = 10;
        Byte wrappedValue = mapDecorator.wrapValue(value);
        assertEquals(value, mapDecorator.unwrapValue(wrappedValue));

        // Edge case: minimum value
        value = Byte.MIN_VALUE;
        wrappedValue = mapDecorator.wrapValue(value);
        assertEquals(value, mapDecorator.unwrapValue(wrappedValue));
    }

    @Test
    public void testReadExternal() {
        // Not tested as it requires ObjectInput parameter
    }

    @Test
    public void testWriteExternal() {
        // Not tested as it requires ObjectOutput parameter
    }
}