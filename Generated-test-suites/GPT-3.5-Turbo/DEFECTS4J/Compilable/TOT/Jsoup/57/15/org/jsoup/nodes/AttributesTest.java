package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        assertEquals("value1", attributes.get("test1"));
        assertEquals("value2", attributes.get("test2"));
        assertEquals("value3", attributes.get("test3"));
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        assertEquals("value1", attributes.getIgnoreCase("Test1"));
        assertEquals("value2", attributes.getIgnoreCase("Test2"));
        assertEquals("value3", attributes.getIgnoreCase("Test3"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");

        assertEquals("value1", attributes.get("test1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("test1", true);

        assertTrue(attributes.hasKey("test1"));
        assertTrue(attributes.get("test1").isEmpty());

        attributes.put("test2", false);
        assertFalse(attributes.hasKey("test2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");

        attributes.remove("test1");
        assertFalse(attributes.hasKey("test1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("Test2", "value2");

        attributes.removeIgnoreCase("tEsT1");

        assertFalse(attributes.hasKey("test1"));
        assertTrue(attributes.getIgnoreCase("Test2").isEmpty());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");

        assertTrue(attributes.hasKey("test1"));
        assertFalse(attributes.hasKey("test2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("Test2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("tEsT1"));
        assertTrue(attributes.hasKeyIgnoreCase("tEsT2"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        assertEquals(2, attributes.size());

        attributes.remove("test1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "value2");
        attributes2.put("test3", "value3");

        attributes1.addAll(attributes2);

        assertTrue(attributes1.hasKey("test1"));
        assertTrue(attributes1.hasKey("test2"));
        assertTrue(attributes1.hasKey("test3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        
        assertEquals(3, count);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        assertEquals(3, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");
        attributes.put("data-test3", "value3");

        assertEquals(3, attributes.dataset().size());
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        String expected = " test1=\"value1\" test2=\"value2\" test3=\"value3\"";
        assertEquals(expected, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        attributes1.put("test2", "value2");
        attributes1.put("test3", "value3");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "value1");
        attributes2.put("test2", "value2");
        attributes2.put("test3", "value3");

        assertTrue(attributes1.equals(attributes2));
        assertTrue(attributes2.equals(attributes1));

        attributes2.remove("test3");
        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        attributes1.put("test2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "value1");
        attributes2.put("test2", "value2");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        attributes1.put("test2", "value2");

        Attributes attributes2 = attributes1.clone();

        assertTrue(attributes1.equals(attributes2));
        assertTrue(attributes2.equals(attributes1));

        attributes2.remove("test2");
        assertFalse(attributes1.equals(attributes2));
    }

}