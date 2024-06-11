import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        // Testing constructors with different number of keys
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);
        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKey5 = new MultiKey<>(new K[] {key1, key2, key3, key4, key5});

        // Testing getters
        K[] keys1 = multiKey1.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2}, keys1);
        int size1 = multiKey1.size();
        Assert.assertEquals(2, size1);

        K[] keys2 = multiKey2.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2, key3}, keys2);
        int size2 = multiKey2.size();
        Assert.assertEquals(3, size2);

        K[] keys3 = multiKey3.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2, key3, key4}, keys3);
        int size3 = multiKey3.size();
        Assert.assertEquals(4, size3);

        K[] keys4 = multiKey4.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2, key3, key4, key5}, keys4);
        int size4 = multiKey4.size();
        Assert.assertEquals(5, size4);

        K[] keys5 = multiKey5.getKeys();
        Assert.assertArrayEquals(keys5, multiKey4.getKeys());
        int size5 = multiKey5.size();
        Assert.assertEquals(5, size5);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));

        MultiKey<String> multiKeyString1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKeyString2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKeyString3 = new MultiKey<>("key1", "key3");

        Assert.assertTrue(multiKeyString1.equals(multiKeyString2));
        Assert.assertFalse(multiKeyString1.equals(multiKeyString3));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());

        MultiKey<String> multiKeyString1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKeyString2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKeyString3 = new MultiKey<>("key1", "key3");

        Assert.assertEquals(multiKeyString1.hashCode(), multiKeyString2.hashCode());
        Assert.assertNotEquals(multiKeyString1.hashCode(), multiKeyString3.hashCode());
    }

    @Test
    public void testMultiKeyClone() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();

        MultiKey<K> clonedMultiKey = new MultiKey<>(keys.clone());

        Assert.assertNotSame(multiKey, clonedMultiKey);
        Assert.assertArrayEquals(keys, clonedMultiKey.getKeys());
    }
}