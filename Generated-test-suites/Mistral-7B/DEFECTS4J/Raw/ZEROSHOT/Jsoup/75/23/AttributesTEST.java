package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

    @Test
    public void testInitialization() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertNull(attributes.get("test"));
    }

    @Test
    public void testSettingGettingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Assert.assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testSettingGettingEmptyAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "");
        Assert.assertEquals("", attributes.get("test"));
    }

    @Test
    public void testSettingGettingBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", false);
        Assert.assertFalse(attributes.get("test").isEmpty());
        Assert.assertNull(attributes.get("test"));
    }

    @Test
    public void testSettingGettingCaseInsensitiveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        Assert.assertEquals("value", attributes.getIgnoreCase("test"));
    }

    @Test
    public void testSettingMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        Assert.assertEquals("value1", attributes.get("test1"));
        Assert.assertEquals("value2", attributes.get("test2"));
    }

    @Test
    public void testSettingDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        attributes.put("test", "value2");
        Assert.assertEquals("value2", attributes.get("test"));
    }

    @Test
    public void testSettingDataAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "value");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value", dataset.get("data-test"));
    }

    @Test
    public void testSettingDataAttributeWithPrefix() {
        Attributes attributes = new Attributes();
        attributes.put("data-prefix-test", "value");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value", dataset.get("data-prefix-test"));
    }

    @Test
    public void testRemovingAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove("test");
        Assert.assertNull(attributes.get("test"));
    }

    @Test
    public void testRemovingCaseInsensitiveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        attributes.removeIgnoreCase("test");
        Assert.assertNull(attributes.get("test"));
    }

    @Test
    public void testRemovingNonExistentAttribute() {
        Attributes attributes = new Attributes();
        attributes.remove("test");
        Assert.assertNull(attributes.get("test"));
    }

    @Test
    public void testCheckingForExistenceOfAttribute() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("test"));
        attributes.put("test", "value");
        Assert.assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testCheckingForExistenceOfCaseInsensitiveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
    }

    @Test
    public void testCheckingForExistenceOfDataAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "value");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertTrue(dataset.containsKey("data-test"));
    }

    @Test
    public void testCheckingForNonExistenceOfAttribute() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testCheckingForNonExistenceOfCaseInsensitiveAttribute() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    @Test
    public void testCheckingForNonExistenceOfDataAttribute() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        Assert.assertFalse(dataset.containsKey("test"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testSizeAfterRemovingAnAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.remove("test1");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testIteratingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> expectedList = Arrays.asList(new Attribute("test1", "value1"),
                new Attribute("test2", "value2"));
        int i = 0;
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            Assert.assertEquals(expectedList.get(i).getValue(), attribute.getValue());
            i++;
        }
    }

    @Test
    public void testAddingAllAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        Iterator<Attribute> iterator = attributes3.iterator();
        List<Attribute> expectedList = new ArrayList<>();
        expectedList.addAll(Arrays.asList(new Attribute("test1", "value1"), new Attribute("test2", "value2")));
        int i = 0;
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            Assert.assertEquals(expectedList.get(i).getValue(), attribute.getValue());
            i++;
        }
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Assert.assertEquals("test=\"value\"", attributes.toString());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Iterator<Attribute> iterator = clonedAttributes.iterator();
        Attribute attribute = iterator.next();
        Assert.assertEquals(attributes.get("test"), attribute.getValue());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("test1", "value1");
        attributes2.put("test1", "value1");
        attributes3.put("test2", "value2");
        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("test1", "value1");
        attributes2.put("test1", "value1");
        attributes3.put("test2", "value2");
        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }
}