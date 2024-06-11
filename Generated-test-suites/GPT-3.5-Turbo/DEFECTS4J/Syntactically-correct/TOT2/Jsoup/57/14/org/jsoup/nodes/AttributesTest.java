import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testSize() {
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("otherKey", "otherValue");
        attributes.addAll(otherAttributes);
        assertTrue(attributes.hasKey("otherKey"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "value");
        assertTrue(attributes.dataset().containsKey("key"));
        assertEquals("value", attributes.dataset().get("key"));
    }

    @Test
    public void testHtml() {
        attributes.put("key", "value");
        assertEquals(" key=\"value\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key", "value");
        assertEquals(attributes, otherAttributes);
    }

    @Test
    public void testHashCode() {
        assertEquals(attributes.hashCode(), attributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes cloneAttributes = attributes.clone();
        assertEquals(attributes, cloneAttributes);
    }
}