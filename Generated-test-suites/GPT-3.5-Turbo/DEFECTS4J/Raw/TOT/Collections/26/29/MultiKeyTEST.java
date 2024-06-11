import org.junit.*;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructor_withTwoImmutableKeys() {
        String key1 = "Key1";
        String key2 = "Key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
    }

    @Test
    public void testConstructor_withThreeImmutableKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructor_withEmptyKeys() {
        String[] keys = new String[]{};
        
        try {
            MultiKey<String> multiKey = new MultiKey<>(keys);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The array of keys must not be null", e.getMessage());
        }
    }

    @Test
    public void testEquals_withEqualKeys() {
        String key1 = "Test";
        String key2 = "Test";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEquals_withDifferentKeys() {
        String key1 = "Test";
        String key2 = "Different";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key1);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode_withEqualKeys() {
        String key1 = "Test";
        String key2 = "Test";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString_withKeys() {
        String key1 = "Test";
        String key2 = "String";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals("MultiKey[Test, String]", multiKey.toString());
    }
}