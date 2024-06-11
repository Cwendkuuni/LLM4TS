
import static org.junit.Assert.*;
import org.junit.*;

public class AttributesTest {
    
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");
        attributes.put("Key", "anotherValue");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("anotherValue", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertEquals("true", attributes.get("key"));
        attributes.put("key", false);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");
        attributes.put("Key", "anotherValue");
        attributes.removeIgnoreCase("key");
        assertEquals("anotherValue", attributes.get("Key"));
        attributes.removeIgnoreCase("Key");
        assertEquals("", attributes.get("Key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key", "value");
        attributes.addAll(newAttributes);
        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        StringBuilder builder = new StringBuilder();
        for (Attribute attribute : attributes) {
            builder.append(attribute.getKey()).append(attribute.getValue());
        }
        assertEquals("key1value1key2value2", builder.toString());
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        StringBuilder builder = new StringBuilder();
        for (Attribute attribute : attributes.asList()) {
            builder.append(attribute.getKey()).append(attribute.getValue());
        }
        assertEquals("key1value1key2value2", builder.toString());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals(2, attributes.dataset().size());
        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
    }

    @Test
    public void testDatasetIterator() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : attributes.dataset().entrySet()) {
            builder.append(entry.getKey()).append(entry.getValue());
        }
        assertEquals("key1value1key2value2", builder.toString());
    }

}
