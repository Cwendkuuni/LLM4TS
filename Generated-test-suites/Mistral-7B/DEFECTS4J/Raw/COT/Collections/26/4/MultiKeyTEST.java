import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1"; // replace with actual immutable type and value
        K key2 = "key2"; // replace with actual immutable type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;

        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
        assertEquals(2, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        MultiKey<?> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        K[] expectedKeys = new K[5];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        expectedKeys[3] = key4;
        expectedKeys[4] = key5;

        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testMultiKeyClone() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2});

        MultiKey<K> multiKeyClone = multiKey.clone();
        K[] actualKeys = multiKeyClone.getKeys();
        K[] expectedKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, new K("key3"));

        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey1.equals(multiKey1));
        assertFalse(multiKey1.equals(multiKey3));
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
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        multiKey.getKey(2);
    }

}