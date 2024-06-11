
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        // create MultiKey with two keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        // check if the keys are correctly set
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        // create MultiKey with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");

        // check if the keys are correctly set
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorFourKeys() {
        // create MultiKey with four keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");

        // check if the keys are correctly set
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        // create MultiKey with five keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");

        // check if the keys are correctly set
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructorArrayKeys() {
        // create MultiKey with array of keys
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        // check if the keys are correctly set
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArrayKeysNull() {
        // attempt to create MultiKey with null array of keys
        MultiKey<String> multiKey = new MultiKey<>(null);

        // exception should be thrown
    }

    @Test
    public void testEquals() {
        // create MultiKey with two keys
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");

        // check if the keys are equal
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testNotEquals() {
        // create MultiKey with two keys
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key3");

        // check if the keys are not equal
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        // create MultiKey with two keys
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");

        // check if the hash codes are equal
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        // create MultiKey with two keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        // check if the toString method returns the correct string
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test
    public void testGetKeys() {
        // create MultiKey with two keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        // check if the getKeys method returns the correct array of keys
        String[] expected = {"key1", "key2"};
        assertArrayEquals(expected, multiKey.getKeys());
    }

    @Test
    public void testGetSize() {
        // create MultiKey with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");

        // check if the getSize method returns the correct size
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testGetKey() {
        // create MultiKey with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");

        // check if the getKey method returns the correct key
        assertEquals("key1", multiKey.getKey(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        // create MultiKey with two keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        // attempt to get a key with an invalid index
        String key = multiKey.getKey(2);

        // exception should be thrown
    }

    @Test
    public void testMergeMultiKey() {
        // create MultiKey with two keys
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key3", "key4");

        // merge the two MultiKeys
        Map<MultiKey<String>, String> map = new HashMap<>();
        map.put(multiKey1, "value1");
        map.put(multiKey2, "value2");

        // check if the values can be retrieved correctly
        assertEquals("value1", map.get(multiKey1));
        assertEquals("value2", map.get(multiKey2));
    }
}
