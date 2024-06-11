package org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    // Your test cases go here

    @Test
    public void testGetKey() {
        Attribute attr = new Attribute("testKey", "testValue");
        String key = attr.getKey();
        Assert.assertEquals("testKey", key);

        // Test edge cases and exception handling scenarios
        Attribute attrEmptyKey = new Attribute("", "testValue");
        String keyEmpty = attrEmptyKey.getKey();
        Assert.assertEquals("", keyEmpty);

        Attribute attrNullKey = new Attribute(null, "testValue");
        Assert.assertNull(attrNullKey.getKey());

        Attribute attrWithoutKey = new Attribute(null, null);
        Assert.assertNull(attrWithoutKey.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("testKey", "testValue");
        String newValue = "newValue";
        String oldValue = attr.setValue(newValue);
        Assert.assertEquals("testValue", oldValue);
        Assert.assertEquals("newValue", attr.getValue());

        // Test edge cases and exception handling scenarios
        Attribute attrWithNullValue = new Attribute("testKey", null);
        String oldValueNull = attrWithNullValue.setValue("newValue");
        Assert.assertEquals(null, oldValueNull);
        Assert.assertEquals("newValue", attrWithNullValue.getValue());

        Attribute attrWithoutValue = new Attribute("testKey", null);
        String oldValueEmpty = attrWithoutValue.setValue("newValue");
        Assert.assertNull(oldValueEmpty);
        Assert.assertEquals("newValue", attrWithoutValue.getValue());

        Attribute attrWithEmptyStringValue = new Attribute("testKey", "");
        String oldValueEmptyString = attrWithEmptyStringValue.setValue("newValue");
        Assert.assertEquals("", oldValueEmptyString);
        Assert.assertEquals("newValue", attrWithEmptyStringValue.getValue());

        Attribute attrWithoutValueOrKey = new Attribute(null, null);
        String oldValueEmptyBoth = attrWithoutValueOrKey.setValue("newValue");
        Assert.assertNull(oldValueEmptyBoth);
        Assert.assertEquals("newValue", attrWithoutValueOrKey.getValue());
    }
}

// Add more test cases for other methods in the Attribute class here