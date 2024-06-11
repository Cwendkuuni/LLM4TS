import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = {key1, key2};
        assertArrayEquals("The keys should be equal", expectedKeys, multiKey.getKeys());
        assertEquals("The size of the MultiKey should be 2", 2, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        K[] keys = new K[0];
        MultiKey<K> multiKey = new MultiKey<>(keys);
    }

    @Test
    public void testGetKey() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K key1FromMultiKey = multiKey.getKey(0);
        assertEquals("The first key should be equal to the original key", key1, key1FromMultiKey);
        K key2FromMultiKey = multiKey.getKey(1);
        assertEquals("The second key should be equal to the original key", key2, key2FromMultiKey);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }

    @Test
    public void testEquals() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(new Integer(1), new Integer(3));
        assertTrue("MultiKey1 and MultiKey2 should be equal", multiKey1.equals(multiKey2));
        assertFalse("MultiKey1 and MultiKey3 should not be equal", multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> multiKeyClone = new MultiKey<>(multiKey.getKeys().clone());
        int hashCodeFromMultiKey = multiKey.hashCode();
        int hashCodeFromClone = multiKeyClone.hashCode();
        assertEquals("The hash codes of MultiKey and the cloned instance should be equal", hashCodeFromMultiKey, hashCodeFromClone);
    }

    @Test
    public void testToString() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedToString = "MultiKey[1, 2]";
        String toStringFromMultiKey = multiKey.toString();
        assertEquals("The toString method should return the expected String", expectedToString, toStringFromMultiKey);
    }
}