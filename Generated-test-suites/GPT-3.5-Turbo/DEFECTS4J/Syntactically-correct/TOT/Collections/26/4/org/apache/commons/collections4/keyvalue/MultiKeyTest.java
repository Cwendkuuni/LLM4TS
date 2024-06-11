import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        // Typical use case
        MultiKey<String> key = new MultiKey<>("key1", "key2");
        assertNotNull(key);
        
        // Edge case: Empty strings as keys
        key = new MultiKey<>("", "");
        assertNotNull(key);
        
        // Error scenario: null keys
        try {
            key = new MultiKey<>(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Pass
        }
    }

    @Test
    public void testConstructorThreeKeys() {
        // Typical use case
        MultiKey<String> key = new MultiKey<>("key1", "key2", "key3");
        assertNotNull(key);
        
        // Edge case: Empty strings as keys
        key = new MultiKey<>("", "", "");
        assertNotNull(key);
        
        // Error scenario: null keys
        try {
            key = new MultiKey<>(null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Pass
        }
    }

    @Test
    public void testConstructorFourKeys(){
        // Typical use case
        MultiKey<String> key = new MultiKey<>("key1", "key2", "key3", "key4");
        assertNotNull(key);
        
        // Edge case: Empty strings as keys
        key = new MultiKey<>("", "", "", "");
        assertNotNull(key);
        
        // Error scenario: null keys
        try {
            key = new MultiKey<>(null, null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Pass
        }
    }

    @Test
    public void testConstructorFiveKeys(){
        // Typical use case
        MultiKey<String> key = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertNotNull(key);
        
        // Edge case: Empty strings as keys
        key = new MultiKey<>("", "", "", "", "");
        assertNotNull(key);
        
        // Error scenario: null keys
        try {
            key = new MultiKey<>(null, null, null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Pass
        }
    }

    @Test
    public void testConstructorArrayKeys() {
        // Typical use case
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> key = new MultiKey<>(keys);
        assertNotNull(key);
        
        // Error scenario: null keys
        try {
            key = new MultiKey<>(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Pass
        }
    }

    @Test
    public void testGetKeys() {
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> key = new MultiKey<>(keys);
      
        // Typical use case: verify the keys are returned as array
        assertArrayEquals(keys, key.getKeys());
    }

    @Test
    public void testGetKey() {
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> key = new MultiKey<>(keys);
      
        // Typical use case: verify the keys are returned correctly
        assertEquals("key1", key.getKey(0));
        assertEquals("key2", key.getKey(1));
        assertEquals("key3", key.getKey(2));
      
        // Error scenario: Invalid index
        try {
            key.getKey(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Pass
        }
    }

    @Test
    public void testSize() {
        // Typical use case
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> key = new MultiKey<>(keys);
        assertEquals(3, key.size());
        
        // Edge case: Empty keys
        String[] emptyKeys = {};
        key = new MultiKey<>(emptyKeys);
        assertEquals(0, key.size());
    }

    @Test
    public void testEquals() {
        // Typical use case: Equal keys
        MultiKey<String> key1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> key2 = new MultiKey<>("key1", "key2", "key3");
        assertTrue(key1.equals(key2));
        
        // Typical use case: Non-equal keys
        key2 = new MultiKey<>("key3", "key2", "key1");
        assertFalse(key1.equals(key2));
        
        // Error scenario: Comparing with different object type
        assertFalse(key1.equals("key1, key2, key3"));
    }

    @Test
    public void testHashCode() {
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> key = new MultiKey<>(keys);
        int hashCode = keys[0].hashCode() ^ keys[1].hashCode() ^ keys[2].hashCode();
        assertEquals(hashCode, key.hashCode());
    }

    @Test
    public void testToString() {
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> key = new MultiKey<>(keys);
        assertEquals("MultiKey[key1, key2, key3]", key.toString());
    }

    @Test
    public void testReadResolve() {
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> key = new MultiKey<>(keys);
        assertEquals(key, key.readResolve());
    }
}