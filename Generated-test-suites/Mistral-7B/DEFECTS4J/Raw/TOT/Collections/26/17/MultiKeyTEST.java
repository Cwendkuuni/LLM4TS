import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // typical use case: add multiKey to a Map
        Map<MultiKey<K>, String> map = new HashMap<>();
        map.put(multiKey, "value");
        assertEquals("value", map.get(multiKey));

        // edge case: multiKey with same keys in different order
        MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);
        assertTrue(multiKey.equals(multiKey2));
        assertEquals("value", map.get(multiKey2));

        // error scenario: null keys
        try {
            MultiKey<K> multiKeyNull = new MultiKey<>(null, key2);
            fail("Should not be able to create MultiKey with null key");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            MultiKey<K> multiKeyNull2 = new MultiKey<>(key1, null);
            fail("Should not be able to create MultiKey with null key");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        // typical use case: add multiKey to a Map
        Map<MultiKey<K>, String> map = new HashMap<>();
        map.put(multiKey, "value");
        assertEquals("value", map.get(multiKey));

        // edge case: multiKey with same keys in different order
        MultiKey<K> multiKey2 = new MultiKey<>(key2, key1, key3);
        assertTrue(multiKey.equals(multiKey2));
        assertEquals("value", map.get(multiKey2));

        // error scenario: null keys
        try {
            MultiKey<K> multiKeyNull = new MultiKey<>(null, key2, key3);
            fail("Should not be able to create MultiKey with null key");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            MultiKey<K> multiKeyNull2 = new MultiKey<>(key1, null, key3);
            fail("Should not be able to create MultiKey with null key");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            MultiKey<K> multiKeyNull3 = new MultiKey<>(key1, key2, null);
            fail("Should not be able to create MultiKey with null key");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithMoreThanFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        K key6 = "key6";

        try {
            new MultiKey<>(key1, key2, key3, key4, key5, key6);
            fail("Should not be able to create MultiKey with more than five keys");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new Object[]{key1, key2}, keys);
    }

    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        int hashCodeMultiKey = multiKey.hashCode();
        int hashCodeMultiKey2 = multiKey2.hashCode();
        int hashCodeMultiKey3 = multiKey3.hashCode();

        assertEquals(hashCodeMultiKey, hashCodeMultiKey2);
        assertNotEquals(hashCodeMultiKey, hashCodeMultiKey3);
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[keys=[key1, key2]]", multiKey.toString());
    }
}