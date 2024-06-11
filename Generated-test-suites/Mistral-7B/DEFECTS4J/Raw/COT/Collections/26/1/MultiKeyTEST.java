import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1"; // replace with actual immutable key implementation
        K key2 = "key2"; // replace with actual immutable key implementation
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;

        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
        Assert.assertEquals(2, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeyArray() {
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
        Assert.assertEquals(3, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNegativeIndex() {
        MultiKey<Integer> multiKey = new MultiKey<>(new Integer[5]);
        multiKey.getKey(-1);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K key1Retrieved = multiKey.getKey(0);
        K key2Retrieved = multiKey.getKey(1);

        Assert.assertEquals(key1, key1Retrieved);
        Assert.assertEquals(key2, key2Retrieved);
    }
}