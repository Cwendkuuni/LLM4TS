import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[3];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testMultiKeyFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[4];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        expectedKeys[3] = key4;
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
        assertEquals(4, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeyArray() {
        MultiKey<?> multiKey = new MultiKey<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKey() {
        K key = null;
        MultiKey<K> multiKey = new MultiKey<>(key, "key2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyChangedKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        key1 = "newKey1";
        assertFalse(multiKey.equals(new MultiKey<>(key1, key2)));
    }

    @Test
    public void testMultiKeyClone() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> clonedMultiKey = new MultiKey<>(multiKey.getKeys().clone());
        assertEquals(multiKey, clonedMultiKey);
    }
}