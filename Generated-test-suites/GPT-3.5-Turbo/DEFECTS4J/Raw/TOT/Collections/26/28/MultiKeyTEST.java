import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    private MultiKey<String> multiKey;

    @Before
    public void setUp() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
    }

    @Test
    public void testConstructorOneKey() {
        MultiKey<String> multiKey = new MultiKey<>("testKey");
        assertEquals(1, multiKey.size());
        assertEquals("testKey", multiKey.getKey(0));
    }

    @Test
    public void testConstructorTwoKeys(){
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testConstructorArray() {
        String[] keys = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testGetKeys() {
        String[] keys = {"key1", "key2", "key3"};
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test
    public void testGetKey() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        multiKey.getKey(3);
    }

    @Test
    public void testSize() {
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2", "key4");
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "MultiKey[key1, key2, key3]";
        assertEquals(expected, multiKey.toString());
    }
}