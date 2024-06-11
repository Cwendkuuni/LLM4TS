import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.SerializationException;
import org.jsoup.nodes.Attributes;

import java.util.List;
import java.util.Map;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testGetWithNonExistingKey() {
        assertEquals("", attributes.get("nonExistingKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
    }

    @Test
    public void testGetIgnoreCaseWithNonExistingKey() {
        assertEquals("", attributes.getIgnoreCase("nonExistingKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutBooleanValue() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertEquals("true", attributes.get("key1"));

        attributes.put("key2", false);
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes.Attribute attribute = new Attributes.Attribute("key1", "value1");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonExistingKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistingKey"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        attributes.remove("key2");
        assertEquals(1, attributes.size());
        attributes.remove("key1");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attributes.Attribute attribute : attributes) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attributes.Attribute> list = attributes.asList();

        assertEquals(2, list.size());
        assertEquals("key1", list.get(0).getKey());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("key2", list.get(1).getKey());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        assertEquals(" key1=\"value1\" key2=\"value2\"", html);
    }

    @Test
    public void testEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testNotEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key3", "value3");

        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
    }

    @Test
    public void testCloneWithEmptyAttributes() {
        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
    }

    @Test
    public void testCloneWithNullAttributes() {
        attributes = null;
        Attributes clone = (Attributes) attributes.clone();

        assertNull(clone);
    }
}

