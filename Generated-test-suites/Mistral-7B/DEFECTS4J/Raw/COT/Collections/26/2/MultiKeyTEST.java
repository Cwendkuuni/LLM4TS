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
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null, "key2");
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[]{key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullArrayOfKeys() {
        MultiKey<String> multiKey = new MultiKey<>(new K[0]);
    }

    @Test
    public void testMultiKeyFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[]{key1, key2, key3, key4};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNegativeSizeArrayOfKeys() {
        MultiKey<String> multiKey = new MultiKey<>(new K[3], false);
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
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyNegativeIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2}, false);
        multiKey.getKey(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyIndexOutOfBounds() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2}, false);
        multiKey.getKey(2);
    }

    @Test
    public void testMultiKeyEqualsSameInstance() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(new K[]{key1, key2}, false);
        MultiKey<K> multiKey2 = multiKey1;
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testMultiKeyEqualsDifferentInstances() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(new K[]{key1, key2}, false);
        MultiKey<K> multiKey2 = new MultiKey<>(new K[]{key2, key1}, false);
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testMultiKeyEqualsNull() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2}, false);
        assertFalse(multiKey.equals(null));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2}, false);
        MultiKey<K> multiKey2 = new MultiKey<>(new K[]{key2, key1}, false);
        int firstHashCode = multiKey.hashCode();
        int secondHashCode = multiKey2.hashCode();
        assertNotSame(firstHashCode, secondHashCode);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2}, false);
        String expected = "MultiKey[key1, key2]";
        String actual = multiKey.toString();
        assertEquals(expected, actual);
    }

}