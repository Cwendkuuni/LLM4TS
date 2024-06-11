package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class AttributesTest {

    private Attributes attributes;
    private Map<String, Attribute> expectedAttributes;

    public AttributesTest(Map<String, Attribute> expectedAttributes) {
        this.expectedAttributes = expectedAttributes;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                // Test empty attributes
                new HashMap<String, Attribute>() {},

                // Test attributes with various keys and values
                new HashMap<String, Attribute>() {{
                    put("id", new Attribute("id", "1"));
                    put("class", new Attribute("class", "foo bar"));
                    put("title", new Attribute("title", "Test Title"));
                }},

                // Test attributes with duplicated keys
                new HashMap<String, Attribute>() {{
                    put("id", new Attribute("id", "1"));
                    put("id", new Attribute("id", "2"));
                }},

                // Test attributes with duplicated keys (case insensitive)
                new HashMap<String, Attribute>() {{
                    put("ID", new Attribute("id", "1"));
                    put("ID", new Attribute("id", "2"));
                }},

                // Test attributes with data-* keys
                new LinkedHashMap<String, Attribute>() {{
                    put("data-foo", new Attribute("data-foo", "bar"));
                    put("data-bar", new Attribute("data-bar", "baz"));
                }},

                // Test removing an attribute
                new HashMap<String, Attribute>() {{
                    put("id", new Attribute("id", "1"));
                    remove("id");
                }},

                // Test removing an attribute by case insensitive key
                new HashMap<String, Attribute>() {{
                    put("ID", new Attribute("ID", "1"));
                    removeIgnoreCase("id");
                }},

                // Test removing an attribute that does not exist
                new HashMap<String, Attribute>() {{
                    remove("nonExistentAttribute");
                }},

                // Test removing all attributes
                new HashMap<String, Attribute>() {{
                    clear();
                }},
        });
    }

    @Test
    public void testGet() {
        String attributeValue = attributes.get("class");
        assertEquals(expectedAttributes.get("class").getValue(), attributeValue);
    }

    @Test
    public void testGetIgnoreCase() {
        String attributeValue = attributes.getIgnoreCase("CLASS");
        assertEquals(expectedAttributes.get("class").getValue(), attributeValue);
    }

    @Test
    public void testPut() {
        attributes.put("foo", "bar");
        Attribute attribute = attributes.get("foo");
        assertEquals("bar", attribute.getValue());
    }

    @Test
    public void testPutBoolean() {
        attributes.put("foo", true);
        Attribute attribute = attributes.get("foo");
        assertTrue(attribute instanceof BooleanAttribute);
    }

    @Test
    public void testPutReplace() {
        attributes.put("foo", "bar");
        attributes.put("foo", "baz");
        Attribute attribute = attributes.get("foo");
        assertEquals("baz", attribute.getValue());
    }

    @Test
    public void testPutReplaceWithNull() {
        attributes.put("foo", "bar");
        attributes.put("foo", null);
        assertNull(attributes.get("foo"));
    }

    @Test
    public void testPutReplaceWithDifferentAttribute() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put("foo", new Attribute("foo", "baz"));
        Attribute attribute = attributes.get("foo");
        assertEquals("baz", attribute.getValue());
    }

    @Test
    public void testPutReplaceWithDifferentClass() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put("foo", new BooleanAttribute("foo"));
        assertTrue(attributes.get("foo") instanceof BooleanAttribute);
    }

    @Test
    public void testPutReplaceWithSameClassDifferentValue() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put("foo", new Attribute("foo", "baz"));
        Attribute attribute = attributes.get("foo");
        assertEquals("baz", attribute.getValue());
    }

    @Test
    public void testPutReplaceWithSameKeyDifferentClass() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put("foo", new Attribute("baz", "baz"));
        Attribute attribute = attributes.get("foo");
        assertEquals("baz", attribute.getKey());
    }

    @Test
    public void testPutReplaceWithSameClassSameValue() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put("foo", new Attribute("foo", "bar"));
        Attribute attribute = attributes.get("foo");
        assertEquals("bar", attribute.getValue());
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.put("FOO", "bar");
        Attribute attribute = attributes.get("foo");
        assertEquals("bar", attribute.getValue());
    }

    @Test
    public void testPutIgnoreCaseWithSameKeyDifferentClass() {
        attributes.put("FOO", new Attribute("foo", "bar"));
        attributes.put("FOO", new StringAttribute("foo", "baz"));
        Attribute attribute = attributes.get("foo");
        assertEquals("baz", attribute.getValue());
    }

    @Test
    public void testPutIgnoreCaseWithSameKeySameClass() {
        attributes.put("FOO", new Attribute("foo", "bar"));
        attributes.put("FOO", new Attribute("foo", "bar"));
        Attribute attribute = attributes.get("foo");
        assertEquals("bar", attribute.getValue());
    }

    @Test
    public void testPutIgnoreCaseWithDifferentKeys() {
        attributes.put("FOO", new Attribute("foo", "bar"));
        attributes.put("BAR", new Attribute("bar", "baz"));
        Attribute attribute = attributes.get("foo");
        assertEquals("bar", attribute.getValue());
    }

    @Test
    public void testPutIgnoreCaseWithSameKeySameClassDifferentValue() {
        attributes.put("FOO", new Attribute("foo", "bar"));
        attributes.put("FOO", new Attribute("foo", "baz"));
        Attribute attribute = attributes.get("foo");
        assertEquals("baz", attribute.getValue());
    }

    @Test
    public void testPutIgnoreCaseWithSameKeyDifferentClassDifferentValue() {
        attributes.put("FOO", new Attribute("foo", "bar"));
        attributes.put("FOO", new BooleanAttribute("foo"));
        Attribute attribute = attributes.get("foo");
        assertTrue(attribute instanceof BooleanAttribute);
    }

    @Test
    public void testPutNull() {
        try {
            attributes.put(null, "bar");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutNullValue() {
        attributes.put("foo", null);
        assertNull(attributes.get("foo"));
    }

    @Test
    public void testPutNullValueWithDifferentClass() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put("foo", null);
        assertNull(attributes.get("foo"));
    }

    @Test
    public void testPutNullKey() {
        try {
            attributes.put(null, "bar");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutNullKeyWithDifferentClass() {
        try {
            attributes.put(null, new Attribute("foo", "bar"));
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutNullKeyWithSameClass() {
        try {
            attributes.put(null, new Attribute("foo", "bar"));
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutIgnoringNullKey() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put(null, new Attribute("bar", "baz"));
        Attribute attribute = attributes.get("foo");
        assertEquals("bar", attribute.getValue());
    }

    @Test
    public void testPutIgnoringNullValue() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put("foo", null);
        Attribute attribute = attributes.get("foo");
        assertNull(attribute);
    }

    @Test
    public void testPutIgnoringNullKeyValue() {
        attributes.put("foo", new Attribute("foo", "bar"));
        attributes.put(null, null);
        Attribute attribute = attributes.get("foo");
        assertEquals("bar", attribute.getValue());
    }

    @Test
    public void testPutWithCustomAttributeType() {
        Attribute attribute = new CustomAttribute("custom", "value");
        attributes.put("foo", attribute);
        Attribute retrievedAttribute = attributes.get("foo");
        assertEquals(attribute, retrievedAttribute);
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("class"));
        assertFalse(attributes.hasKey("nonExistentAttribute"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("CLASS"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentAttribute"));
    }

    @Test
    public void testSize() {
        assertEquals(expectedAttributes.size(), attributes.size());
    }

    @Test
    public void testAddAll() {
        Map<String, Attribute> otherAttributes = new HashMap<String, Attribute>() {{
            put("foo", new Attribute("foo", "bar"));
            put("bar", new Attribute("bar", "baz"));
        }};
        attributes.addAll(otherAttributes);
        List<Attribute> attributesList = attributes.asList();
        List<Attribute> otherAttributesList = otherAttributes.values();
        for (int i = 0; i < attributesList.size(); i++) {
            Attribute expectedAttribute = otherAttributesList.get(i);
            Attribute actualAttribute = attributesList.get(i);
            assertEquals(expectedAttribute.getKey(), actualAttribute.getKey());
            assertEquals(expectedAttribute.getValue(), actualAttribute.getValue());
        }
    }

    @Test
    public void testRemove() {
        String removedAttributeValue = attributes.remove("class");
        assertNull(attributes.get("class"));
        assertEquals("foo bar", removedAttributeValue);
    }

    @Test
    public void testRemoveIgnoreCase() {
        String removedAttributeValue = attributes.removeIgnoreCase("CLASS");
        assertNull(attributes.get("CLASS"));
        assertEquals("foo bar", removedAttributeValue);
    }

    @Test
    public void testRemoveNonexistentAttribute() {
        assertNull(attributes.remove("nonExistentAttribute"));
    }

    @Test
    public void testRemoveAll() {
        attributes.clear();
        assertNull(attributes.get("class"));
    }

    @Test
    public void testAsList() {
        List<Attribute> attributesList = attributes.asList();
        List<Attribute> expectedAttributesList = new ArrayList<Attribute>(expectedAttributes.values());
        for (int i = 0; i < attributesList.size(); i++) {
            Attribute expectedAttribute = expectedAttributesList.get(i);
            Attribute actualAttribute = attributesList.get(i);
            assertEquals(expectedAttribute.getKey(), actualAttribute.getKey());
            assertEquals(expectedAttribute.getValue(), actualAttribute.getValue());
        }
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        Map<String, String> expectedDataset = new HashMap<String, String>();
        for (Map.Entry<String, Attribute> entry : expectedAttributes.entrySet()) {
            if (entry.getValue().isDataAttribute()) {
                expectedDataset.put(entry.getKey().substring(dataPrefix.length()), entry.getValue().getValue());
            }
        }
        assertEquals(expectedDataset, dataset);
    }

    @Test
    public void testHtml() throws IOException {
        StringBuilder expectedHtml = new StringBuilder();
        StringBuilder actualHtml = new StringBuilder();
        attributes.html(expectedHtml, new Document.OutputSettings());
        attributes.html(actualHtml, new Document.OutputSettings());
        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testToString() {
        assertEquals(attributes.toString(), attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("class", new Attribute("class", "foo bar"));
        assertFalse(attributes.equals(otherAttributes));

        otherAttributes = new Attributes();
        otherAttributes.putAll(expectedAttributes);
        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        assertEquals(attributes.hashCode(), expectedAttributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
        for (Map.Entry<String, Attribute> entry : expectedAttributes.entrySet()) {
            Attribute expectedAttribute = entry.getValue();
            Attribute clonedAttribute = clonedAttributes.get(entry.getKey());
            assertEquals(expectedAttribute, clonedAttribute);
        }
    }
}