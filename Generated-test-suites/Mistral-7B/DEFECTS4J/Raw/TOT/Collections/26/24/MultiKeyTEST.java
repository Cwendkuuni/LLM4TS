import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        Assert.assertArrayEquals("The keys should be equal.", expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[3];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        Assert.assertArrayEquals("The keys should be equal.", expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        Assert.assertEquals("The first key should be equal.", key1, multiKey.getKey(0));
        Assert.assertEquals("The second key should be equal.", key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }

    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        Assert.assertEquals("The size should be equal.", 2, multiKey.size());
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key3");
        Assert.assertTrue("The two MultiKey instances should be equal.", multiKey1.equals(multiKey2));
        Assert.assertFalse("The MultiKey instances should not be equal.", multiKey1.equals(multiKey3));
        MultiKey<?> multiKey4 = new MultiKey<>(Collections.singletonList(key1), Collections.singletonList(key2));
        Assert.assertFalse("The MultiKey instances should not be equal.", multiKey1.equals(multiKey4));
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = key1.hashCode() ^ key2.hashCode();
        Assert.assertEquals("The hash codes should be equal.", expectedHashCode, multiKey.hashCode());
    }
}