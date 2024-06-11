import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructor_ThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructor_FourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructor_FiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructor_KeyArray() {
        String[] keyArray = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keyArray);
        assertArrayEquals(keyArray, multiKey.getKeys());
    }

    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_InvalidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        multiKey.getKey(3);
    }

    @Test
    public void testEquals_SameObject() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertTrue(multiKey.equals(multiKey));
    }

    @Test
    public void testEquals_EqualKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEquals_DifferentKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key2", "key1");
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertNotNull(multiKey.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertNotNull(multiKey.toString());
    }
}