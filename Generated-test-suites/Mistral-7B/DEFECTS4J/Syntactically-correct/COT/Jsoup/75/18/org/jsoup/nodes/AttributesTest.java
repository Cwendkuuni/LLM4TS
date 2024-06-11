package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.EMPTY;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.NotFound;

public class AttributesTest {

    Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGetEmpty() {
        Assert.assertEquals(EMPTY, attributes.get(EMPTY));
    }

    @Test
    public void testGetSet() {
        attributes.put("testKey", "testValue");
        Assert.assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("TestKey", "TestValue");
        Assert.assertEquals("TestValue", attributes.getIgnoreCase("testKey"));
    }

    @Test
    public void testPut() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        Attributes newAttributes = attributes.put("testKey1", "updatedTestValue1");
        Assert.assertEquals("updatedTestValue1", newAttributes.get("testKey1"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("TestKey1", "TestValue1");
        attributes.putIgnoreCase("TestKey2", "TestValue2");
        Attributes newAttributes = attributes.putIgnoreCase("testKey1", "updatedTestValue1");
        Assert.assertEquals("updatedTestValue1", newAttributes.get("testKey1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("testKey", true);
        Assert.assertTrue(attributes.getBoolean("testKey"));
        attributes.remove("testKey");
    }

    @Test
    public void testPutBooleanIgnoreCase() {
        attributes.putIgnoreCase("TESTKey", true);
        Assert.assertTrue(attributes.getBoolean("testKey"));
        attributes.remove("testKey");
    }

    @Test
    public void testRemove() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        attributes.remove("testKey1");
        Assert.assertFalse(attributes.hasKey("testKey1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("TestKey1", "TestValue1");
        attributes.put("TestKey2", "TestValue2");
        attributes.removeIgnoreCase("testKey1");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("testKey1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("testKey1", "testValue1");
        Assert.assertTrue(attributes.hasKey("testKey1"));
        Assert.assertFalse(attributes.hasKey("testKey2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("TestKey1", "TestValue1");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("testKey1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("testKey2"));
    }

    @Test
    public void testSize() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        attributes1.put("testKey2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("testKey3", "testValue3");
        attributes2.put("testKey4", "testValue4");

        attributes.addAll(attributes1);
        Assert.assertEquals(4, attributes.size());
        Assert.assertTrue(attributes.hasKey("testKey1"));
        Assert.assertTrue(attributes.hasKey("testKey2"));
        Assert.assertTrue(attributes.hasKey("testKey3"));
        Assert.assertTrue(attributes.hasKey("testKey4"));
    }

    @Test
    public void testToString() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        Assert.assertEquals("testValue1 testKey1=\"testValue1\" testValue2 testKey2=\"testValue2\"",
                attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        attributes1.put("testKey2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("testKey1", "testValue1");
        attributes2.put("testKey2", "testValue2");

        Attributes attributes3 = new Attributes();
        attributes3.put("testKey1", "testValue1");
        attributes3.put("testKey3", "testValue3");

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        attributes1.put("testKey2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("testKey1", "testValue1");
        attributes2.put("testKey2", "testValue2");

        Attributes attributes3 = new Attributes();
        attributes3.put("testKey1", "testValue1");
        attributes3.put("testKey3", "testValue3");

        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        attributes1.put("testKey2", "testValue2");

        Attributes attributes2 = attributes1.clone();
        Assert.assertEquals(attributes1, attributes2);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("TEST_KEY", "testValue");
        attributes.normalize();
        Assert.assertEquals("testKey", attributes.get("testKey"));
    }

    @Test
    public void testDataPrefix() {
        Attributes attributes = new Attributes();
        attributes.put("dataTestKey", "dataTestValue");
        Map<String, String> data = attributes.dataset();
        Assert.assertEquals("dataTestKey", data.keySet().iterator().next());
        Assert.assertEquals("dataTestValue", data.values().iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        attributes.put(null, "testValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        attributes.put("testKey", null);
    }

    @Test
    public void testCapacity() {
        Attributes attributes = new Attributes();

        for (int i = 0; i < InitialCapacity * GrowthFactor * 2; i++) {
            attributes.put("testKey" + i, "testValue" + i);
        }
        Assert.assertEquals(InitialCapacity * GrowthFactor * 2, attributes.size());
    }

    @Test
    public void testCopyOf() {
        String[] orig = {"testKey1", "testKey2", "testKey3"};
        String[] copy = Attributes.copyOf(orig, 3);
        Assert.assertArrayEquals(Arrays.asList(orig), Arrays.asList(copy));
    }

    @Test
    public void testIndexOfKey() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        Assert.assertEquals(0, attributes.indexOfKey("testKey1"));
        Assert.assertEquals(1, attributes.indexOfKey("testKey2"));
        Assert.assertEquals(NotFound, attributes.indexOfKey("testKey3"));
    }

    @Test
    public void testIndexOfKeyIgnoreCase() {
        attributes.put("TestKey1", "TestValue1");
        attributes.put("TestKey2", "TestValue2");
        Assert.assertEquals(0, attributes.indexOfKeyIgnoreCase("testKey1"));
        Assert.assertEquals(1, attributes.indexOfKeyIgnoreCase("TestKey2"));
        Assert.assertEquals(NotFound, attributes.indexOfKeyIgnoreCase("testKey3"));
    }

    @Test
    public void testCheckCapacity() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        attributes.checkCapacity(5);
        Assert.assertEquals(5, attributes.keys.length);
        Assert.assertEquals(5, attributes.vals.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacity() {
        attributes.checkCapacity(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIndexOutOfBounds() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        attributes.remove(2);
    }

    @Test(expected = NullPointerException.class)
    public void testNullIterator() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        Iterator<Attribute> nullIterator = null;
        Assert.assertFalse(nullIterator.hasNext());
        try {
            nullIterator.next();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCloneNotSupportedException() {
        Attributes attributes1 = new Attributes();
        try {
            attributes1.clone();
        } catch (CloneNotSupportedException e) {
            // expected
        }
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        try {
            attributes.html();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}