import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testTwoKeyConstructor() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }

    @Test
    public void testThreeKeyConstructor() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
    }

    @Test
    public void testArrayConstructor() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2});
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyArrayConstructor() {
        new MultiKey<>(null);
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
    public void testGetKeyOutOfBounds() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
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
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "differentKey2");
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2");
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals("not a MultiKey"));
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey.hashCode(), multiKey2.hashCode());
        K key3 = "key3";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3);
        assertNotEquals(multiKey.hashCode(), multiKey3.hashCode());
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2");
        assertNotEquals(multiKey.hashCode(), multiKey4.hashCode());
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}