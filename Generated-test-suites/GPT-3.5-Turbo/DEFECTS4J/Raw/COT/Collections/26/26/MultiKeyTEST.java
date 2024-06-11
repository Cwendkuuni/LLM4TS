
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructorThreeKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test
    public void testConstructorFourKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }
    
    @Test
    public void testConstructorFiveKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        String key5 = "Key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
    
    @Test
    public void testConstructorArray() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        MultiKey<String> multiKey = new MultiKey<>(new String[] {key1, key2, key3});
        
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArray() {
        String[] keys = null;
        MultiKey<String> multiKey = new MultiKey<>(keys);
    }
    
    @Test
    public void testEquals() {
        String key1 = "Key1";
        String key2 = "Key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key1);
        MultiKey<String> multiKey4 = new MultiKey<>(key1);
        
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
    }
    
    @Test
    public void testHashCode() {
        String key1 = "Key1";
        String key2 = "Key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        String key1 = "Key1";
        String key2 = "Key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[Key1, Key2]", multiKey.toString());
    }
}
