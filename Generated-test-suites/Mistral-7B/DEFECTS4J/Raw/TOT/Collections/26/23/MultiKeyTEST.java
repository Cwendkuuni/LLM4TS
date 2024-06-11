import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeyTestCase;
import org.junit.Assert;
import org.junit.Test;

public class MultiKeyTest extends MultiKeyTestCase {

    @Test
    public void testMultiKeyCreation() {
        K key1 = createKey1();
        K key2 = createKey2();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        Assert.assertEquals(2, multiKey.size());
        Assert.assertArrayEquals(new K[] { key1, key2 }, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyCreationWithThreeKeys() {
        K key1 = createKey1();
        K key2 = createKey2();
        K key3 = createKey3();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        Assert.assertEquals(3, multiKey.size());
        Assert.assertArrayEquals(new K[] { key1, key2, key3 }, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyCreationWithFourKeys() {
        K key1 = createKey1();
        K key2 = createKey2();
        K key3 = createKey3();
        K key4 = createKey4();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        Assert.assertEquals(4, multiKey.size());
        Assert.assertArrayEquals(new K[] { key1, key2, key3, key4 }, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyCreationWithArray() {
        K key1 = createKey1();
        K key2 = createKey2();
        MultiKey<K> multiKey = new MultiKey<>(new K[] { key1, key2 });
        Assert.assertEquals(2, multiKey.size());
        Assert.assertArrayEquals(new K[] { key1, key2 }, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = createKey1();
        K key2 = createKey2();
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        Assert.assertEquals(multiKey1, multiKey2);
        Assert.assertEquals(multiKey2, multiKey1);
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = createKey1();
        K key2 = createKey2();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int hashCode = multiKey.hashCode();
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        Assert.assertEquals(hashCode, multiKey2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullArray() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKey() {
        MultiKey<K> multiKey = new MultiKey<>(new K[] { null });
    }

    private static class MultiKeyTestCase {
        private K createKey1() {
            return null; // Replace this with the actual key creation logic
        }

        private K createKey2() {
            return null; // Replace this with the actual key creation logic
        }

        private K createKey3() {
            return null; // Replace this with the actual key creation logic
        }

        private K createKey4() {
            return null; // Replace this with the actual key creation logic
        }
    }
}