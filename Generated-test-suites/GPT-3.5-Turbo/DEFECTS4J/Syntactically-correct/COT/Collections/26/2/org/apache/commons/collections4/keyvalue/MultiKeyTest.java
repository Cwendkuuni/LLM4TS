
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructor_twoKeys() {
        Object key1 = "key1";
        Object key2 = "key2";
        
        MultiKey multiKey = new MultiKey(key1, key2);
        
        Object[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }
    
    @Test
    public void testConstructor_threeKeys() {
        Object key1 = "key1";
        Object key2 = "key2";
        Object key3 = "key3";
        
        MultiKey multiKey = new MultiKey(key1, key2, key3);
        
        Object[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
    }
    
    @Test
    public void testConstructor_fourKeys() {
        Object key1 = "key1";
        Object key2 = "key2";
        Object key3 = "key3";
        Object key4 = "key4";
        
        MultiKey multiKey = new MultiKey(key1, key2, key3, key4);
        
        Object[] keys = multiKey.getKeys();
        assertEquals(4, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
    }
    
    @Test
    public void testConstructor_fiveKeys() {
        Object key1 = "key1";
        Object key2 = "key2";
        Object key3 = "key3";
        Object key4 = "key4";
        Object key5 = "key5";
        
        MultiKey multiKey = new MultiKey(key1, key2, key3, key4, key5);
        
        Object[] keys = multiKey.getKeys();
        assertEquals(5, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
        assertEquals(key5, keys[4]);
    }
    
    @Test
    public void testConstructor_array() {
        Object[] keys = new Object[] {"key1", "key2"};
        
        MultiKey multiKey = new MultiKey(keys);
        
        Object[] multiKeys = multiKey.getKeys();
        assertEquals(keys.length, multiKeys.length);
        assertArrayEquals(keys, multiKeys);
    }
    
    @Test
    public void testConstructor_array_clone() {
        Object[] keys = new Object[] {"key1", "key2"};
        
        MultiKey multiKey = new MultiKey(keys, false);
        
        Object[] multiKeys = multiKey.getKeys();
        assertEquals(keys.length, multiKeys.length);
        assertNotSame(keys, multiKeys);
        assertArrayEquals(keys, multiKeys);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullArray() {
        new MultiKey(null);
    }
    
    @Test
    public void testGetKeys_singleKey() {
        Object key = "key";
        
        MultiKey multiKey = new MultiKey(key);
        
        Object[] keys = multiKey.getKeys();
        assertEquals(1, keys.length);
        assertEquals(key, keys[0]);
    }
    
    @Test
    public void testGetKey_validIndex() {
        Object key1 = "key1";
        Object key2 = "key2";
        Object key3 = "key3";
        MultiKey multiKey = new MultiKey(key1, key2, key3);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_invalidIndex() {
        MultiKey multiKey = new MultiKey("key");
        
        multiKey.getKey(1);
    }
    
    @Test
    public void testSize_singleKey() {
        MultiKey multiKey = new MultiKey("key");
        
        assertEquals(1, multiKey.size());
    }
    
}
