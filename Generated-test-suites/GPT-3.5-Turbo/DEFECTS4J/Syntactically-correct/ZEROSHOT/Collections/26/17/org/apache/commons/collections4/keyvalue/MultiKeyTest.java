import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        // Test case 1: construct a MultiKey object with two keys
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Verify the keys are correctly set
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        // Test case 2: construct a MultiKey object with three keys
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        // Verify the keys are correctly set
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorFourKeys() {
        // Test case 3: construct a MultiKey object with four keys
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        // Verify the keys are correctly set
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorFiveKeys() {
        // Test case 4: construct a MultiKey object with five keys
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        // Verify the keys are correctly set
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorArrayKeys() {
        // Test case 5: construct a MultiKey object with an array of keys
        String[] keys = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        // Verify the keys are correctly set
        Assert.assertEquals(keys[0], multiKey.getKey(0));
        Assert.assertEquals(keys[1], multiKey.getKey(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArray() {
        // Test case 6: construct a MultiKey object with a null array of keys
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        // Test case 7: get key with invalid index
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Try to get key at index 2, which is invalid
        multiKey.getKey(2);
    }

    @Test
    public void testEquals() {
        // Test case 8: test equals method
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key1);

        // Verify the equals method returns true for the same keys
        Assert.assertTrue(multiKey1.equals(multiKey1));

        // Verify the equals method returns true for multiKey1 and multiKey2
        Assert.assertTrue(multiKey1.equals(multiKey2));

        // Verify the equals method returns false for multiKey1 and multiKey3
        Assert.assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        // Test case 9: test hashCode method
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key1);

        // Verify the hashCode method returns the same value for the same keys
        Assert.assertEquals(multiKey1.hashCode(), multiKey1.hashCode());

        // Verify the hashCode method returns the same value for multiKey1 and multiKey2
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());

        // Verify the hashCode method returns different values for multiKey1 and multiKey3
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testToString() {
        // Test case 10: test toString method
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Verify the toString method returns the expected string
        Assert.assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test
    public void testGetKeys() {
        // Test case 11: test getKeys method
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Verify the getKeys method returns the cloned array of keys
        Assert.assertEquals(key1, multiKey.getKeys()[0]);
        Assert.assertEquals(key2, multiKey.getKeys()[1]);
    }

    @Test
    public void testSize() {
        // Test case 12: test size method
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Verify the size method returns the correct number of keys
        Assert.assertEquals(2, multiKey.size());
    }

    @Test
    public void testSerialization() {
        // Test case 13: test serialization/deserialization
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Serialize the MultiKey object
        byte[] serialized = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(multiKey);
            serialized = baos.toByteArray();
        } catch (IOException e) {
            Assert.fail("IOException occurred while serializing MultiKey object");
        }

        // Deserialize the MultiKey object
        MultiKey<String> deserialized = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
            ObjectInputStream ois = new ObjectInputStream(bais);
            deserialized = (MultiKey<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("IOException or ClassNotFoundException occurred while deserializing MultiKey object");
        }

        // Verify the deserialized MultiKey object is equal to the original one
        Assert.assertEquals(multiKey, deserialized);
    }

    @Test
    public void testUsageExample() {
        // Test case 14: test usage example
        Map<MultiKey<String>, String> map = new HashMap<>();

        // Populate the map with data mapping key+locale to localized text
        String key1 = "key1";
        String key2 = "key2";
        String localizedText1 = "localizedText1";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        map.put(multiKey1, localizedText1);

        // Retrieve the localized text
        String localizedText = map.get(multiKey1);

        // Verify the retrieved localized text is the same as the original one
        Assert.assertEquals(localizedText1, localizedText);
    }
}