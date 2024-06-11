import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // Test construction with two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        K[] keys1 = multiKey1.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2}, keys1);

        // Test construction with three keys
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        K[] keys2 = multiKey2.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2, key3}, keys2);

        // Test construction with four keys
        K key4 = "key4";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);
        K[] keys3 = multiKey3.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2, key3, key4}, keys3);

        // Test construction with five keys
        K key5 = "key5";
        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] keys4 = multiKey4.getKeys();
        Assert.assertArrayEquals(new K[] {key1, key2, key3, key4, key5}, keys4);

        // Test construction with an array of keys
        K[] keys5 = new K[] {"key6", "key7"};
        MultiKey<K> multiKey5 = new MultiKey<>(keys5);
        K[] keys5Clone = multiKey5.getKeys();
        Assert.assertArrayEquals(keys5, keys5Clone);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));

        try {
            multiKey.getKey(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            multiKey.getKey(2);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);

        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        assertEquals(multiKey1, multiKey1);
        assertEquals(multiKey1, multiKey2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());

        assertNotEquals(multiKey1, multiKey3);
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        String multiKeyString = multiKey.toString();

        assertTrue(multiKeyString.contains("MultiKey"));
        assertTrue(multiKeyString.contains("[key1, key2]"));
    }

    @Test
    public void testMultiKeyClone() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        MultiKey<K> clonedMultiKey = multiKey.clone();

        assertEquals(multiKey, clonedMultiKey);
        assertNotSame(multiKey, clonedMultiKey);
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        int hashCode = multiKey.hashCode();

        MultiKey<K> multiKeyClone = multiKey.clone();

        assertEquals(hashCode, multiKeyClone.hashCode());
    }
}