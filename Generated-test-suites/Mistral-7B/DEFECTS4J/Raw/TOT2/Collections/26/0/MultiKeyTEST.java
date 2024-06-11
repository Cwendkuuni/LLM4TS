import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    // Test for constructor taking two keys
    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }

    // Test for constructor taking three keys
    @Test
    public void testMultiKeyThreeKeys() {
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

    // Test for constructor taking an array of keys
    @Test
    public void testMultiKeyArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }

    // Test for equals method
    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key3");
        assertFalse(multiKey1.equals(multiKey3));
    }

    // Test for hashCode method
    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(hashCode(key1, key2), multiKey.hashCode());
    }

    // Test for size method
    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    // Test for getKey method
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(1);
    }

    // Test for getKey method with valid index
    @Test
    public void testGetKeyValidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    // Edge case test for null keys in constructor
    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null, null);
    }

    // Edge case test for cloning keys in constructor
    @Test
    public void testMultiKeyCloneKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(new String[] {key1, key2, key3}, true);
        String[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertNotEquals(key1, keys[0]); // keys are cloned
        assertNotEquals(key1, keys[1]);
        assertNotEquals(key1, keys[2]);
    }

    private int hashCode(Object... objects) {
        int h = 17;
        for (Object obj : objects) {
            h = 31 * h + (obj == null ? 0 : obj.hashCode());
        }
        return h;
    }
}
