import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorWithTwoKeys() {
        K key1 = "key1"; // replace with actual immutable key type and value
        K key2 = "key2"; // replace with actual immutable key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorWithNullKeysArray() {
        MultiKey<?> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1"; // replace with actual immutable key type and value
        K key2 = "key2"; // replace with actual immutable key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyWithInvalidIndex() {
        K key1 = "key1"; // replace with actual immutable key type and value
        K key2 = "key2"; // replace with actual immutable key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1"; // replace with actual immutable key type and value
        K key2 = "key2"; // replace with actual immutable key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1"; // replace with actual immutable key type and value
        K key2 = "key2"; // replace with actual immutable key type and value
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, new K() {}); // replace with actual immutable key type
        assertFalse(multiKey1.equals(null));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1"; // replace with actual immutable key type and value
        K key2 = "key2"; // replace with actual immutable key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, new K() {}); // replace with actual immutable key type
        assertEquals(multiKey.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1"; // replace with actual immutable key type and value
        K key2 = "key2"; // replace with actual immutable key type and value
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}