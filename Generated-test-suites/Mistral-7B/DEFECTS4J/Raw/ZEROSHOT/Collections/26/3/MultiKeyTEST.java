import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // Test construction with two keys
        MultiKey<String> key1_1 = new MultiKey<>("key1", "locale1");
        MultiKey<String> key1_2 = new MultiKey<>("key1", "locale1");
        assertEquals(key1_1, key1_2);

        // Test construction with three keys
        MultiKey<String> key2_1 = new MultiKey<>("key1", "locale1", "locale2");
        MultiKey<String> key2_2 = new MultiKey<>("key1", "locale1", "locale2");
        assertEquals(key2_1, key2_2);

        // Test construction with four keys
        MultiKey<String> key3_1 = new MultiKey<>("key1", "locale1", "locale2", "locale3");
        MultiKey<String> key3_2 = new MultiKey<>("key1", "locale1", "locale2", "locale3");
        assertEquals(key3_1, key3_2);

        // Test construction with five keys
        MultiKey<String> key4_1 = new MultiKey<>("key1", "locale1", "locale2", "locale3", "locale4");
        MultiKey<String> key4_2 = new MultiKey<>("key1", "locale1", "locale2", "locale3", "locale4");
        assertEquals(key4_1, key4_2);

        // Test construction with an array of keys
        MultiKey<String> key5_1 = new MultiKey(new String[]{"key1", "locale1", "locale2"});
        MultiKey<String> key5_2 = new MultiKey(new String[]{"key1", "locale1", "locale2"});
        assertEquals(key5_1, key5_2);
    }

    @Test
    public void testGetKeysAndSize() {
        MultiKey<String> key = new MultiKey<>("key1", "locale1");
        assertArrayEquals(new String[]{"key1", "locale1"}, key.getKeys());
        assertEquals(2, key.size());
    }

    @Test
    public void testGetKey() {
        MultiKey<String> key = new MultiKey<>("key1", "locale1");
        assertEquals("key1", key.getKey(0));
        assertEquals("locale1", key.getKey(1));
    }

    @Test
    public void testEquals() {
        MultiKey<String> key1 = new MultiKey<>("key1", "locale1");
        MultiKey<String> key2 = new MultiKey<>("key1", "locale1");
        MultiKey<String> key3 = new MultiKey<>("key1", "locale2");
        MultiKey<Integer> key4 = new MultiKey<>("key1", 1);

        assertEquals(key1, key1);
        assertEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertNotEquals(key1, key4);
    }

    @Test
    public void testHashCode() {
        MultiKey<String> key1 = new MultiKey<>("key1", "locale1");
        MultiKey<String> key2 = new MultiKey<>("key1", "locale1");
        MultiKey<String> key3 = new MultiKey<>("key1", "locale2");
        MultiKey<Integer> key4 = new MultiKey<>("key1", 1);

        assertEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1.hashCode(), key3.hashCode());
        assertNotEquals(key1.hashCode(), key4.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> key = new MultiKey<>("key1", "locale1");
        assertEquals("MultiKey[key1, locale1]", key.toString());
    }
}