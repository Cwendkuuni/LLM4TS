package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String[] expectedKeys = new String[]{"key1", "key2"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String[] expectedKeys = new String[]{"key1", "key2", "key3"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        String[] expectedKeys = new String[]{"key1", "key2", "key3", "key4"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        String[] expectedKeys = new String[]{"key1", "key2", "key3", "key4", "key5"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorArrayKeys() {
        String[] keys = new String[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorArrayKeysMakeCloneFalse() {
        String[] keys = new String[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        multiKey.getKey(3);
    }

    @Test
    public void testSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEqualsSameObject() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertTrue(multiKey.equals(multiKey));
    }

    @Test
    public void testEqualsSameValues() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEqualsDifferentValues() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key4", "key5", "key6");
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEqualsDifferentType() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertFalse(multiKey.equals("key1,key2,key3"));
    }

    @Test
    public void testHashCodeSameValues() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testHashCodeDifferentValues() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key4", "key5", "key6");
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}