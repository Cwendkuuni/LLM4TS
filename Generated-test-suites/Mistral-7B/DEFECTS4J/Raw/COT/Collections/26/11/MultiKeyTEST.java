import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;

        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        K[] expectedKeys = new K[3];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;

        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNegativeIndexGetKey() {
        K[] keys = new K[3];
        MultiKey<K> multiKey = new MultiKey<>(keys);
        multiKey.getKeys();

        int index = -1;
        multiKey.getKey(index);
    }

    @Test
    public void testMultiKeySize() {
        K[] keys = new K[5];
        MultiKey<K> multiKey = new MultiKey<>(keys);

        int expectedSize = 5;
        int actualSize = multiKey.size();

        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);

        boolean expectedResult = true;
        boolean actualResult = multiKey1.equals(multiKey2);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMultiKeyNotEqualsDifferentNumberOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1);

        boolean expectedResult = false;
        boolean actualResult = multiKey1.equals(multiKey2);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMultiKeyNotEqualsDifferentKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, "differentKey");

        boolean expectedResult = false;
        boolean actualResult = multiKey1.equals(multiKey2);

        Assert.assertEquals(expectedResult, actualResult);
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

        String expectedString = "MultiKey[keys=[key1, key2]]";
        String actualString = multiKey.toString();

        Assert.assertEquals(expectedString, actualString);
    }
}