import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertArrayEquals(new String[]{key1, key2}, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertArrayEquals(new String[]{key1, key2, key3}, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertArrayEquals(new String[]{key1, key2, key3, key4}, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertArrayEquals(new String[]{key1, key2, key3, key4, key5}, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testGetKey() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        multiKey.getKey(3);
    }

    @Test
    public void testSize() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(keys.length, multiKey.size());
    }

    @Test
    public void testEquals() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key2"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEqualsWithDifferentKeys() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key3"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        String[] keys = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        int expectedHashCode = keys[0].hashCode() ^ keys[1].hashCode();
        assertEquals(expectedHashCode, multiKey.hashCode());
    }

    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
    
    // Add more test methods for all remaining methods in the MultiKey class
    
    // ...
    
}