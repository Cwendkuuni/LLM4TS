import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        // Typical use case
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        assertNotNull(multiKey);
        
        // Edge case
        MultiKey<String> emptyKey = new MultiKey<>(null, null);
        assertNotNull(emptyKey);
    }

    @Test
    public void testConstructorWithThreeKeys() {
        // Typical use case
        MultiKey<String> multiKey = new MultiKey<>("a", "b", "c");
        assertNotNull(multiKey);
        
        // Edge case
        MultiKey<Integer> emptyKey = new MultiKey<>(null, null, null);
        assertNotNull(emptyKey);
    }

    @Test
    public void testConstructorWithFourKeys() {
        // Typical use case
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
        assertNotNull(multiKey);
        
        // Edge case
        MultiKey<String> emptyKey = new MultiKey<>(null, null, null, null);
        assertNotNull(emptyKey);
    }

    @Test
    public void testConstructorWithFiveKeys() {
        // Typical use case
        MultiKey<String> multiKey = new MultiKey<>("a", "b", "c", "d", "e");
        assertNotNull(multiKey);
        
        // Edge case
        MultiKey<Integer> emptyKey = new MultiKey<>(null, null, null, null, null);
        assertNotNull(emptyKey);
    }

    @Test
    public void testConstructorWithArray() {
        // Typical use case
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertNotNull(multiKey);
        
        // Edge case
        MultiKey<Integer> emptyKey = new MultiKey<>(new Integer[0]);
        assertNotNull(emptyKey);
    }

    @Test
    public void testGetKeys() {
        // Typical use case
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Integer[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals(2, (int) keys[1]);
        
        // Edge case
        MultiKey<Integer> emptyKey = new MultiKey<>(null, null);
        Integer[] emptyKeys = emptyKey.getKeys();
        assertEquals(2, emptyKeys.length);
        assertNull(emptyKeys[0]);
    }

    @Test
    public void testGetKey() {
        // Typical use case
        MultiKey<String> multiKey = new MultiKey<>("a", "b", "c");
        assertEquals("b", multiKey.getKey(1));
        
        // Edge case
        MultiKey<String> emptyKey = new MultiKey<>(null, null);
        assertNull(emptyKey.getKey(1));
    }

    @Test
    public void testSize() {
        // Typical use case
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(3, multiKey.size());
        
        // Edge case
        MultiKey<String> emptyKey = new MultiKey<>(null, null, null, null);
        assertEquals(4, emptyKey.size());
    }

    @Test
    public void testEquals() {
        // Typical use case
        MultiKey<Integer> key1 = new MultiKey<>(1, 2);
        MultiKey<Integer> key2 = new MultiKey<>(1, 2);
        assertTrue(key1.equals(key2));
        
        // Edge case
        MultiKey<String> emptyKey1 = new MultiKey<>(null, null);
        MultiKey<String> emptyKey2 = new MultiKey<>(null, null);
        assertTrue(emptyKey1.equals(emptyKey2));
    }

    @Test
    public void testHashCode() {
        // Typical use case
        MultiKey<Integer> key1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> key2 = new MultiKey<>(1, 2, 3);
        assertEquals(key1.hashCode(), key2.hashCode());
        
        // Edge case
        MultiKey<String> emptyKey1 = new MultiKey<>(null, null);
        MultiKey<String> emptyKey2 = new MultiKey<>(null, null);
        assertEquals(emptyKey1.hashCode(), emptyKey2.hashCode());
    }

    @Test
    public void testToString() {
        // Typical use case
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        assertEquals("MultiKey[1, 2]", multiKey.toString());
        
        // Edge case
        MultiKey<String> emptyKey = new MultiKey<>(null, null);
        assertEquals("MultiKey[null, null]", emptyKey.toString());
    }

}