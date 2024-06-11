import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testTwoKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[]{key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testThreeKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[]{key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testFourKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[]{key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testFiveKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] expectedKeys = new K[]{key1, key2, key3, key4, key5};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testArrayConstruction() {
        K[] keys = new K[]{"key1", "key2"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        K[] expectedKeys = keys.clone();
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArrayConstruction() {
        MultiKey<K> multiKey = new MultiKey<>(null);
        fail("Expected IllegalArgumentException but it did not occur");
    }

    @Test
    public void testSize() {
        K[] keys = new K[]{"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        int size = multiKey.size();
        assertEquals(3, size);
    }

    @Test
    public void testGetKey() {
        K[] keys = new K[]{"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        K key1 = multiKey.getKey(0);
        K key2 = multiKey.getKey(1);
        K key3 = multiKey.getKey(2);
        assertEquals(keys[0], key1);
        assertEquals(keys[1], key2);
        assertEquals(keys[2], key3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyOutOfBounds() {
        K[] keys = new K[]{"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        multiKey.getKey(3);
        fail("Expected IndexOutOfBoundsException but it did not occur");
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2");
        MultiKey<K> multiKey5 = new MultiKey<>(new K[]{"key1", "key2"});
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
        assertFalse(multiKey1.equals(multiKey5));
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int hashCode = multiKey.hashCode();
        K key1HashCode = key1.hashCode();
        K key2HashCode = key2.hashCode();
        int calculatedHashCode = (key1HashCode ^ key2HashCode);
        assertEquals(calculatedHashCode, hashCode);
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String multiKeyString = multiKey.toString();
        String expectedString = "MultiKey[key1, key2]";
        assertEquals(expectedString, multiKeyString);
    }
}