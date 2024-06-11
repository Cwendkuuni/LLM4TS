import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testGetKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String[] keys = multiKey.getKeys();
        assertArrayEquals(new String[]{"key1", "key2"}, keys);
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArrayConstructor() {
        new MultiKey<>(null);
    }

    @Test
    public void testIndexOutOfBoundsException() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertThrows(IndexOutOfBoundsException.class, () -> multiKey.getKey(2));
    }

}