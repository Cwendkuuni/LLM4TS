package org.apache.commons.collections4.keyvalue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {

    private MultiKey<String> multiKey;
    private String key1;
    private String key2;
    private String key3;
    private String key4;
    private String key5;

    @Before
    public void setUp() {
        key1 = "key1";
        key2 = "key2";
        key3 = "key3";
        key4 = "key4";
        key5 = "key5";
        multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
    }

    @Test
    public void testGetKeys() {
        String[] expectedKeys = {key1, key2, key3, key4, key5};
        Assert.assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testGetKey() {
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key5, multiKey.getKey(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_IndexOutOfBounds() {
        multiKey.getKey(5);
    }

    @Test
    public void testSize() {
        Assert.assertEquals(5, multiKey.size());
    }

    @Test
    public void testEquals_SameObject() {
        Assert.assertTrue(multiKey.equals(multiKey));
    }

    @Test
    public void testEquals_SameValues() {
        MultiKey<String> otherMultiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        Assert.assertTrue(multiKey.equals(otherMultiKey));
    }

    @Test
    public void testEquals_DifferentValues() {
        MultiKey<String> otherMultiKey = new MultiKey<>(key1, key2, key3, key4, "different");
        Assert.assertFalse(multiKey.equals(otherMultiKey));
    }

    @Test
    public void testEquals_DifferentClass() {
        Assert.assertFalse(multiKey.equals(new Object()));
    }

    @Test
    public void testHashCode_SameValues() {
        MultiKey<String> otherMultiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        Assert.assertEquals(multiKey.hashCode(), otherMultiKey.hashCode());
    }

    @Test
    public void testHashCode_DifferentValues() {
        MultiKey<String> otherMultiKey = new MultiKey<>(key1, key2, key3, key4, "different");
        Assert.assertNotEquals(multiKey.hashCode(), otherMultiKey.hashCode());
    }

    @Test
    public void testHashCode_NullKey() {
        MultiKey<String> nullKeyMultiKey = new MultiKey<>(key1, null, key3, key4, key5);
        MultiKey<String> otherNullKeyMultiKey = new MultiKey<>(key1, null, key3, key4, key5);
        Assert.assertEquals(nullKeyMultiKey.hashCode(), otherNullKeyMultiKey.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "MultiKey[key1, key2, key3, key4, key5]";
        Assert.assertEquals(expectedString, multiKey.toString());
    }
}