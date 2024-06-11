import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[] {key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        K key3 = (K) new Integer(3);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[] {key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testMultiKeyFourKeys() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        K key3 = (K) new Integer(3);
        K key4 = (K) new Integer(4);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[] {key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(4, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyClone() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> originalMultiKey = new MultiKey<>(key1, key2);
        MultiKey<K> clonedMultiKey = new MultiKey<>(originalMultiKey.getKeys().clone());
        K[] expectedKeys = new K[] {key1, key2};
        assertArrayEquals(expectedKeys, clonedMultiKey.getKeys());
        assertEquals(originalMultiKey.hashCode(), clonedMultiKey.hashCode());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key2, key1);
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testMultiKeyHashcode() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);

        int hashcode1 = multiKey.hashCode();
        int hashcode2 = multiKey2.hashCode();
        int expectedHashcode = key1.hashCode() ^ key2.hashCode();

        assertEquals(expectedHashcode, hashcode1);
        assertEquals(expectedHashcode, hashcode2);
    }

}