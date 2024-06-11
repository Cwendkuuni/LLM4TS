import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testConstructor_ThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testConstructor_FourKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }
    
    @Test
    public void testConstructor_FiveKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }
    
    @Test
    public void testConstructor_Array() {
        Integer[] keys = {1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keys[i], multiKey.getKey(i));
        }
        assertEquals(keys.length, multiKey.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullArray() {
        Integer[] keys = null;
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
    }
    
    @Test
    public void testEquals_SameObject() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertTrue(multiKey.equals(multiKey));
    }
    
    @Test
    public void testEquals_SameKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2);
        
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_DifferentKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key2, key1);
        
        assertFalse(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_DifferentObject() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        String other = "test";
        
        assertFalse(multiKey.equals(other));
    }
    
    @Test
    public void testHashCode_SameKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2);
        
        assertTrue(multiKey1.hashCode() == multiKey2.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key2, key1);
        
        assertFalse(multiKey1.hashCode() == multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals("MultiKey[1, 2]", multiKey.toString());
    }
    
    @Test
    public void testCalculateHashCode() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        multiKey.calculateHashCode(multiKey.getKeys());
        assertEquals(key1.hashCode() ^ key2.hashCode(), multiKey.hashCode());
    }
    
    @Test
    public void testReadResolve() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        MultiKey<Integer> resolvedMultiKey = (MultiKey<Integer>) multiKey.readResolve();
        assertTrue(multiKey.equals(resolvedMultiKey));
    }
}