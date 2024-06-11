import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1"; // replace with actual type and value
        K key2 = "key2"; // replace with actual type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = (K[]) new Object[]{key1, key2};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1"; // replace with actual type and value
        K key2 = "key2"; // replace with actual type and value
        K key3 = "key3"; // replace with actual type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = (K[]) new Object[]{key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyMoreThanFiveKeys() {
        K key1 = "key1"; // replace with actual type and value
        K key2 = "key2"; // replace with actual type and value
        K key3 = "key3"; // replace with actual type and value
        K key4 = "key4"; // replace with actual type and value
        K key5 = "key5"; // replace with actual type and value
        K key6 = "key6"; // replace with actual type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5, key6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKey() {
        K[] keys = (K[]) new Object[]{null};
        MultiKey<String> multiKey = new MultiKey<>(keys);
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1"; // replace with actual type and value
        K key2 = "key2"; // replace with actual type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedSize = 2;
        int actualSize = multiKey.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1"; // replace with actual type and value
        K key2 = "key2"; // replace with actual type and value
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        boolean expectedResult = true;
        boolean actualResult = multiKey1.equals(multiKey2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMultiKeyNotEqualsDifferentSize() {
        K key1 = "key1"; // replace with actual type and value
        K key2 = "key2"; // replace with actual type and value
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1);
        boolean expectedResult = false;
        boolean actualResult = multiKey1.equals(multiKey2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMultiKeyNotEqualsDifferentKeys() {
        K key1 = "key1"; // replace with actual type and value
        K key2 = "key2"; // replace with actual type and value
        K key3 = "key3"; // replace with actual type and value
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key3);
        boolean expectedResult = false;
        boolean actualResult = multiKey1.equals(multiKey2);
        assertEquals(expectedResult, actualResult);
    }
}