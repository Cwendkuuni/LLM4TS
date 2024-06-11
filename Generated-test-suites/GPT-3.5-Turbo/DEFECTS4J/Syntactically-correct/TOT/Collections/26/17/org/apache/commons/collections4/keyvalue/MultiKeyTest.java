import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {
    
    private MultiKey<String> multiKey;
    
    @Before
    public void setUp() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
    }
    
    @Test
    public void testConstructorWithKeys() {
        String[] keys = multiKey.getKeys();
        Assert.assertEquals("key1", keys[0]);
        Assert.assertEquals("key2", keys[1]);
        Assert.assertEquals("key3", keys[2]);
    }
    
    @Test
    public void testGetKey() {
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyOutOfBounds() {
        multiKey.getKey(3);
    }
    
    @Test
    public void testSize() {
        Assert.assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> anotherMultiKey = new MultiKey<>("key1", "key2", "key3");
        Assert.assertEquals(multiKey, anotherMultiKey);
    }
    
    @Test
    public void testNotEquals() {
        MultiKey<String> anotherMultiKey = new MultiKey<>("key1", "key2", "key4");
        Assert.assertNotEquals(multiKey, anotherMultiKey);
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> anotherMultiKey = new MultiKey<>("key1", "key2", "key3");
        Assert.assertEquals(multiKey.hashCode(), anotherMultiKey.hashCode());
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}