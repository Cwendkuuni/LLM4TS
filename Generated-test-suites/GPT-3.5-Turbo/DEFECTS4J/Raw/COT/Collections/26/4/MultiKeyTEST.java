import org.junit.Assert;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        Assert.assertEquals(Integer.valueOf(2), multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        Assert.assertEquals(Integer.valueOf(2), multiKey.getKey(1));
        Assert.assertEquals(Integer.valueOf(3), multiKey.getKey(2));
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
        Assert.assertEquals(4, multiKey.size());
        Assert.assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        Assert.assertEquals(Integer.valueOf(2), multiKey.getKey(1));
        Assert.assertEquals(Integer.valueOf(3), multiKey.getKey(2));
        Assert.assertEquals(Integer.valueOf(4), multiKey.getKey(3));
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        Assert.assertEquals(5, multiKey.size());
        Assert.assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        Assert.assertEquals(Integer.valueOf(2), multiKey.getKey(1));
        Assert.assertEquals(Integer.valueOf(3), multiKey.getKey(2));
        Assert.assertEquals(Integer.valueOf(4), multiKey.getKey(3));
        Assert.assertEquals(Integer.valueOf(5), multiKey.getKey(4));
    }

    @Test
    public void testConstructorWithArray() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        Assert.assertEquals(Integer.valueOf(2), multiKey.getKey(1));
        Assert.assertEquals(Integer.valueOf(3), multiKey.getKey(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        Integer[] keys = null;
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
    }

    @Test
    public void testGetKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Integer[] keys = multiKey.getKeys();
        Assert.assertEquals(3, keys.length);
        Assert.assertEquals(Integer.valueOf(1), keys[0]);
        Assert.assertEquals(Integer.valueOf(2), keys[1]);
        Assert.assertEquals(Integer.valueOf(3), keys[2]);
    }

    @Test
    public void testSize() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Assert.assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey3 = new MultiKey<>(1, 2, 4);
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Assert.assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }
}