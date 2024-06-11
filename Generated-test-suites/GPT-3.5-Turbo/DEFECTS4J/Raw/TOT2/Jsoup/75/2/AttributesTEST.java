import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2")); // edge case: key not found
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutIgnorecase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1")); // edge case: case insensitive
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("key1")); // edge case: boolean attribute
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1")); // edge case: remove attribute
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(" key=\"value\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals(133419503, attributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clone = attributes.clone();
        assertEquals(attributes.get("key1"), clone.get("key1"));
    }
}