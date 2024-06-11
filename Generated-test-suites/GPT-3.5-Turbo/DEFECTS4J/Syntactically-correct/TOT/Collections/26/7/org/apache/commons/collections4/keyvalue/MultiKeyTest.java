import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Assert;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        // Test typical use case
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));

        // Test edge case with null keys
        multiKey = new MultiKey<>(null, null);
        Assert.assertNull(multiKey.getKey(0));
        Assert.assertNull(multiKey.getKey(1));

        // Test error scenario with different key types
        try {
            multiKey = new MultiKey<>("key1", 1);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithThreeKeys() {
        // Test typical use case
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Assert.assertEquals(1, (int) multiKey.getKey(0));
        Assert.assertEquals(2, (int) multiKey.getKey(1));
        Assert.assertEquals(3, (int) multiKey.getKey(2));

        // Test edge case with null keys
        multiKey = new MultiKey<>(null, null, null);
        Assert.assertNull(multiKey.getKey(0));
        Assert.assertNull(multiKey.getKey(1));
        Assert.assertNull(multiKey.getKey(2));

        // Test error scenario with different key types
        try {
            multiKey = new MultiKey<>(1, 2, "key");
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithFourKeys() {
        // Test typical use case
        MultiKey<Character> multiKey = new MultiKey<>('a', 'b', 'c', 'd');
        Assert.assertEquals('a', (char) multiKey.getKey(0));
        Assert.assertEquals('b', (char) multiKey.getKey(1));
        Assert.assertEquals('c', (char) multiKey.getKey(2));
        Assert.assertEquals('d', (char) multiKey.getKey(3));

        // Test edge case with null keys
        multiKey = new MultiKey<>(null, null, null, null);
        Assert.assertNull(multiKey.getKey(0));
        Assert.assertNull(multiKey.getKey(1));
        Assert.assertNull(multiKey.getKey(2));
        Assert.assertNull(multiKey.getKey(3));

        // Test error scenario with different key types
        try {
            multiKey = new MultiKey<>("key1", 'a', 'b', 'c');
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithFiveKeys() {
        // Test typical use case
        MultiKey<Boolean> multiKey = new MultiKey<>(true, false, true, false, true);
        Assert.assertEquals(true, (boolean) multiKey.getKey(0));
        Assert.assertEquals(false, (boolean) multiKey.getKey(1));
        Assert.assertEquals(true, (boolean) multiKey.getKey(2));
        Assert.assertEquals(false, (boolean) multiKey.getKey(3));
        Assert.assertEquals(true, (boolean) multiKey.getKey(4));

        // Test edge case with null keys
        multiKey = new MultiKey<>(null, null, null, null, null);
        Assert.assertNull(multiKey.getKey(0));
        Assert.assertNull(multiKey.getKey(1));
        Assert.assertNull(multiKey.getKey(2));
        Assert.assertNull(multiKey.getKey(3));
        Assert.assertNull(multiKey.getKey(4));

        // Test error scenario with different key types
        try {
            multiKey = new MultiKey<>("key1", true, false, true, false);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithArray() {
        // Test typical use case
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        Assert.assertArrayEquals(keys, multiKey.getKeys());

        // Test edge case with empty array
        keys = new String[]{};
        multiKey = new MultiKey<>(keys);
        Assert.assertArrayEquals(keys, multiKey.getKeys());

        // Test error scenario with null array
        try {
            multiKey = new MultiKey<>(null);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test error scenario with different key types
        try {
            keys = new String[]{"key1", "key2", "key3", "4"};
            multiKey = new MultiKey<>(keys);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key2", "key1");

        // Test typical use case
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertTrue(multiKey2.equals(multiKey1));
        Assert.assertFalse(multiKey1.equals(multiKey3));
        Assert.assertFalse(multiKey3.equals(multiKey1));

        // Test edge cases with null keys
        multiKey1 = new MultiKey<>(null, null);
        multiKey2 = new MultiKey<>(null, null);
        multiKey3 = new MultiKey<>("key1", "key2");
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertTrue(multiKey2.equals(multiKey1));
        Assert.assertFalse(multiKey1.equals(multiKey3));
        Assert.assertFalse(multiKey3.equals(multiKey1));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key2", "key1");

        // Test typical use case
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());

        // Test edge cases with null keys
        multiKey1 = new MultiKey<>(null, null);
        multiKey2 = new MultiKey<>(null, null);
        multiKey3 = new MultiKey<>("key1", "key2");
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Assert.assertEquals("MultiKey[1, 2, 3]", multiKey.toString());

        // Test edge case with null keys
        multiKey = new MultiKey<>(null, null, null);
        Assert.assertEquals("MultiKey[null, null, null]", multiKey.toString());
    }

}