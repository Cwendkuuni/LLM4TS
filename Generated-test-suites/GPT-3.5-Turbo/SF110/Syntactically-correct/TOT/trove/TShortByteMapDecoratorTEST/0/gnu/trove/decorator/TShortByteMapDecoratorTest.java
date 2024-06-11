import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        TShortByteMap map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPutAndGet_NormalCase() {
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testPutAndGet_NullKey() {
        Short key = null;
        Byte value = 2;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testPutAndGet_NullValue() {
        Short key = 1;
        Byte value = null;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testPutAndGet_NullKeyAndValue() {
        Short key = null;
        Byte value = null;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testPutAndGet_NoEntryValue() {
        Short key = 1;
        Byte value = decorator._map.getNoEntryValue();
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testGet_NotFoundKey() {
        Short key = 1;
        assertNull(decorator.get(key));
    }

    @Test
    public void testClear() {
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove_NormalCase() {
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertEquals(value, decorator.remove(key));
        assertNull(decorator.get(key));
    }

    @Test
    public void testRemove_NullKey() {
        Short key = null;
        Byte value = 2;
        decorator.put(key, value);

        assertEquals(value, decorator.remove(key));
        assertNull(decorator.get(key));
    }

    @Test
    public void testRemove_NotFoundKey() {
        Short key = 1;
        assertNull(decorator.remove(key));
    }
    
    @Test
    public void testEntrySet_Size() {
        Short key1 = 1;
        Byte value1 = 2;
        decorator.put(key1, value1);

        Short key2 = 3;
        Byte value2 = 4;
        decorator.put(key2, value2);

        assertEquals(2, decorator.entrySet().size());
    }

    @Test
    public void testEntrySet_IsEmpty() {
        assertTrue(decorator.entrySet().isEmpty());
    }

    @Test
    public void testEntrySet_Contains() {
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertTrue(decorator.entrySet().contains(new HashMap.SimpleEntry<>(key, value)));
    }

    @Test
    public void testEntrySet_IteratorNext() {
        Short key1 = 1;
        Byte value1 = 2;
        decorator.put(key1, value1);

        Short key2 = 3;
        Byte value2 = 4;
        decorator.put(key2, value2);

        Map.Entry<Short, Byte> entry = decorator.entrySet().iterator().next();

        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());
    }

    @Test
    public void testEntrySet_IteratorHasNext() {
        assertFalse(decorator.entrySet().iterator().hasNext());
    }

    @Test
    public void testEntrySet_IteratorRemove() {
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        decorator.entrySet().iterator().remove();

        assertNull(decorator.get(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_Add() {
        decorator.entrySet().add(new HashMap.SimpleEntry<>(1, 2));
    }

    @Test
    public void testEntrySet_Remove() {
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertTrue(decorator.entrySet().remove(new HashMap.SimpleEntry<>(key, value)));
        assertNull(decorator.get(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_AddAll() {
        decorator.entrySet().addAll(new HashMap.SimpleEntry<>(1, 2));
    }

    @Test
    public void testEntrySet_Clear() {
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testContainsValue_NormalCase() {
        Byte value = 2;
        decorator.put((short) 1, value);

        assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testContainsValue_NotFoundValue() {
        assertFalse(decorator.containsValue((byte) 2));
    }

    @Test
    public void testContainsKey_NormalCase() {
        Short key = 1;
        decorator.put(key, (byte) 2);

        assertTrue(decorator.containsKey(key));
    }

    @Test
    public void testContainsKey_NullKey() {
        assertTrue(decorator.containsKey(null));
    }

    @Test
    public void testContainsKey_NotFoundKey() {
        assertFalse(decorator.containsKey((short) 1));
    }

    @Test
    public void testSize() {
        assertEquals(0, decorator.size());

        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());

        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 2);
        map.put((short) 3, (byte) 4);

        decorator.putAll(map);

        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 3));
    }

    @Test
    public void testWrapKey() {
        short k = 1;
        Short key = decorator.wrapKey(k);
        assertEquals(k, key.shortValue());
    }

    @Test
    public void testUnwrapKey() {
        Short key = 1;
        short k = decorator.unwrapKey(key);
        assertEquals(key.shortValue(), k);
    }

    @Test
    public void testWrapValue() {
        byte v = 2;
        Byte value = decorator.wrapValue(v);
        assertEquals(v, value.byteValue());
    }

    @Test
    public void testUnwrapValue() {
        Byte value = 2;
        byte v = decorator.unwrapValue(value);
        assertEquals(value.byteValue(), v);
    }

    @Test
    public void testReadExternal() throws Exception {
        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
        decorator2.readExternal(new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(new byte[] {0})));
        assertNotNull(decorator2.getMap());
    }

    @Test
    public void testWriteExternal() throws Exception {
        decorator.writeExternal(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()));
        assertNotNull(decorator.getMap());
    }
}