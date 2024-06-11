import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3"));
        assertEquals("", attributes.get(""));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("Key2"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("Key3"));
        assertEquals("", attributes.getIgnoreCase(""));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        assertEquals("true", attributes.get("key1"));

        attributes.put("key2", false);
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");

        attributes.put(attribute);

        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("key1");

        assertFalse(attributes.hasKey("Key1"));
        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("Key1"));
        assertEquals("", attributes.get("key1"));
        assertEquals("value2", attributes.get("Key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey(""));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");

        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase(""));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());

        attributes.remove("key1");

        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes newAttributes = new Attributes();
        newAttributes.put("key2", "value2");

        attributes.addAll(newAttributes);

        assertTrue(attributes.hasKey("key2"));
        assertEquals("value2", attributes.get("key2"));
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
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String expected = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expected, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(1308942383, attributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
    }
}