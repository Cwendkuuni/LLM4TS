import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(1, multiKey.getKey(0).intValue());
        assertEquals(2, multiKey.getKey(1).intValue());
        assertEquals(3, multiKey.getKey(2).intValue());
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<Character> multiKey = new MultiKey<>('a', 'b', 'c', 'd');
        assertEquals('a', multiKey.getKey(0).charValue());
        assertEquals('b', multiKey.getKey(1).charValue());
        assertEquals('c', multiKey.getKey(2).charValue());
        assertEquals('d', multiKey.getKey(3).charValue());
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<Boolean> multiKey = new MultiKey<>(true, false, true, false, true);
        assertTrue(multiKey.getKey(0));
        assertFalse(multiKey.getKey(1));
        assertTrue(multiKey.getKey(2));
        assertFalse(multiKey.getKey(3));
        assertTrue(multiKey.getKey(4));
    }

    @Test
    public void testConstructorCloneKeys() {
        String[] keys = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        keys[0] = "changed";
        assertNotEquals("changed", multiKey.getKey(0));
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
    public void testNullKeyArrayConstructor() {
        new MultiKey<>(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        multiKey.getKey(3);
    }

    @Test
    public void testSize() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(3, multiKey.size());
    }

}