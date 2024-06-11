import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2}, keys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testGetKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2}, keys);
    }

    @Test
    public void testGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        multiKey.getKey(2);
    }

    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        Assert.assertEquals(2, multiKey.size());
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(new K[] {key1, key2});
        MultiKey<K> multiKey2 = new MultiKey<>(new K[] {key1, key2});
        MultiKey<K> multiKey3 = new MultiKey<>(new K[] {key1, key2, new Object()});
        Assert.assertEquals(multiKey1, multiKey1);
        Assert.assertEquals(multiKey1, multiKey2);
        Assert.assertNotEquals(multiKey1, multiKey3);
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        MultiKey<K> multiKey2 = new MultiKey<>(new K[] {key1, key2});
        Assert.assertEquals(multiKey.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        String expected = "MultiKey[key1, key2]";
        Assert.assertEquals(expected, multiKey.toString());
    }
}