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
        Assert.assertEquals(2, multiKey.size());
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
    public void testMultiKeyNullKeysArray() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyCloneArray() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});

        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;

        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
        Assert.assertEquals(2, multiKey.size());

        K[] clonedKeys = multiKey.getKeys().clone();
        Assert.assertNotSame(actualKeys, clonedKeys);
        Assert.assertArrayEquals(expectedKeys, clonedKeys);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyOutOfBounds() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        multiKey.getKey(2);
    }
}