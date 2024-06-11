import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // Test construction with two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        Assert.assertArrayEquals(new K[] {key1, key2}, multiKey1.getKeys());

        // Test construction with three keys
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        Assert.assertArrayEquals(new K[] {key1, key2, key3}, multiKey2.getKeys());

        // Test construction with four keys
        K key4 = "key4";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);
        Assert.assertArrayEquals(new K[] {key1, key2, key3, key4}, multiKey3.getKeys());

        // Test construction with five keys
        K key5 = "key5";
        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key4, key5);
        Assert.assertArrayEquals(new K[] {key1, key2, key3, key4, key5}, multiKey4.getKeys());

        // Test construction with an array of keys
        K[] keys = new K[] {key1, key2, key3, key4, key5};
        MultiKey<K> multiKey5 = new MultiKey<>(keys);
        Assert.assertArrayEquals(keys, multiKey5.getKeys());
    }

    @Test
    public void testMultiKeyMethods() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));

        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2}, keys);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);

        MultiKey<K> multiKey3 = new MultiKey<>(key1, "differentKey2");
        MultiKey<K> multiKey4 = new MultiKey<>("differentKey1", key2);
        MultiKey<String> multiKey5 = new MultiKey<>("differentKey1", "differentKey2");

        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));
        Assert.assertFalse(multiKey1.equals(multiKey4));
        Assert.assertFalse(multiKey1.equals(multiKey5));
        Assert.assertFalse(multiKey1.equals(null));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        Assert.assertEquals(Arrays.hashCode(new K[] {key1, key2}), multiKey.hashCode());
    }
}