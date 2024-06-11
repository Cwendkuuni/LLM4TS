package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String[] keys = multiKey.getKeys();
        
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        Integer[] keys = multiKey.getKeys();
        
        assertEquals(Integer.valueOf(1), keys[0]);
        assertEquals(Integer.valueOf(2), keys[1]);
        assertEquals(Integer.valueOf(3), keys[2]);
        assertEquals(Integer.valueOf(4), keys[3]);
        assertEquals(Integer.valueOf(5), keys[4]);
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key", "value");
        MultiKey<String> multiKey2 = new MultiKey<>("key", "value");
        
        assertEquals(true, multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        
        assertEquals(342, multiKey.hashCode());
    }

    @Test
    public void testGetSize() {
        MultiKey<Character> multiKey = new MultiKey<>('a', 'b', 'c', 'd');
        
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testGetKey() {
        MultiKey<Boolean> multiKey = new MultiKey<>(true, false, true);
        
        assertEquals(true, multiKey.getKey(0));
        assertEquals(false, multiKey.getKey(1));
        assertEquals(true, multiKey.getKey(2));
    }
}