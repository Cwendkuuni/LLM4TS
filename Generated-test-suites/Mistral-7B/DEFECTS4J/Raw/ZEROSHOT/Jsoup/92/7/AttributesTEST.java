package org.junit.Test;

import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();

        // Test put method
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));

        // Test putIgnoreCase method
        attributes.putIgnoreCase("Key1", "Value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("", attributes.getIgnoreCase("key2"));

        // Test put boolean attribute
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());
        assertFalse(attributes.getIgnoreCase("booleanKey").isEmpty());

        // Test put method with existing key
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        // Test remove method
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        // Test removeIgnoreCase method
        attributes.put("Key1", "Value1");
        attributes.removeIgnoreCase("Key1");
        assertFalse(attributes.hasKey("Key1"));

        // Test hasKey method
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
        assertFalse(attributes.hasKey("key2"));

        // Test hasKeyIgnoreCase method
        attributes.put("KEY3", "Value3");
        assertTrue(attributes.hasKeyIgnoreCase("key3"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY3"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));

        // Test size method
        attributes.put("key4", "value4");
        assertEquals(4, attributes.size());

        // Test addAll method
        Attributes attributes2 = new Attributes();
        attributes2.put("key5", "value5");
        attributes.addAll(attributes2);
        assertEquals(5, attributes.size());
        assertTrue(attributes.hasKey("key5"));

        // Test iterator method
        List<Attribute> list = attributes.asList();
        for (Attribute attribute : list) {
            System.out.println(attribute.getKey() + "=" + attribute.getValue());
        }

        // Test dataset method
        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        // Test html method
        System.out.println(attributes.html());

        // Test equals method
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertTrue(attributes.equals(attributes3));

        // Test clone method
        Attributes attributes4 = attributes.clone();
        assertEquals(attributes, attributes4);

        // Test normalize method
        attributes.put("Test", "test");
        attributes.normalize();
        assertEquals("test", attributes.get("test"));

        // Test remove duplicate attribute
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals(1, attributes.size());

        // Test shouldCollapseAttribute method
        attributes.put("checked", "true");
        attributes.put("checked", "");
        attributes.put("checked", null);
        assertFalse(attributes.hasKey("checked"));

        // Test shouldCollapseAttribute method with document output settings
        // (not possible to test directly, as this code is not accessible)
    }
}