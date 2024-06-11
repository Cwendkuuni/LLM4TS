import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
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
    public void testPutAndGet() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutAndRemove() {
        attributes.put("key2", "value2");
        assertTrue(attributes.hasKey("key2"));
        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("key3", true);
        assertTrue(attributes.hasKey("key3"));
        assertEquals("", attributes.get("key3"));
        attributes.put("key4", false);
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testPutAttributeObject() {
        Attribute attribute = new Attribute("key5", "value5");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key5"));
        assertEquals("value5", attributes.get("key5"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key6", "value6");
        assertTrue(attributes.hasKeyIgnoreCase("key6"));
        attributes.removeIgnoreCase("key6");
        assertFalse(attributes.hasKeyIgnoreCase("key6"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Attributes attributesCopy = attributes.clone();
        assertEquals(attributes, attributesCopy);
        assertEquals(attributes.hashCode(), attributesCopy.hashCode());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "data-value");
        assertEquals("data-value", attributes.dataset().get("key"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String expectedHtml = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expectedHtml, attributes.html());
    }

    @Test(expected = RuntimeException.class)
    public void testCloneException() {
        attributes.clone();
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int count = 0;
        for (Attribute attr : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAddAll() {
        attributes.put("key1", "value1");
        Attributes newAttributes = new Attributes();
        newAttributes.put("key2", "value2");
        newAttributes.put("key3", "value3");
        attributes.addAll(newAttributes);
        assertEquals(3, attributes.size());
    }

    @Test
    public void testNormalize() {
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        attributes.normalize();
        assertFalse(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKey("key1"));
    }
}