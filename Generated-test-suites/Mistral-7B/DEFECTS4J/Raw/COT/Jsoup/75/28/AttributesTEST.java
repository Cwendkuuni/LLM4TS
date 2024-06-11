package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.internal.Normalizer.lowerCase;

/**
 * Test class for Attributes.java
 */
public class AttributesTest {

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();

        // Test get() and hasKey() methods
        attributes.put("testKey", "testValue");
        assertEquals("testValue", attributes.get("testKey"));
        assertTrue(attributes.hasKey("testKey"));
        assertFalse(attributes.hasKey("nonExistentKey"));

        // Test getIgnoreCase() method
        attributes.put("TestKey", "testValueIgnoredCase");
        assertEquals("testValueIgnoredCase", attributes.getIgnoreCase("testKey"));
        assertEquals("testValueIgnoredCase", attributes.getIgnoreCase("TestKey"));

        // Test put() method
        Attributes attributes2 = new Attributes();
        attributes2.put("testKey2", "testValue2");
        attributes.putAll(attributes2);
        assertEquals(2, attributes.size());
        assertEquals("testValue", attributes.get("testKey"));
        assertEquals("testValue2", attributes.get("testKey2"));

        // Test putIgnoreCase() method
        attributes.putIgnoreCase("TestKey2", "testValue3");
        assertEquals(3, attributes.size());
        assertEquals("testValue3", attributes.getIgnoreCase("TestKey2"));

        // Test put() method with boolean value
        attributes.put("booleanKey", "true");
        attributes.put("booleanKey", null);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());
        assertEquals(0, attributes.get("booleanKey").length());

        // Test remove() method
        attributes.remove("booleanKey");
        assertFalse(attributes.hasKey("booleanKey"));

        // Test removeIgnoreCase() method
        attributes.putIgnoreCase("testKeyIgnoreCase", "testValueIgnoreCase");
        attributes.removeIgnoreCase("testKeyIgnoreCase");
        assertFalse(attributes.hasKeyIgnoreCase("testKeyIgnoreCase"));

        // Test size() method
        assertEquals(0, attributes.size());
        attributes.put("testKey4", "testValue4");
        assertEquals(1, attributes.size());

        // Test addAll() method
        Attributes attributes3 = new Attributes();
        attributes3.put("testKey5", "testValue5");
        attributes.addAll(attributes3);
        assertEquals(2, attributes.size());

        // Test iterator() and asList() methods
        List<Attribute> list = attributes.asList();
        Iterator<Attribute> iterator = list.iterator();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("testKey", "testValue");
        expectedMap.put("testKey2", "testValue2");
        expectedMap.put("TestKey2", "testValue3");
        expectedMap.put("booleanKey", null);
        expectedMap.put("testKeyIgnoreCase", "testValueIgnoreCase");
        expectedMap.put("testKey4", "testValue4");
        expectedMap.put("testKey5", "testValue5");
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            String key = attribute.getKey();
            String value = attribute.getValue();
            String expectedKey = null;
            String expectedValue = null;
            for (Map.Entry<String, String> entry : expectedMap.entrySet()) {
                if (key.equalsIgnoreCase(entry.getKey())) {
                    expectedKey = entry.getKey();
                    expectedValue = entry.getValue();
                    break;
                }
            }
            assertEquals(expectedKey, key);
            assertEquals(expectedValue, value);
        }

        // Test dataset() method
        Map<String, String> dataset = attributes.dataset();
        assertEquals(6, dataset.size());
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("data-")) {
                String expectedValue = "";
                for (int i = key.length(); i < key.length() + "data".length(); i++) {
                    expectedValue += key.charAt(i) == '-' ? '_' : key.charAt(i);
                }
                assertEquals(expectedValue, entry.getValue());
            }
        }

        // Test html() method
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, null);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        String expectedHtml = " testKey=\"testValue\" TestKey=\"testValueIgnoreCase\" booleanKey data-booleankey=\"\" testKeyIgnoreCase=\"testValueIgnoreCase\" testKey4=\"testValue4\" testKey5=\"testValue5\"";
        assertEquals(expectedHtml, accum.toString());

        // Test toString() method
        assertEquals(expectedHtml, attributes.toString());

        // Test equals() and hashCode() methods
        Attributes attributes4 = new Attributes();
        attributes4.put("testKey", "testValue");
        assertTrue(attributes.equals(attributes));
        assertTrue(attributes.equals(attributes4));
        assertEquals(attributes.hashCode(), attributes.hashCode());
        assertEquals(attributes.hashCode(), attributes4.hashCode());

        // Test clone() method
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);

        // Test normalize() method
        attributes.normalize();
        List<Attribute> normalizedList = attributes.asList();
        for (Attribute attribute : normalizedList) {
            String key = attribute.getKey();
            assertEquals(lowerCase(key), key);
        }
    }
}