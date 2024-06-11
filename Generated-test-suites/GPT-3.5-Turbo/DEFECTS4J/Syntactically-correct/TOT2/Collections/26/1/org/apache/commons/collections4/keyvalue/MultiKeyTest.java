import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String[] keys = multiKey.getKeys();
        assertEquals(2, multiKey.size());
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String[] keys = multiKey.getKeys();
        assertEquals(3, multiKey.size());
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key2", "key1");
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArrayConstructor() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
}