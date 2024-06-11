import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorOneKey() {
        Integer key = 1;
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key);
        assertEquals(1, multiKey.size());
        assertEquals(key, multiKey.getKey(0));
    }
    
    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2);
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2, key3);
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test
    public void testConstructorFourKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2, key3, key4);
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }
    
    @Test
    public void testConstructorFiveKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2, key3, key4, key5);
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        Integer[] keys = null;
        MultiKey<Integer> multiKey = new MultiKey<Integer>(keys);
    }
    
    @Test
    public void testConstructorKeysMakeClone() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        Integer[] keys = {key1, key2};
        MultiKey<Integer> multiKey = new MultiKey<Integer>(keys);
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        
        keys[0] = new Integer(3);
        
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testEquals() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<Integer>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<Integer>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
    }
    
    @Test
    public void testNotEquals() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<Integer>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<Integer>(key2, key1);
        assertFalse(multiKey1.equals(multiKey2));
        assertFalse(multiKey2.equals(multiKey1));
    }
    
    @Test
    public void testEqualHashCode() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<Integer>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<Integer>(key1, key2);
        assertTrue(multiKey1.hashCode() == multiKey2.hashCode());
    }
    
    @Test
    public void testNotEqualHashCode() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<Integer>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<Integer>(key2, key1);
        assertFalse(multiKey1.hashCode() == multiKey2.hashCode());
    }
}