package org.jsoup.nodes; // update the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    // Test instance
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGetEmptyAttributes() {
        assertNull(attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetCaseSensitive() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutCaseSensitive() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutReplace() {
        attributes.put("key", "existingValue");
        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutNullValue() {
        attributes.put("key", null);
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key", true);
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyCaseSensitive() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKeyNonExistent() {
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testSizeZero() {
        assertEquals(0, attributes.size());
    }

    @Test
    public void testSizeNonZero() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllEmpty() {
        Attributes emptyAttributes = new Attributes();
        attributes.addAll(emptyAttributes);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAllNonEmpty() {
        Attributes incomingAttributes = new Attributes();
        incomingAttributes.put("key1", "value1");
        attributes.addAll(incomingAttributes);
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );

        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            actualList.add(entry);
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        List<Attribute> actualList = attributes.asList();

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDatasetEmpty() {
        Map<String, String> expectedDataset = Collections.emptyMap();
        Map<String, String> actualDataset = attributes.dataset();
        assertEquals(expectedDataset, actualDataset);
    }

    @Test
    public void testDatasetNonEmpty() {
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> expectedDataset = new LinkedHashMap<>() {{
            put("dataKey1", "dataValue1");
            put("dataKey2", "dataValue2");
        }};

        Map<String, String> actualDataset = attributes.dataset();

        assertEquals(expectedDataset, actualDataset);
    }

    @Test
    public void testHtmlEmpty() {
        assertEquals("", attributes.html());
    }

    @Test
    public void testHtmlNonEmpty() {
        attributes.put("key", "value");
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key=\"value\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, null);

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testToString() {
        attributes.put("key", "value");

        String expectedToString = "key=\"value\"";
        String actualToString = attributes.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEqualsNull() {
        assertFalse(attributes.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        assertTrue(attributes.equals(attributes));
    }

    @Test
    public void testEqualsSameContentsButDifferentInstance() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key", "value");
        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testEqualsDifferentContents() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("differentKey", "differentValue");
        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        attributes.put("key", "value");
        int expectedHashCode = 31 * (31 + attributes.hashCode());
        int actualHashCode = attributes.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testCloneEmpty() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(0, clonedAttributes.size());
    }

    @Test
    public void testCloneNonEmpty() {
        attributes.put("key", "value");

        Attributes clonedAttributes = attributes.clone();
        assertEquals("value", clonedAttributes.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        attributes.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKey() {
        attributes.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseNullKey() {
        attributes.removeIgnoreCase(null);
    }
}