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
    public void testGetKeys() {
        String[] expectedKeys = {"key1", "key2", "key3"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testGetKey() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        try {
            multiKey.getKey(3);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Successful test
        }
    }
    
    @Test
    public void testSize() {
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> sameKey = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> differentKey = new MultiKey<>("key1", "key3", "key2");
        
        assertTrue(multiKey.equals(multiKey));
        assertTrue(multiKey.equals(sameKey));
        assertFalse(multiKey.equals(differentKey));
        assertFalse(multiKey.equals(null));
        assertFalse(multiKey.equals("Not a MultiKey object"));
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> sameKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals(multiKey.hashCode(), sameKey.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}