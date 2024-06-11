
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        assertEquals("value1", attributes.getIgnoreCase("attr1"));
        assertEquals("value2", attributes.getIgnoreCase("attr2"));
        assertEquals("value2", attributes.getIgnoreCase("Attr2"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testPut() {
        attributes.put("attr1", "value1");
        assertEquals("value1", attributes.get("attr1"));
        attributes.put("attr1", "value2");
        assertEquals("value2", attributes.get("attr1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("attr1", true);
        assertEquals("attr1", attributes.get("attr1"));
        attributes.put("attr1", false);
        assertEquals("", attributes.get("attr1"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");

        attributes.put(attr1);

        assertEquals("value1", attributes.get("attr1"));
        assertNull(attributes.get("attr2"));

        attributes.put(attr2);

        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
    }

    @Test
    public void testRemove() {
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        attributes.remove("attr1");

        assertNull(attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));

        attributes.remove("attr2");

        assertNull(attributes.get("attr1"));
        assertNull(attributes.get("attr2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("attr1", "value1");
        attributes.put("Attr2", "value2");

        attributes.removeIgnoreCase("AttR1");

        assertNull(attributes.get("attr1"));
        assertEquals("value2", attributes.get("Attr2"));

        attributes.removeIgnoreCase("ATTr2");

        assertNull(attributes.get("attr1"));
        assertNull(attributes.get("attr2"));
    }

    @Test
    public void testHasKey() {
        attributes.put("attr1", "value1");
        assertTrue(attributes.hasKey("attr1"));
        assertFalse(attributes.hasKey("attr2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("attr1", "value1");
        attributes.put("Attr2", "value2");
        assertTrue(attributes.hasKeyIgnoreCase("attr1"));
        assertTrue(attributes.hasKeyIgnoreCase("attR1"));
        assertTrue(attributes.hasKeyIgnoreCase("Attr2"));
        assertFalse(attributes.hasKeyIgnoreCase("attr2"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("attr1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("attr2", "value2");
        assertEquals(2, attributes.size());
        attributes.remove("attr1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("attr1", "value1");
        incoming.put("attr2", "value2");

        attributes.addAll(incoming);

        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
    }

    @Test
    public void testIterator() {
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        int count = 0;
        for (Attribute attribute : attributes.asList()) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testDataset() {
        attributes.put("data-attr1", "value1");
        attributes.put("data-attr2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals("value1", dataset.get("attr1"));
        assertEquals("value2", dataset.get("attr2"));
    }

    @Test
    public void testHtml() {
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        String html = attributes.html();

        assertTrue(html.contains("attr1=\"value1\""));
        assertTrue(html.contains("attr2=\"value2\""));
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("attr1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("attr1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("attr2", "value2");

        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("attr1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("attr1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("attr2", "value2");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        Attributes clone = attributes.clone();

        assertEquals("value1", clone.get("attr1"));
        assertEquals("value2", clone.get("attr2"));
    }
}
