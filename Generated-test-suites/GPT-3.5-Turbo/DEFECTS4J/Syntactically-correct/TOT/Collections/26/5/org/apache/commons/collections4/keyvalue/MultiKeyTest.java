import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    
    private MultiKey<String> multiKey;
    
    @Before
    public void setUp() {
        multiKey = new MultiKey<>("key1", "key2");
    }
    
    // Test for Constructor 1
    @Test
    public void testConstructor1() {
        MultiKey<String> newMultiKey = new MultiKey<>("key1", "key2");
        assertNotNull(newMultiKey);
        assertArrayEquals(new String[]{"key1", "key2"}, newMultiKey.getKeys());
    }
    
    // Test for Constructor 2
    @Test
    public void testConstructor2() {
        MultiKey<String> newMultiKey = new MultiKey<>("key1", "key2", "key3");
        assertNotNull(newMultiKey);
        assertArrayEquals(new String[]{"key1", "key2", "key3"}, newMultiKey.getKeys());
    }
    
    // Test for Constructor 3
    @Test
    public void testConstructor3() {
        MultiKey<String> newMultiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertNotNull(newMultiKey);
        assertArrayEquals(new String[]{"key1", "key2", "key3", "key4"}, newMultiKey.getKeys());
    }
    
    // Test for Constructor 4
    @Test
    public void testConstructor4() {
        MultiKey<String> newMultiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertNotNull(newMultiKey);
        assertArrayEquals(new String[]{"key1", "key2", "key3", "key4", "key5"}, newMultiKey.getKeys());
    }
    
    // Test for Constructor 5
    @Test
    public void testConstructor5() {
        String[] keys = new String[]{"key1", "key2", "key3", "key4", "key5"};
        MultiKey<String> newMultiKey = new MultiKey<>(keys);
        assertNotNull(newMultiKey);
        assertArrayEquals(keys, newMultiKey.getKeys());
    }
    
    // Test for getKey(int index)
    @Test
    public void testGetKey() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }
    
    // Test for size()
    @Test
    public void testSize() {
        assertEquals(2, multiKey.size());
    }
    
    // Test for equals(Object other)
    @Test
    public void testEquals() {
        MultiKey<String> newMultiKey = new MultiKey<>("key1", "key2");
        assertTrue(multiKey.equals(newMultiKey));
        
        MultiKey<String> diffMultiKey = new MultiKey<>("key1", "key3");
        assertFalse(multiKey.equals(diffMultiKey));
        
        assertFalse(multiKey.equals(null));
        assertFalse(multiKey.equals("key1"));
    }
    
    // Test for hashCode()
    @Test
    public void testHashCode() {
        assertEquals(multiKey.getKey(0).hashCode() ^ multiKey.getKey(1).hashCode(), multiKey.hashCode());
    }
    
    // Test for toString()
    @Test
    public void testToString() {
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
    
    // Test for calculateHashCode(Object[] keys)
    @Test
    public void testCalculateHashCode() {
        Object[] keys = new Object[]{"key1", "key2"};
        multiKey.calculateHashCode(keys);
        
        int expectedHashCode = keys[0].hashCode() ^ keys[1].hashCode();
        assertEquals(expectedHashCode, multiKey.hashCode());
    }
    
    // Test for readResolve()
    @Test
    public void testReadResolve() {
        MultiKey<String> resolvedMultiKey = (MultiKey<String>) multiKey.readResolve();
        assertEquals(multiKey.hashCode(), resolvedMultiKey.hashCode());
    }
}