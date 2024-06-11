import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[]{key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        MultiKey<Object> multiKey = new MultiKey<>(null, null);
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[]{key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        MultiKey<Object> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[]{key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] expectedKeys = new K[]{key1, key2, key3, key4, key5};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyCloneKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2});
        K[] expectedKeys = new K[]{key1.clone(), key2.clone()};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertFalse(multiKey1.equals(null));
        MultiKey<Object> multiKey3 = new MultiKey<>(key1, key2);
        assertFalse(multiKey1.equals(multiKey3));
        MultiKey<K> multiKey4 = new MultiKey<>(new K[]{key1, key2.clone()});
        assertTrue(multiKey1.equals(multiKey4));
        MultiKey<K> multiKey5 = new MultiKey<>(new K[]{key1.clone(), key2.clone()});
        assertTrue(multiKey1.equals(multiKey5));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = key1.hashCode() ^ key2.hashCode();
        int actualHashCode = multiKey.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedString = "MultiKey[MultiKey[key1, key2]]";
        String actualString = multiKey.toString();
        assertEquals(expectedString, actualString);
    }
}