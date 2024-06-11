package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // should not override existing key
        assertEquals(3, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        assertEquals("value1", attributes.get("Key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertFalse(attributes.get("key1").isEmpty());
        assertEquals(null, attributes.get("key1"));

        attributes.put("key1", false);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals(attribute, attributes.get(0));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKey("Key1"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        for (Attribute attribute : expectedList) {
            Attribute attributeFromIterator = iterator.next();
            assertEquals(attribute, attributeFromIterator);
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        assertEquals(expectedList, list);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Map<String, String> expectedMap = new HashMap<String, String>() {{
            put("data-key1", "value1");
            put("data-key2", "value2");
        }};

        assertEquals(expectedMap, dataset);
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("class", "class1 class2");
        attributes.put("id", "id1");
        attributes.put("data-custom", "data-custom-value");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" class=\"class1 class2\" id=\"id1\" data-custom=\"data-custom-value\"");

        assertEquals(expectedHtml.toString(), attributes.html());
    }
}