package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));

        assertEquals("", attributes.get("nonexistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));

        assertEquals("", attributes.getIgnoreCase("nonexistentkey"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        attributes.put("key2", false);

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertEquals("", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));

        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertFalse(attributes.equals(attributes2));

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes attributes2 = attributes.clone();

        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testNormalize() {
        attributes.put("Key1", "value1");
        attributes.put("KEY2", "value2");

        attributes.normalize();

        assertFalse(attributes.hasKey("Key1"));
        assertFalse(attributes.hasKey("KEY2"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        attributes.put("key3", "value3");

        Map<String, String> expectedDataset = new HashMap<>();
        expectedDataset.put("key1", "value1");
        expectedDataset.put("key2", "value2");

        assertEquals(expectedDataset, attributes.dataset());
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

}