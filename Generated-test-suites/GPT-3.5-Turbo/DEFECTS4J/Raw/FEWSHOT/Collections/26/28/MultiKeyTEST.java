import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {
    @Test
    public void testConstructorWithTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test
    public void testConstructorWithFourKeys() {
        Double key1 = 1.0;
        Double key2 = 2.0;
        Double key3 = 3.0;
        Double key4 = 4.0;
        
        MultiKey<Double> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }
    
    @Test
    public void testConstructorWithFiveKeys() {
        Boolean key1 = true;
        Boolean key2 = false;
        Boolean key3 = true;
        Boolean key4 = false;
        Integer key5 = 5;
        
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
    
    @Test
    public void testConstructorWithArray() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        assertEquals(3, multiKey.size());
        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
        assertEquals(keys[2], multiKey.getKey(2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<Object> multiKey = new MultiKey<>(null);
    }
    
    @Test
    public void testGetKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        Integer[] keys = multiKey.getKeys();
        
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }
    
    @Test
    public void testGetKey() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        MultiKey<Object> multiKey = new MultiKey<>(new Object[]{});
        multiKey.getKey(0);
    }
    
    @Test
    public void testSize() {
        MultiKey<Object> multiKey = new MultiKey<>(new Object[]{});
        assertEquals(0, multiKey.size());
        
        multiKey = new MultiKey<>(1, 2);
        assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testEquals() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey3 = new MultiKey<>(2, 1);
        MultiKey<Integer> multiKey4 = new MultiKey<>(3, 4);
        
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey3.equals(multiKey1));
        assertFalse(multiKey1.equals(multiKey4));
        assertFalse(multiKey4.equals(multiKey1));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey3 = new MultiKey<>(2, 1);
        
        assertEquals(multiKey1.hashCode(), multiKey1.hashCode());
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }
    
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}