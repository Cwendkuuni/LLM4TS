import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key2", false);

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        assertEquals(attributes1, attributes2);
    }
}