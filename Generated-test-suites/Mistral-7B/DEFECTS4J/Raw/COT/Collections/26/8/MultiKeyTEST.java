import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        K key3 = (K) new Integer(3);
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[3];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithNullKeysArray() {
        MultiKey<String> multiKey = new MultiKey<>(new K[0]);
    }

    @Test
    public void testMultiKeyWithClonedKeysArray() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2}.clone(), false);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyWithMakeCloneTrue() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2}, true);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2});
        multiKey.getKey(-1);
    }

    @Test
    public void testEqualsWithSameMultiKey() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey1 = new MultiKey<>(new K[]{key1, key2});
        MultiKey<K> multiKey2 = new MultiKey<>(new K[]{key1, key2});
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEqualsWithDifferentNumberOfKeys() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey1 = new MultiKey<>(new K[]{key1, key2});
        MultiKey<K> multiKey2 = new MultiKey<>(new K[]{key1});
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEqualsWithDifferentKeys() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey1 = new MultiKey<>(new K[]{key1, key2});
        K key3 = (K) new Integer(3);
        MultiKey<K> multiKey2 = new MultiKey<>(new K[]{key1, key3});
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        K key1 = (K) new Integer(1);
        K key2 = (K) new Integer(2);
        MultiKey<K> multiKey1 = new MultiKey<>(new K[]{key1, key2});
        MultiKey<K> multiKey2 = new MultiKey<>(new K[]{key2, key1});
        int hashCode1 = multiKey1.hashCode();
        int hashCode2 = multiKey2.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }
}