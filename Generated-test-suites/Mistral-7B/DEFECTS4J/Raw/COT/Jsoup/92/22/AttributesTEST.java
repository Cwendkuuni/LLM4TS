package org.jsoup.nodes; // update the package name if necessary

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {
    // Basic test cases for each method

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(Attributes.EmptyString, attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        assertEquals("VALUE", attributes.getIgnoreCase("key"));
        assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("NONEEXISTENT"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(Attributes.EmptyString, attributes.get("nonexistent"));

        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "VALUE");
        assertEquals("VALUE", attributes.getIgnoreCase("key"));
        assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("NONEEXISTENT"));

        attributes.putIgnoreCase("KEY", "newValue");
        assertEquals("newValue", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.get("key").equals(Attributes.EmptyString));
        assertNull(attributes.get("key")[0]);

        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertNull(attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "VALUE");
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKeyIgnoreCase("KEY"));
        assertNull(attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonexistent"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY"));
        attributes.putIgnoreCase("KEY", "value");
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("KEY"));
        attributes.putIgnoreCase("KEY", "value");
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes empty = new Attributes();
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        empty.addAll(attributes1);
        assertEquals(2, empty.size());
        assertTrue(empty.hasKey("key1"));
        assertTrue(empty.hasKey("key2"));
        assertEquals("value1", empty.get("key1"));
        assertEquals("value2", empty.get("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> entryList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            entryList.add(entry);
        }

        assertEquals(2, attributes.size());
        assertEquals(entryList.size(), attributes.size());
        assertEquals(entryList, attributes.asList());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        String html = attributes.html();
        attributes.html(sb, new Document.OutputSettings());

        assertEquals(html, sb.toString());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals(attributes.html(), attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value1");

        Attributes attributes5 = new Attributes();
        attributes5.put("key1", "value1");
        attributes5.put("key2", "value2");

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
        assertNotEquals(attributes1, attributes4);
        assertNotEquals(attributes1, attributes5);
        assertNotEquals(attributes1, null);
        assertNotEquals(attributes1, new Object());
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value1");

        Attributes attributes5 = new Attributes();
        attributes5.put("key1", "value1");
        attributes5.put("key2", "value2");

        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes4.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes5.hashCode());
        assertNotEquals(attributes1.hashCode(), null.hashCode());
        assertNotEquals(attributes1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Attributes cloned = attributes.clone();
        assertEquals(attributes.size(), cloned.size());
        assertEquals(attributes.get("key"), cloned.get("key"));
    }

    // Edge cases and exception handling test cases

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.remove(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "newValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "VALUE");
        attributes.putIgnoreCase("KEY", "newValue");
    }
}