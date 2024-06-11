package org.apache.commons.collections4.keyvalue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    private MultiKey<String> multiKey;

    @Before
    public void setup() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
    }

    @Test
    public void testGetKeys() {
        String[] keys = multiKey.getKeys();
        assertArrayEquals(new String[]{"key1", "key2", "key3"}, keys);
    }

    @Test
    public void testGetKey() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        multiKey.getKey(3);
    }

    @Test
    public void testSize() {
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        MultiKey<String> sameMultiKey = new MultiKey<>("key1", "key2", "key3");
        assertTrue(multiKey.equals(sameMultiKey));

        MultiKey<String> diffMultiKey = new MultiKey<>("key1", "key2", "diffKey");
        assertFalse(multiKey.equals(diffMultiKey));

        assertFalse(multiKey.equals(null));
        assertFalse(multiKey.equals("key"));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> sameMultiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(multiKey.hashCode(), sameMultiKey.hashCode());

        MultiKey<String> diffMultiKey = new MultiKey<>("key1", "key2", "diffKey");
        assertNotEquals(multiKey.hashCode(), diffMultiKey.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}