package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.DataAttribute;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        String result = attributes.get("testKey");
        assertEquals("testValue", result);

        String resultIgnoreCase = attributes.getIgnoreCase("TestKey");
        assertEquals("testValue", resultIgnoreCase);

        result = attributes.get("nonExistentKey");
        assertEquals("", result);

        attributes = new Attributes();
        assertNull(attributes.get("testKey"));
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        assertEquals("testValue", attributes.get("testKey"));

        String newValue = "newTestValue";
        attributes.put("testKey", newValue);

        assertEquals(newValue, attributes.get("testKey"));

        attributes = new Attributes();
        attributes.put("testKey", newValue);
        assertEquals(newValue, attributes.get("testKey"));
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", true);

        assertTrue(attributes.hasKey("testKey"));
        assertFalse(attributes.get("testKey").isDataAttribute());

        attributes = new Attributes();
        attributes.put("testKey", false);

        assertFalse(attributes.hasKey("testKey"));

        attributes = new Attributes();
        attributes.put("testKey", true);
        attributes.put("testKey", false);

        assertFalse(attributes.hasKey("testKey"));
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        String key = "testKey";
        Attribute attribute = new Attribute(key, "testValue");

        attributes.put(attribute);

        assertEquals("testValue", attributes.get(key));
        assertEquals(attribute, attributes.get(key));

        String newValue = "newTestValue";
        Attribute newAttribute = new Attribute(key, newValue);
        attributes.put(newAttribute);

        assertEquals(newValue, attributes.get(key));
        assertEquals(newAttribute, attributes.get(key));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        attributes.remove("testKey1");

        assertFalse(attributes.hasKey("testKey1"));
        assertNull(attributes.get("testKey1"));

        assertTrue(attributes.hasKey("testKey2"));
        assertEquals("testValue2", attributes.get("testKey2"));
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("TestKey1", "testValue1");
        attributes.put("TestKey2", "testValue2");

        attributes.removeIgnoreCase("testKey1");

        assertFalse(attributes.hasKey("TestKey1"));
        assertNull(attributes.get("TestKey1"));

        assertTrue(attributes.hasKey("TestKey2"));
        assertEquals("testValue2", attributes.get("TestKey2"));
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        assertTrue(attributes.hasKey("testKey1"));
        assertTrue(attributes.hasKey("testKey2"));

        assertFalse(attributes.hasKey("nonExistentKey"));

        String key = "testKey1";
        attributes.remove(key);

        assertFalse(attributes.hasKey(key));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        Attributes ignoringCaseAttributes = new Attributes();
        ignoringCaseAttributes.put("TestKey1", "testValue1");
        ignoringCaseAttributes.put("TestKey2", "testValue2");

        assertTrue(attributes.hasKeyIgnoreCase("testKey1"));
        assertTrue(ignoringCaseAttributes.hasKeyIgnoreCase("testKey1"));

        assertTrue(attributes.hasKeyIgnoreCase("TestKey1"));
        assertTrue(ignoringCaseAttributes.hasKeyIgnoreCase("TestKey1"));

        assertFalse(attributes.hasKeyIgnoreCase("TestKey3"));
        assertFalse(ignoringCaseAttributes.hasKeyIgnoreCase("TestKey3"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        attributes1.put("testKey2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("testKey3", "testValue3");
        attributes2.put("testKey4", "testValue4");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(4, attributes3.size());

        assertTrue(attributes3.hasKey("testKey1"));
        assertTrue(attributes3.hasKey("testKey2"));
        assertTrue(attributes3.hasKey("testKey3"));
        assertTrue(attributes3.hasKey("testKey4"));
    }

    @Test
    public void testSizeWhenNoAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
    }

    @Test
    public void testGetAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        List<Attribute> attributesList = attributes.asList();

        assertEquals(2, attributesList.size());

        Attribute firstAttribute = attributesList.get(0);
        assertEquals("testKey1", firstAttribute.getKey());
        assertEquals("testValue1", firstAttribute.getValue());

        Attribute secondAttribute = attributesList.get(1);
        assertEquals("testKey2", secondAttribute.getKey());
        assertEquals("testValue2", secondAttribute.getValue());
    }

    @Test
    public void testGetDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-testKey1", "testValue1");
        attributes.put("data-testKey2", "testValue2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());

        String key1 = "data-testKey1";
        assertEquals(key1, dataset.keySet().iterator().next());
        assertEquals("testValue1", dataset.get(key1));

        String key2 = "data-testKey2";
        assertEquals(key2, dataset.keySet().iterator().next());
        assertEquals("testValue2", dataset.get(key2));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" testKey1=\"testValue1\" testKey2=\"testValue2\"");

        String html = attributes.html();

        assertEquals(expectedHtml.toString(), html);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        attributes1.put("testKey2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("testKey1", "testValue1");
        attributes2.put("testKey2", "testValue2");

        Attributes attributes3 = new Attributes();
        attributes3.put("testKey1", "testValue1");
        attributes3.put("testKey3", "testValue3");

        Attributes emptyAttributes = new Attributes();

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(emptyAttributes));
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());

        for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
            Attribute attribute = entry.getValue();
            String key = entry.getKey();
            Attribute clonedAttribute = clonedAttributes.get(key);

            assertEquals(attribute.getKey(), clonedAttribute.getKey());
            assertEquals(attribute.getValue(), clonedAttribute.getValue());
            assertEquals(attribute.isDataAttribute(), clonedAttribute.isDataAttribute());
        }
    }

    @Test
    public void testGetDataKeyMethod() {
        String dataKey = Attributes.dataKey("testKey");

        assertEquals("data-testKey", dataKey);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullMethod() {
        Attributes attributes = new Attributes();
        attributes = null;
        attributes.get("testKey");
        fail("Expected NullPointerException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "testValue");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", null);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutBooleanNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", null);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDataAttributeNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put(new DataAttribute("testKey", null), "testValue");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDataAttributeNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, new DataAttribute("testKey", "testValue"));
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = SerializationException.class)
    public void testHtmlSerializationExceptionMethod() {
        Attributes attributes = new Attributes();
        try {
            attributes.html(null, null);
            fail("Expected SerializationException");
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Test
    public void testDatasetEmptyAttributes() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testDatasetEmptyDataset() {
        Attributes attributes = new Attributes();
        Attributes emptyDatasetAttributes = new Attributes();
        Map<String, String> dataset = emptyDatasetAttributes.dataset();
        attributes.putAll(emptyDatasetAttributes);

        assertEquals(0, dataset.size());
    }

    @Test
    public void testDatasetSingleAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-testKey", "testValue");
        Map<String, String> dataset = attributes.dataset();

        assertEquals(1, dataset.size());
        assertEquals("data-testKey", dataset.keySet().iterator().next());
        assertEquals("testValue", dataset.get("data-testKey"));
    }

    @Test
    public void testDatasetMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-testKey1", "testValue1");
        attributes.put("data-testKey2", "testValue2");
        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());

        String dataKey1 = "data-testKey1";
        assertEquals(dataKey1, dataset.keySet().iterator().next());
        assertEquals("testValue1", dataset.get(dataKey1));

        String dataKey2 = "data-testKey2";
        assertEquals(dataKey2, dataset.keySet().iterator().next());
        assertEquals("testValue2", dataset.get(dataKey2));
    }
}