package org.example; // replace with your test package

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {
    

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        Assert.assertEquals("red", attributes.get("color"));
        Assert.assertEquals(EmptyString, attributes.get("size"));
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        Assert.assertEquals("red", attributes.getIgnoreCase("color"));
        Assert.assertEquals("red", attributes.getIgnoreCase("Color"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("size"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMethodWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.add(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMethodWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.add("key", null);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "large");
        Assert.assertEquals("red", attributes.get("color"));
        Assert.assertEquals("large", attributes.get("size"));
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        attributes.put("size", "large");
        attributes.putIgnoreCase("color", "RED");
        attributes.putIgnoreCase("Color", "RED");
        Assert.assertEquals("red", attributes.get("Color"));
        Assert.assertEquals("red", attributes.get("color"));
        Assert.assertEquals("large", attributes.get("size"));
    }

    @Test
    public void testPutBooleanMethodTrue() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        Assert.assertTrue(attributes.hasKey("checked"));
        Assert.assertEquals(EmptyString, attributes.get("checked"));
    }

    @Test
    public void testPutBooleanMethodFalse() {
        Attributes attributes = new Attributes();
        attributes.put("checked", false);
        Assert.assertTrue(attributes.hasKey("checked"));
        Assert.assertFalse(attributes.hasKey("checked"));
        Assert.assertEquals(EmptyString, attributes.get("checked"));
    }

    @Test
    public void testPutMethodWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("color", "blue");
        Assert.assertEquals("blue", attributes.get("color"));
    }

    @Test
    public void testPutMethodWithSameKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        attributes.put("Color", "blue");
        Assert.assertEquals("blue", attributes.get("Color"));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "large");
        attributes.remove("color");
        Assert.assertFalse(attributes.hasKey("color"));
        Assert.assertEquals("large", attributes.get("size"));
    }

    @Test
    public void testRemoveMethodWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.remove(null);
        Assert.assertEquals("red", attributes.get("color"));
    }

    @Test
    public void testContainsKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        Assert.assertTrue(attributes.hasKey("color"));
        Assert.assertFalse(attributes.hasKey("size"));
    }

    @Test
    public void testContainsKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Color"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("color"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("size"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "large");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("color", "red");
        attributes1.put("size", "large");
        attributes2.put("style", "bold");

        attributes1.addAll(attributes2);
        Assert.assertEquals(3, attributes1.size());
        Assert.assertEquals("red", attributes1.get("color"));
        Assert.assertEquals("large", attributes1.get("size"));
        Assert.assertEquals("bold", attributes1.get("style"));
    }

    @Test
    public void testGetDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-color", "red");
        attributes.put("data-size", "large");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("red", dataset.get("color"));
        Assert.assertEquals("large", dataset.get("size"));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        attributes.put("style", "color: blue; background-color: red;");

        String html = attributes.html();
        String expectedHtml = " class=\"testClass\" style=\"color: blue; background-color: red;\"";
        Assert.assertEquals(expectedHtml, html);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithException() {
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        attributes.put("style", "color: blue; background-color: red;");
        attributes.put("data-test", "test");

        try {
            attributes.html();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Test
    public void testToStringMethod() {
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        attributes.put("style", "color: blue; background-color: red;");

        String toString = attributes.toString();
        String expectedToString = "Attributes{size=2, keys=[class, style], vals=[testClass, color: blue; background-color: red;]}";
        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("color", "red");
        attributes2.put("color", "red");
        attributes3.put("size", "large");

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("color", "red");
        attributes2.put("color", "red");

        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "large");

        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Assert.assertEquals(attributes.get("color"), clonedAttributes.get("color"));
        Assert.assertEquals(attributes.get("size"), clonedAttributes.get("size"));
    }

    @Test
    public void testNormalizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        attributes.put("size", "Large");

        attributes.normalize();
        Assert.assertEquals("red", attributes.get("color"));
        Assert.assertEquals("large", attributes.get("size"));
    }

    @Test
    public void testRemoveDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("color", "blue");

        attributes.removeDuplicateAttributes();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("red", attributes.get("color"));
    }

    @Test
    public void testRemoveDuplicateAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        attributes.put("Color", "blue");

        attributes.removeDuplicateAttributesIgnoreCase();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("red", attributes.get("Color"));
    }

    
}