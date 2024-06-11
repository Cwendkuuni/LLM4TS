import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {
    
    @Test
    public void testConstructorWithTwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        assertNotNull(multiKey);
        assertEquals(2, multiKey.size());
        assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        assertEquals(Integer.valueOf(2), multiKey.getKey(1));
    }
    
    @Test
    public void testEqualsAndHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key", "value");
        MultiKey<String> multiKey2 = new MultiKey<>("key", "value");
        assertTrue(multiKey1.equals(multiKey2));
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKeys() {
        new MultiKey<>(null);
    }
    
    @Test
    public void testGetKeys() {
        MultiKey<String> multiKey = new MultiKey<>("a", "b", "c");
        assertEquals(3, multiKey.getKeys().length);
        assertArrayEquals(new String[]{"a", "b", "c"}, multiKey.getKeys());
    }
    
    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("apple", "banana", "cherry");
        assertEquals("apple", multiKey.getKey(0));
        assertEquals("banana", multiKey.getKey(1));
        assertEquals("cherry", multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        multiKey.getKey(3);
    }
    
    @Test
    public void testSize() {
        MultiKey<Double> multiKey = new MultiKey<>(1.1, 2.2, 3.3, 4.4, 5.5);
        assertEquals(5, multiKey.size());
    }
    
}