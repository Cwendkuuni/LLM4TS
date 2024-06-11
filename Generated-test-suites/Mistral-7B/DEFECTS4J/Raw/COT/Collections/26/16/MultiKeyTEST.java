import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[] {key1, key2};
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        MultiKey<K> multiKey = new MultiKey<>(null, "key2");
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[] {key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        MultiKey<K> multiKey = new MultiKey<>(new K[] {null, "key2"});
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedSize = 2;
        int actualSize = multiKey.size();
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K expectedKey = key1;
        K actualKey = multiKey.getKey(0);
        Assert.assertEquals(expectedKey, actualKey);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(10);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        Object other = new Object();
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(other));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = (key1.hashCode() ^ key2.hashCode());
        int actualHashCode = multiKey.hashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedToString = "MultiKey[key1, key2]";
        String actualToString = multiKey.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }
}