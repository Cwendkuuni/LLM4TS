import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator decorator;

    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        decorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        Double key = 3.14;
        Short value = 42;

        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testClear() {
        Double key = 3.14;
        Short value = 42;

        decorator.put(key, value);
        decorator.clear();

        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(value));
        assertNull(decorator.get(key));
    }

    @Test
    public void testRemove() {
        Double key = 3.14;
        Short value = 42;

        decorator.put(key, value);
        Short removedValue = decorator.remove(key);

        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(value));
        assertNull(decorator.get(key));
        assertEquals(value, removedValue);
    }

    @Test
    public void testEntrySet() {
        Double key1 = 3.14;
        Short value1 = 42;
        Double key2 = 2.718;
        Short value2 = 24;

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();

        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new Entry<>(key1, value1)));
        assertTrue(entrySet.contains(new Entry<>(key2, value2)));
    }

    @Test
    public void testSize() {
        Double key1 = 3.14;
        Short value1 = 42;
        Double key2 = 2.718;
        Short value2 = 24;

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());

        Double key = 3.14;
        Short value = 42;

        decorator.put(key, value);

        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Double key1 = 3.14;
        Short value1 = 42;
        Double key2 = 2.718;
        Short value2 = 24;

        TDoubleShortMapDecorator otherDecorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
        otherDecorator.put(key1, value1);
        otherDecorator.put(key2, value2);

        decorator.putAll(otherDecorator);

        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(key1));
        assertTrue(decorator.containsKey(key2));
    }

    @Test
    public void testWrapAndUnwrapKeyAndValue() {
        double key = 3.14;
        short value = 42;

        Double wrappedKey = decorator.wrapKey(key);
        double unwrappedKey = decorator.unwrapKey(wrappedKey);
        Short wrappedValue = decorator.wrapValue(value);
        short unwrappedValue = decorator.unwrapValue(wrappedValue);

        assertEquals(key, unwrappedKey, 0.0001);
        assertEquals(value, unwrappedValue);
    }

    private static class Entry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
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
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry<?, ?> entry = (Entry<?, ?>) obj;
            return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}