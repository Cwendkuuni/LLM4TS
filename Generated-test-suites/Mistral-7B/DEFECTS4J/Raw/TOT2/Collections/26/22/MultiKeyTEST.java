import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MultiKeyTest {
    // Test constructor with two keys
    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }

    // Test constructor with three keys
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
    }

    // Test constructor with array of keys
    @Test
    public void testMultiKeyWithArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2, key3});
        K[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
    }

    // Test getKey method
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2); // should throw IndexOutOfBoundsException
    }

    // Test size method
    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    // Test equals method
    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey1, multiKey2);

        K key3 = "key3";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);
        assertNotEquals(multiKey1, multiKey3);

        Object[] keys = new Object[] { key1, key2 };
        MultiKey<K> multiKey4 = new MultiKey<>(keys);
        assertEquals(multiKey1, multiKey4);
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int hashCode = multiKey.hashCode();
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(hashCode, multiKey2.hashCode());

        K key3 = "key3";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);
        assertNotEquals(hashCode, multiKey3.hashCode());
    }

    // Test toString method
    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String toString = multiKey.toString();
        assertTrue(toString.contains("MultiKey[key1, key2]"));
    }
}