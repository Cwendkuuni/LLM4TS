import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertArrayEquals(new K[] {key1, key2}, multiKey.getKeys());
        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // checks hashCode consistency
    }

    @Test
    public void testThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertArrayEquals(new K[] {key1, key2, key3}, multiKey.getKeys());
        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // checks hashCode consistency
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArray() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testCloneArray() {
        K key1 = "key1";
        K key2 = "key2";
        K[] keys = new K[] {key1, key2};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        MultiKey<K> clone = new MultiKey<>(keys.clone(), false);
        assertEquals(multiKey.hashCode(), clone.hashCode());
        assertEquals(multiKey, clone);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyOutOfBounds() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        multiKey.getKey(2);
    }
}