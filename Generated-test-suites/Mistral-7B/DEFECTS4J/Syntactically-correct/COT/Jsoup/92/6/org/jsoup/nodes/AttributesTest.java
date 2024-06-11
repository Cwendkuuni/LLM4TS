package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

    

    @Test
    public void testAttributesEmpty() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testAttributesBasic() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttributesNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAttributesNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", null);
    }

    @Test
    public void testAttributesCaseSensitive() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        assertFalse(attributes.hasKey("test"));
        assertTrue(attributes.hasKey("Test"));
        assertEquals("value", attributes.get("Test"));
    }

    @Test
    public void testAttributesIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        assertTrue(attributes.hasKeyIgnoreCase("test"));
        assertEquals("value", attributes.getIgnoreCase("Test"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAttributesInvalidIndex() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove(Integer.MAX_VALUE);
    }

    @Test
    public void testAttributesRemove() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testAttributesRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        attributes.removeIgnoreCase("test");
        assertFalse(attributes.hasKey("Test"));
    }

    @Test
    public void testAttributesPutMultiple() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAttributesPutOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        attributes.put("test", "value2");
        assertEquals("value2", attributes.get("test"));
    }

    @Test
    public void testAttributesPutIgnoreCaseOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value1");
        attributes.put("test", "value2");
        assertEquals("value2", attributes.getIgnoreCase("Test"));
    }

    @Test
    public void testAttributesAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(3, attributes3.size());
        assertTrue(attributes3.hasKey("test1"));
        assertTrue(attributes3.hasKey("test2"));
    }

    @Test
    public void testAttributesDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-test1"));
        assertTrue(dataset.containsKey("data-test2"));
    }

    @Test
    public void testAttributesHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        String html = attributes.html();
        assertTrue(html.contains(" test=\"value\""));
    }

    @Test
    public void testAttributesToString() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals(attributes.toString(), attributes.html());
    }

    @Test
    public void testAttributesEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("test1", "value1");
        attributes2.put("test1", "value1");
        attributes3.put("test2", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testAttributesClone() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertTrue(attributes.equals(clonedAttributes));
    }

    @Test
    public void testAttributesNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("TEST", "value");

        attributes.normalize();
        assertEquals("test", attributes.keys[0]);
    }

    
}