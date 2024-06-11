import org.junit.*;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
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
    public void testConstructorFourKeys() {
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
    public void testConstructorFiveKeys() {
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
    public void testConstructorArrayClone() {
        Integer[] keys = new Integer[]{1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(5, multiKey.size());
    }
    
    @Test
    public void testConstructorArrayNoClone() {
        Integer[] keys = new Integer[]{1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys, false);
        
        assertSame(keys, multiKey.getKeys());
        assertEquals(5, multiKey.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(null);
    }
    
    @Test
    public void testEquals() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey3 = new MultiKey<>(key2, key1);
        
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals("text"));
        assertFalse(multiKey1.equals(multiKey3));
    }
    
    @Test
    public void testHashCode() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key2, key1);
        
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals("MultiKey[1, 2]", multiKey.toString());
    }
    
    @Test
    public void testReadResolve() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2);
        
        MultiKey<Integer> result = (MultiKey<Integer>) multiKey1.readResolve();
        
        assertEquals(multiKey2, result);
    }
}