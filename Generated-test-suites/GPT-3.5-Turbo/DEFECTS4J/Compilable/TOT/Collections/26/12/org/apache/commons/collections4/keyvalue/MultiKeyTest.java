package org.apache.commons.collections4.keyvalue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    private MultiKey<String> multiKey;

    @Before
    public void setUp() {
        multiKey = new MultiKey<>("key1", "key2");
    }

    @Test
    public void testConstructor_TwoKeys() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructor_ThreeKeys() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructor_FourKeys() {
        multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructor_FiveKeys() {
        multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructor_CloneKeys() {
        String[] keysArray = new String[]{"key1", "key2"};
        multiKey = new MultiKey<>(keysArray, true);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
        keysArray[0] = "modified";
        assertNotEquals("modified", multiKey.getKey(0));
    }

    @Test
    public void testConstructor_NonCloneKeys() {
        String[] keysArray = new String[]{"key1", "key2"};
        multiKey = new MultiKey<>(keysArray, false);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
        keysArray[1] = "modified";
        assertEquals("modified", multiKey.getKey(1));
    }

    @Test
    public void testGetKeys() {
        String[] keysArray = multiKey.getKeys();
        assertArrayEquals(new String[]{"key1", "key2"}, keysArray);
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(multiKey.equals(multiKey));
    }

    @Test
    public void testEquals_SameKeys() {
        MultiKey<String> sameMultiKey = new MultiKey<>("key1", "key2");
        assertTrue(multiKey.equals(sameMultiKey));
    }

    @Test
    public void testEquals_DifferentKeys() {
        MultiKey<String> differentMultiKey = new MultiKey<>("key1", "key3");
        assertFalse(multiKey.equals(differentMultiKey));
    }

    @Test
    public void testEquals_NullObject() {
        assertFalse(multiKey.equals(null));
    }

    @Test
    public void testHashCode_SameKeys() {
        MultiKey<String> sameMultiKey = new MultiKey<>("key1", "key2");
        assertEquals(multiKey.hashCode(), sameMultiKey.hashCode());
    }

    @Test
    public void testHashCode_DifferentKeys() {
        MultiKey<String> differentMultiKey = new MultiKey<>("key1", "key3");
        assertNotEquals(multiKey.hashCode(), differentMultiKey.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}