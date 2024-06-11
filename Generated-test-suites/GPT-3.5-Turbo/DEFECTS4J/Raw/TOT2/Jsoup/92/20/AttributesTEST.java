import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key4", true);
        assertTrue(attributes.hasKeyIgnoreCase("key4"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key5", "value5");
        attributes.remove("key5");
        assertFalse(attributes.hasKey("key5"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key6", "value6");

        Attributes attributes2 = new Attributes();
        attributes2.put("key7", "value7");

        attributes1.addAll(attributes2);
        assertTrue(attributes1.hasKey("key7"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        assertTrue(attributes.dataset().containsKey("data-key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key8", "value8");
        assertEquals(" key8=\"value8\"", attributes.html());
    }

}