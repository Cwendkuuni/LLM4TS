import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeyTests; // Assuming there is a utility class named MultiKeyTests for test data and assertions
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    // Basic test cases for each constructor

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

    @Test
    public void testMultiKeyFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] keys = multiKey.getKeys();
        assertEquals(4, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
    }

    @Test
    public void testMultiKeyArrayOfKeys() {
        K[] keys = new K[]{"key1", "key2"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        K[] multiKeyKeys = multiKey.getKeys();
        assertEquals(2, multiKeyKeys.length);
        assertArrayEquals(keys, multiKeyKeys);
    }

    // Edge cases and exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyEmptyArray() {
        MultiKey<K> multiKey = new MultiKey<>(new K[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeyInArray() {
        K[] keys = new K[]{"key1", null};
        MultiKey<K> multiKey = new MultiKey<>(keys);
    }

    @Test
    public void testMultiKeyCloneKeys() {
        K key1 = new ImmutableKey("key1"); // Assuming an ImmutableKey class
        K key2 = new ImmutableKey("key2"); // Assuming an ImmutableKey class
        MultiKey<ImmutableKey> multiKey = new MultiKey<>(key1, key2);
        MultiKey<ImmutableKey> multiKeyCopy = new MultiKey<>(multiKey.getKeys().clone());
        assertEquals(multiKey, multiKeyCopy);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyNegativeIndex() {
        K[] keys = new K[]{"key1", "key2"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        multiKey.getKey(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyOutOfBoundsIndex() {
        K[] keys = new K[]{"key1", "key2"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        multiKey.getKey(keys.length);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key2, key1);
        MultiKey<K> multiKey4 = new MultiKey<>(new K[]{"key1", "key2"});
        MultiKey<K> multiKey5 = new MultiKey<>(new K[]{"key2", "key1"});
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
        assertFalse(multiKey1.equals(multiKey5));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key2, key1);
        MultiKey<K> multiKey4 = new MultiKey<>(new K[]{"key1", "key2"});
        MultiKey<K> multiKey5 = new MultiKey<>(new K[]{"key2", "key1"});
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey4.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey5.hashCode());
    }
}