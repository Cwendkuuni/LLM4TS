import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testTwoKeyConstructor() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testThreeKeyConstructor() {
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

    @Test
    public void testFourKeyConstructor() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[4];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        expectedKeys[3] = key4;
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyArrayConstructor() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testCloneKeyArrayConstructor() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new Object[]{key1, key2}.clone());
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedSize = 2;
        int actualSize = multiKey.size();
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyNegativeIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyGreaterThanSizeIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(multiKey.size() + 1);
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key3");
        Object other = new Object();
        Assert.assertTrue(multiKey1.equals(multiKey1));
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(other));
        Assert.assertFalse(multiKey1.equals(null));
        Assert.assertFalse(multiKey1.equals(""));
        Assert.assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = Arrays.hashCode(new Object[]{key1, key2});
        int actualHashCode = multiKey.hashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedToString = "MultiKey[ MultiKey[key1, key2]]";
        String actualToString = multiKey.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }
}