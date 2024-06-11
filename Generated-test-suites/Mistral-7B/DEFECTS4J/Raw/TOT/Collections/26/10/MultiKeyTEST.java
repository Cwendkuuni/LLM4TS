import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(multiKey.hashCode(), multiKey.hashCode());
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey, multiKey2);
        MultiKey<K> multiKey3 = new MultiKey<>(key2, key1);
        assertNotEquals(multiKey, multiKey3);

        // Test get method
        K value = "value";
        Map<MultiKey<K>, Object> map = new HashMap<>();
        map.put(multiKey, value);
        assertEquals(value, map.get(multiKey));
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(multiKey.hashCode(), multiKey.hashCode());
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        assertEquals(multiKey, multiKey2);
        MultiKey<K> multiKey3 = new MultiKey<>(key2, key3, key1);
        assertNotEquals(multiKey, multiKey3);

        // Test get method
        K value = "value";
        Map<MultiKey<K>, Object> map = new HashMap<>();
        map.put(multiKey, value);
        assertEquals(value, map.get(multiKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null, "key2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithNullArrayOfKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyWithFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] keys = multiKey.getKeys();
        assertEquals(5, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
        assertEquals(key5, keys[4]);
        assertEquals(multiKey.hashCode(), multiKey.hashCode());
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(multiKey, multiKey2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key5, key4);
        assertNotEquals(multiKey, multiKey3);

        // Test get method
        K value = "value";
        Map<MultiKey<K>, Object> map = new HashMap<>();
        map.put(multiKey, value);
        assertEquals(value, map.get(multiKey));
    }
}