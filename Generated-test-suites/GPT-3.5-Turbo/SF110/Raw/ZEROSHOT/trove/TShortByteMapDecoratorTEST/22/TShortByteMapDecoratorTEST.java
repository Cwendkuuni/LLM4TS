import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        TShortByteMap map = new TShortByteMapImpl();
        decorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPut() {
        Short key = 1;
        Byte value = 2;
        assertNull(decorator.put(key, value));
        assertEquals(value, decorator.put(key, value));
    }

    @Test
    public void testGet() {
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testClear() {
        decorator.put(1, 2);
        decorator.put(3, 4);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);
        assertEquals(value, decorator.remove(key));
    }

    @Test
    public void testEntrySet() {
        decorator.put(1, 2);
        decorator.put(3, 4);
        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new MapEntry<>(1, 2)));
        assertTrue(entrySet.contains(new MapEntry<>(3, 4)));
    }

    @Test
    public void testContainsValue() {
        Byte value = 1;
        decorator.put(1, value);
        assertTrue(decorator.containsValue(value));
        assertFalse(decorator.containsValue((byte) 2));
    }

    @Test
    public void testContainsKey() {
        Short key = 1;
        decorator.put(key, 2);
        assertTrue(decorator.containsKey(key));
        assertFalse(decorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        decorator.put(1, 2);
        decorator.put(3, 4);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put(1, 2);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new TShortByteMapImpl();
        map.put((short) 1, (byte) 2);
        map.put((short) 3, (byte) 4);
        decorator.putAll(map);
        assertEquals(map.size(), decorator.size());
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 3));
    }

    @Test
    public void testWrapKey() {
        short key = 1;
        assertEquals((Short) key, decorator.wrapKey(key));
    }

    @Test
    public void testUnwrapKey() {
        Short key = 1;
        assertEquals((short) key, decorator.unwrapKey(key));
    }

    @Test
    public void testWrapValue() {
        byte value = 2;
        assertEquals((Byte) value, decorator.wrapValue(value));
    }

    @Test
    public void testUnwrapValue() {
        Byte value = 2;
        assertEquals((byte) value, decorator.unwrapValue(value));
    }

    private static class TShortByteMapImpl implements TShortByteMap {

        @Override
        public byte put(short key, byte value) {
            return 0;
        }

        @Override
        public byte get(short key) {
            return 0;
        }

        @Override
        public byte remove(short key) {
            return 0;
        }

        @Override
        public boolean containsKey(short key) {
            return false;
        }

        @Override
        public TShortByteIterator iterator() {
            return null;
        }

        @Override
        public void clear() {

        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public byte getNoEntryValue() {
            return 0;
        }

        @Override
        public short getNoEntryKey() {
            return 0;
        }
    }

    private static class MapEntry<K, V> implements Map.Entry<K, V> {

        private final K key;
        private final V value;

        public MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return this.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MapEntry<?, ?> mapEntry = (MapEntry<?, ?>) o;
            return key.equals(mapEntry.key) && value.equals(mapEntry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}