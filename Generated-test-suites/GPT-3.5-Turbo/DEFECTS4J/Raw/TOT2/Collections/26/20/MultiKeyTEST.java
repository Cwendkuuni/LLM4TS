import org.junit.Test;
import org.junit.Assert;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals((Integer) 1, multiKey.getKey(0));
        Assert.assertEquals((Integer) 2, multiKey.getKey(1));
    }

    @Test
    public void testConstructor_ThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testConstructor_FourKeys() {
        MultiKey<Float> multiKey = new MultiKey<>(1.1f, 2.2f, 3.3f, 4.4f);
        Assert.assertEquals(4, multiKey.size());
        Assert.assertEquals(1.1f, multiKey.getKey(0));
        Assert.assertEquals(2.2f, multiKey.getKey(1));
        Assert.assertEquals(3.3f, multiKey.getKey(2));
        Assert.assertEquals(4.4f, multiKey.getKey(3));
    }

    @Test
    public void testEquals() {
        MultiKey<Boolean> multiKey1 = new MultiKey<>(true, false);
        MultiKey<Boolean> multiKey2 = new MultiKey<>(true, false);
        MultiKey<Boolean> multiKey3 = new MultiKey<>(false, true);
        
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<Character> multiKey1 = new MultiKey<>('a', 'b');
        MultiKey<Character> multiKey2 = new MultiKey<>('a', 'b');

        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_IndexOutOfBounds() {
        MultiKey<Integer> multiKey = new MultiKey<>(10, 20);
        multiKey.getKey(2);
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        Assert.assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}