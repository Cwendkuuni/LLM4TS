import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1"; // replace with actual key type and value
        K key2 = "key2"; // replace with actual key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        MultiKey<Object> multiKey = new MultiKey<>(null, "key2");
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1"; // replace with actual key type and value
        K key2 = "key2"; // replace with actual key type and value
        K key3 = "key3"; // replace with actual key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[3];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeyArray() {
        MultiKey<Object> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyFourKeys() {
        K key1 = "key1"; // replace with actual key type and value
        K key2 = "key2"; // replace with actual key type and value
        K key3 = "key3"; // replace with actual key type and value
        K key4 = "key4"; // replace with actual key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[4];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        expectedKeys[3] = key4;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1"; // replace with actual key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1);
        int expectedSize = 1;
        int actualSize = multiKey.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1"; // replace with actual key type and value
        K key2 = "key2"; // replace with actual key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K expectedKey = key1;
        K actualKey = multiKey.getKey(0);
        assertEquals(expectedKey, actualKey);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyOutOfBounds() {
        K key1 = "key1"; // replace with actual key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1);
        multiKey.getKey(1);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1"; // replace with actual key type and value
        K key2 = "key2"; // replace with actual key type and value
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "differentKey2");
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1"; // replace with actual key type and value
        K key2 = "key2"; // replace with actual key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = new K[2];
        keys[0] = key1;
        keys[1] = key2;
        int expectedHashCode = key1.hashCode() ^ key2.hashCode();
        int actualHashCode = multiKey.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}