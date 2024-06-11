package org.jsoup.nodes;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }
}