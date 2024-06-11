import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    // Test constructor with two keys
    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = (K[]) new Object[]{key1, key2};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    // Test constructor with three keys
    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = (K[]) new Object[]{key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    // Test constructor with four keys
    @Test
    public void testMultiKeyFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = (K[]) new Object[]{key1, key2, key3, key4};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    // Test constructor with five keys
    @Test
    public void testMultiKeyFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] expectedKeys = (K[]) new Object[]{key1, key2, key3, key4, key5};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    // Test constructor with an array of keys
    @Test
    public void testMultiKeyArrayOfKeys() {
        K[] keys = (K[]) new Object[]{"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        K[] expectedKeys = keys.clone();
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    // Test getKey method
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        K[] keys = (K[]) new Object[]{"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        multiKey.getKey(-1);
    }

    // Test size method
    @Test
    public void testSize() {
        K[] keys = (K[]) new Object[]{"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        int expectedSize = 3;
        int actualSize = multiKey.size();
        assertEquals(expectedSize, actualSize);
    }

    // Test equals method
    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, new String("key2"));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = 32 * (32 + key1.hashCode()) + key2.hashCode();
        int actualHashCode = multiKey.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    // Test toString method
    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedToString = "MultiKey[keys=[key1, key2]]";
        String actualToString = multiKey.toString();
        assertEquals(expectedToString, actualToString);
    }

}