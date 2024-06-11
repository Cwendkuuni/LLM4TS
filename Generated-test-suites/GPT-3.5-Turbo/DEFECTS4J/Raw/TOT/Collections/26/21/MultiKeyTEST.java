
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
    public void testConstructorWithTwoKeys() {
        MultiKey<String> twoKeys = new MultiKey<>("key1", "key2");
        Assert.assertEquals(2, twoKeys.size());
        Assert.assertEquals("key1", twoKeys.getKey(0));
        Assert.assertEquals("key2", twoKeys.getKey(1));
    }

    @Test
    public void testConstructorWithThreeKeys() {
        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithFourKeys() {
        MultiKey<String> fourKeys = new MultiKey<>("key1", "key2", "key3", "key4");
        Assert.assertEquals(4, fourKeys.size());
        Assert.assertEquals("key1", fourKeys.getKey(0));
        Assert.assertEquals("key2", fourKeys.getKey(1));
        Assert.assertEquals("key3", fourKeys.getKey(2));
        Assert.assertEquals("key4", fourKeys.getKey(3));
    }

    @Test
    public void testConstructorWithFiveKeys() {
        MultiKey<String> fiveKeys = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        Assert.assertEquals(5, fiveKeys.size());
        Assert.assertEquals("key1", fiveKeys.getKey(0));
        Assert.assertEquals("key2", fiveKeys.getKey(1));
        Assert.assertEquals("key3", fiveKeys.getKey(2));
        Assert.assertEquals("key4", fiveKeys.getKey(3));
        Assert.assertEquals("key5", fiveKeys.getKey(4));
    }

    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> arrayKeys = new MultiKey<>(keys);
        Assert.assertEquals(3, arrayKeys.size());
        Assert.assertEquals("key1", arrayKeys.getKey(0));
        Assert.assertEquals("key2", arrayKeys.getKey(1));
        Assert.assertEquals("key3", arrayKeys.getKey(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<String> nullArrayKeys = new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        MultiKey<String> equalKeys = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> differentKeys = new MultiKey<>("key1", "key2", "key4");
        MultiKey<String> differentSize = new MultiKey<>("key1", "key2", "key3", "key4");
        MultiKey<String> notMultiKey = new MultiKey<>("key1", "key2", "key3") {};
        
        Assert.assertTrue(multiKey.equals(equalKeys));
        Assert.assertFalse(multiKey.equals(differentKeys));
        Assert.assertFalse(multiKey.equals(differentSize));
        Assert.assertFalse(multiKey.equals(notMultiKey));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> equalKeys = new MultiKey<>("key1", "key2", "key3");
        Assert.assertEquals(multiKey.hashCode(), equalKeys.hashCode());
    }

    @Test
    public void testGetKeys() {
        String[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new String[]{"key1", "key2", "key3"}, keys);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}
