package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Dataset;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class for Attributes.java
 */
public class AttributesTest {

    /**
     * Test method for {@link Attributes#get(String)}
     */
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String retrievedValue = attributes.get("key");
        assertEquals("value", retrievedValue);

        String emptyValue = attributes.get("emptyKey");
        assertEquals("", emptyValue);
    }

    /**
     * Test method for {@link Attributes#getIgnoreCase(String)}
     */
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        String retrievedValue = attributes.getIgnoreCase("key");
        assertEquals("value", retrievedValue);

        String emptyValue = attributes.getIgnoreCase("KEY");
        assertEquals("", emptyValue);

        String nonExistentValue = attributes.getIgnoreCase("nonExistentKey");
        assertEquals("", nonExistentValue);
    }

    /**
     * Test method for {@link Attributes#put(String, String)}
     */
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();

        attributes.put("key", "value");
        attributes.put("key", "newValue");

        String retrievedValue = attributes.get("key");
        assertEquals("newValue", retrievedValue);
    }

    /**
     * Test method for {@link Attributes#put(String, boolean)}
     */
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));

        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    /**
     * Test method for {@link Attributes#put(Attribute)}
     */
    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();

        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        String retrievedValue = attributes.get("key");
        assertEquals("value", retrievedValue);
    }

    /**
     * Test method for {@link Attributes#remove(String)}
     */
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove("key");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key"));
    }

    /**
     * Test method for {@link Attributes#removeIgnoreCase(String)}
     */
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        attributes.removeIgnoreCase("key");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("Key"));
    }

    /**
     * Test method for {@link Attributes#hasKey(String)}
     */
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    /**
     * Test method for {@link Attributes#hasKeyIgnoreCase(String)}
     */
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    /**
     * Test method for {@link Attributes#size()}
     */
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    /**
     * Test method for {@link Attributes#addAll(Attributes)}
     */
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        assertEquals(4, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
        assertTrue(attributes1.hasKey("key4"));
    }

    /**
     * Test method for {@link Attributes#iterator()}
     */
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();

        assertTrue(iterator.hasNext());
        Attribute firstAttribute = iterator.next();
        assertEquals("key1", firstAttribute.getKey());

        assertTrue(iterator.hasNext());
        Attribute secondAttribute = iterator.next();
        assertEquals("key2", secondAttribute.getKey());

        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link Attributes#asList()}
     */
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributeList = attributes.asList();

        assertEquals(2, attributeList.size());
        assertTrue(attributeList.get(0).getKey().equals("key1"));
        assertTrue(attributeList.get(1).getKey().equals("key2"));
    }

    /**
     * Test method for {@link Attributes#dataset()}
     */
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Dataset dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));
    }

    /**
     * Test method for {@link Attributes#html()}
     */
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        assertEquals("<key1>value1</key1><key2>value2</key2>", html);
    }

    /**
     * Test method for {@link Attributes#toString()}
     */
    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String toString = attributes.toString();

        assertEquals("<attributes><key1>value1</key1><key2>value2</key2></attributes>", toString);
    }

    /**
     * Test method for equals()
     */
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        attributes3.put("key2", "value1");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    /**
     * Test method for hashCode()
     */
    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        attributes3.put("key2", "value1");

        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    /**
     * Test method for clone()
     */
    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());
        assertTrue(attributes.equals(clonedAttributes));
    }

    /**
     * Test edge cases and exception handling scenarios
     */
    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws IOException {
        Attributes attributes = new Attributes();
        Document document = new Document();
        attributes.html(document.outputSettings(), document.body());
    }
}