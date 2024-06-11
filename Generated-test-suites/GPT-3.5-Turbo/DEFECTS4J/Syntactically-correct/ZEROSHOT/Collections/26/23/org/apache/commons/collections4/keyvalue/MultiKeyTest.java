import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    
    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
    }
    
    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
    }
    
    @Test
    public void testConstructorFourKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
    }
    
    @Test
    public void testConstructorFiveKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
        assertEquals(key5, multiKey.getKeys()[4]);
    }
    
    @Test
    public void testConstructorArrayKeys() {
        Integer[] keys = new Integer[] {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        assertArrayEquals(keys, multiKey.getKeys());
    }
    
    @Test
    public void testGetKeyAtIndex() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testSize() {
        Integer[] keys = new Integer[] {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        assertEquals(keys.length, multiKey.size());
    }
    
    @Test
    public void testEqualsSameKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2);
        
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEqualsDifferentKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key2, key1);
        
        assertFalse(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEqualsWithNonMultiKeyObject() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        Object obj = new Object();
        
        assertFalse(multiKey.equals(obj));
    }
    
    @Test
    public void testHashCodeSameKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals("MultiKey[1, 2]", multiKey.toString());
    }
}