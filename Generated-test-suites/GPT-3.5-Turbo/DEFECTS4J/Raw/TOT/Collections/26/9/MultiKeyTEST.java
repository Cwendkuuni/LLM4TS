import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;


public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorWithThreeKeys() {
        int key1 = 1;
        int key2 = 2;
        int key3 = 3;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);

        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals(key1, (int) multiKey.getKey(0));
        Assert.assertEquals(key2, (int) multiKey.getKey(1));
        Assert.assertEquals(key3, (int) multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithFourKeys() {
        boolean key1 = true;
        boolean key2 = false;
        boolean key3 = true;
        boolean key4 = false;

        MultiKey<Boolean> multiKey = new MultiKey<>(key1, key2, key3, key4);

        Assert.assertEquals(4, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorWithFiveKeys() {
        char key1 = 'a';
        char key2 = 'b';
        char key3 = 'c';
        char key4 = 'd';
        char key5 = 'e';

        MultiKey<Character> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        Assert.assertEquals(5, multiKey.size());
        Assert.assertEquals(key1, (char) multiKey.getKey(0));
        Assert.assertEquals(key2, (char) multiKey.getKey(1));
        Assert.assertEquals(key3, (char) multiKey.getKey(2));
        Assert.assertEquals(key4, (char) multiKey.getKey(3));
        Assert.assertEquals(key5, (char) multiKey.getKey(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        new MultiKey<>(null);
    }

    @Test
    public void testConstructorWithArray() {
        Object[] keys = {1, 2, 3, 4, 5};

        MultiKey<Object> multiKey = new MultiKey<>(keys);

        Assert.assertEquals(5, multiKey.size());
        Assert.assertEquals(keys[0], multiKey.getKey(0));
        Assert.assertEquals(keys[1], multiKey.getKey(1));
        Assert.assertEquals(keys[2], multiKey.getKey(2));
        Assert.assertEquals(keys[3], multiKey.getKey(3));
        Assert.assertEquals(keys[4], multiKey.getKey(4));
    }

    @Test
    public void testGetKeys() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        String[] keys = multiKey.getKeys();

        Assert.assertEquals(2, keys.length);
        Assert.assertEquals(key1, keys[0]);
        Assert.assertEquals(key2, keys[1]);
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key1, key3);

        Assert.assertTrue(multiKey1.equals(multiKey1));
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));
        Assert.assertFalse(multiKey1.equals(null));
        Assert.assertFalse(multiKey1.equals("key1"));
    }

    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        Assert.assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test
    public void testUsageInMap() {
        String key1 = "key1";
        String key2 = "key2";
        String localizedText = "localizedText";

        Map<MultiKey<String>, String> map = new HashMap<>();
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        map.put(multiKey, localizedText);

        String retrievedText = map.get(multiKey);

        Assert.assertEquals(localizedText, retrievedText);
    }
}