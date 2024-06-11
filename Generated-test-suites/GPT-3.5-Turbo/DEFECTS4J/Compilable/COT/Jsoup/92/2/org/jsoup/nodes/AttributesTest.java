package org.jsoup.nodes;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        // Test when attribute is not set
        assert(attributes.get("key1").equals(""));

        // Test when attribute is set
        attributes.put("key1", "value1");
        assert(attributes.get("key1").equals("value1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        // Test when attribute is not set
        assert(attributes.getIgnoreCase("key1").equals(""));

        // Test when attribute is set
        attributes.put("key1", "value1");
        assert(attributes.getIgnoreCase("KEY1").equals("value1"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        // Test when attribute does not exist
        attributes.put("key1", "value1");
        assert(attributes.get("key1").equals("value1"));

        // Test when attribute already exists
        attributes.put("key1", "value2");
        assert(attributes.get("key1").equals("value2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        // Test when attribute does not exist
        attributes.remove("key1");
        assert(attributes.get("key1").equals(""));

        // Test when attribute exists
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assert(attributes.get("key1").equals(""));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        // Test when attribute does not exist
        assert(!attributes.hasKey("key1"));

        // Test when attribute exists
        attributes.put("key1", "value1");
        assert(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        // Test when attribute does not exist
        assert(!attributes.hasKeyIgnoreCase("key1"));

        // Test when attribute exists
        attributes.put("key1", "value1");
        assert(attributes.hasKeyIgnoreCase("KEY1"));
    }
}