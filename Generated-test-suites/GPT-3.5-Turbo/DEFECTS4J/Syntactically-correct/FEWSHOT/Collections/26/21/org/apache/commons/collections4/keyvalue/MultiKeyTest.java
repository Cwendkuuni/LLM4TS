import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructor() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertNotNull(multiKey);
    }

    @Test
    public void testGetKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String[] keys = multiKey.getKeys();
        assertNotNull(keys);
        assertEquals(2, keys.length);
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
    }

    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_IndexOutOfBounds() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        multiKey.getKey(2);
    }

    @Test
    public void testSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testEquals_SameInstance() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(multiKey, multiKey);
    }

    @Test
    public void testEquals_Null() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertNotEquals(multiKey, null);
    }

    @Test
    public void testEquals_DifferentClass() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertNotEquals(multiKey, "key1-key2");
    }

    @Test
    public void testEquals_EqualKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testEquals_DifferentKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key3");
        assertNotEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode_EqualKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

}