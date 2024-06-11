import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);

        K[] keys1 = multiKey1.getKeys();
        K[] keys2 = multiKey2.getKeys();

        Assert.assertArrayEquals(new K[] {key1, key2}, keys1);
        Assert.assertArrayEquals(new K[] {key1, key2, key3}, keys2);
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);

        int size1 = multiKey1.size();
        int size2 = multiKey2.size();

        Assert.assertEquals(2, size1);
        Assert.assertEquals(3, size2);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));

        MultiKey<String> multiKey4 = new MultiKey<>(new String("key1"), new String("key2"));
        Assert.assertTrue(multiKey1.equals(multiKey4));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);

        String toString1 = multiKey1.toString();
        String toString2 = multiKey2.toString();

        Assert.assertEquals("MultiKey[key1, key2]", toString1);
        Assert.assertEquals("MultiKey[key1, key2, key3]", toString2);
    }
}