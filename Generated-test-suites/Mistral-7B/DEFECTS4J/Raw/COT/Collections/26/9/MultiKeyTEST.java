import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[]{key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[]{key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2, key3});
        K[] expectedKeys = new K[]{key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArrayOfKeys() {
        new MultiKey<>(null);
    }

    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }
}