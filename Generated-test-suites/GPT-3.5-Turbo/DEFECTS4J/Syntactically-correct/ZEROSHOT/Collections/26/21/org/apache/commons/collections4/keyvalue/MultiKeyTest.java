import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        // Create MultiKey with two keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        // Check that the keys are correctly set
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testConstructorWithThreeKeys() {
        // Create MultiKey with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");

        // Check that the keys are correctly set
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithFourKeys() {
        // Create MultiKey with four keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");

        // Check that the keys are correctly set
        assertEquals(4, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
    }

    @Test
    public void testConstructorWithFiveKeys() {
        // Create MultiKey with five keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");

        // Check that the keys are correctly set
        assertEquals(5, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
    }

    @Test
    public void testConstructorWithKeysArray() {
        // Create MultiKey with array of keys
        String[] keysArray = new String[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keysArray);

        // Check that the keys are correctly set
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));

        // Modify the array and check that the keys in MultiKey are not affected
        keysArray[0] = "modified";
        assertEquals("key1", multiKey.getKey(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKeysArray() {
        // Try to create MultiKey with null keys
        new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        // Create two MultiKey objects with the same keys
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");

        // Check that the two MultiKey objects are equal
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        // Create two MultiKey objects with the same keys
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");

        // Check that the hash codes of the two MultiKey objects are equal
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        // Create MultiKey with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");

        // Check that the toString method returns the expected string
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }

    @Test
    public void testGetKeys() {
        // Create MultiKey with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");

        // Get the keys and check that they are equal to the original keys array
        assertArrayEquals(new String[] { "key1", "key2", "key3" }, multiKey.getKeys());
    }

    @Test
    public void testGetKey() {
        // Create MultiKey with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");

        // Check that the getKey method returns the expected key at each index
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
}