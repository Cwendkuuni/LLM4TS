import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
    }
    
    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
    }
    
    @Test
    public void testConstructorFourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        String[] keys = multiKey.getKeys();
        assertEquals(4, keys.length);
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
        assertEquals("key4", keys[3]);
    }
    
    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        String[] keys = multiKey.getKeys();
        assertEquals(5, keys.length);
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
        assertEquals("key4", keys[3]);
        assertEquals("key5", keys[4]);
    }
    
    @Test
    public void testConstructorArrayKeys() {
        String[] keysArray = new String[3];
        keysArray[0] = "key1";
        keysArray[1] = "key2";
        keysArray[2] = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(keysArray);
        String[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeysArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }
    
    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String key = multiKey.getKey(2);
    }
    
    @Test
    public void testSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("KEY1", "KEY2");
        
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
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
}