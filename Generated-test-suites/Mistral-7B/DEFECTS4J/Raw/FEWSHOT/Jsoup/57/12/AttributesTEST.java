package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.DataAttribute;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals("value", attributes.get("test"));
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        assertEquals("value", attributes.getIgnoreCase("test"));
        assertEquals("", attributes.getIgnoreCase("NONEEXISTENT"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.get("test").getValue().contains("false"));
    }

    @Test
    public void testPutSameAttribute() {
        Attributes attributes = new Attributes();
        Attribute attr = new Attribute("test", "value");
        attributes.put(attr);
        assertEquals("value", attributes.get("test").getValue());
    }

    @Test
    public void testPutDifferentAttributes() {
        Attributes attributes = new Attributes();
        Attribute attr1 = new Attribute("test1", "value1");
        Attribute attr2 = new Attribute("test2", "value2");
        attributes.put(attr1);
        attributes.put(attr2);
        assertEquals("value1", attributes.get("test1").getValue());
        assertEquals("value2", attributes.get("test2").getValue());
    }

    @Test
    public void testPutNull() {
        Attributes attributes = new Attributes();
        assertThrows(NullPointerException.class, () -> attributes.put(null));
    }

    @Test
    public void testPutEmptyString() {
        Attributes attributes = new Attributes();
        assertThrows(IllegalArgumentException.class, () -> attributes.put("", ""));
    }

    @Test
    public void testPutDataAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new DataAttribute("testdata", "value"));
        assertEquals("value", attributes.getIgnoreCase("testdata").getValue());
    }

    @Test
    public void testPutDataAttributeWithPrefix() {
        Attributes attributes = new Attributes();
        String dataKey = Attributes.dataPrefix + "testdata";
        attributes.put(new DataAttribute("testdata", "value"));
        assertEquals("value", attributes.getIgnoreCase(dataKey).getValue());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove("test");
        assertNull(attributes.get("test"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "Value");
        attributes.removeIgnoreCase("test");
        assertNull(attributes.getIgnoreCase("test"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        assertTrue(attributes.hasKeyIgnoreCase("test"));
        assertFalse(attributes.hasKeyIgnoreCase("NONEEXISTENT"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("test", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("test1", "value1");
        attributes2.put("test2", "value2");
        attributes1.addAll(attributes2);
        assertEquals("value1", attributes1.get("test1").getValue());
        assertEquals("value2", attributes1.get("test2").getValue());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        List<Attribute> list = attributes.asList();
        Iterator<Attribute> iterator = list.iterator();
        Attribute first = iterator.next();
        assertEquals("test1", first.getKey());
        assertEquals("value1", first.getValue());
        assertSame(first, iterator.next());
        assertNull(iterator.hasNext());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put(new DataAttribute("testdata1", "value1"));
        attributes.put(new DataAttribute("testdata2", "value2"));
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value1", dataset.get("testdata1"));
        assertEquals("value2", dataset.get("testdata2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" test=\"value\"");
        assertEquals(expectedHtml.toString(), attributes.html());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Attributes cloned = attributes.clone();
        assertEquals("value", cloned.get("test").getValue());
    }
}