// MultiKeyTest1.java

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiKeyTest1 {

    @Test
    public void testMultiKeyWithTwoKeys() {
        final K key1 = "key1";
        final K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(new Object[]{key1, key2}, multiKey.getKeys());
        assertEquals(2, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithNullKeys() {
        K[] keys = new K[2];
        keys[0] = null;
        try {
            new MultiKey<>(keys);
        } catch (IllegalArgumentException e) {
            assertEquals("The array of keys must not be null", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        final K key1 = "key1";
        final K key2 = "key2";
        final K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(new Object[]{key1, key2, key3}, multiKey.getKeys());
        assertEquals(3, multiKey.size());
    }

    
}