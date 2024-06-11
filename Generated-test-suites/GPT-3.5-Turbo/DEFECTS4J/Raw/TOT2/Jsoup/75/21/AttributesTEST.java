import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        attributes.put("key", "updatedValue");
        assertEquals("updatedValue", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKeyIgnoreCase("booleanKey"));
        assertEquals("", attributes.get("booleanKey"));
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKeyIgnoreCase("booleanKey"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Attributes attributes = new Attributes();
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        assertEquals("value", attributes.dataset().get("key"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        assertTrue(attributes.equals(clonedAttributes));
        assertEquals(attributes.hashCode(), clonedAttributes.hashCode());
    }

    @Test
    public void testNormalization() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.normalize();
        assertTrue(attributes.hasKey("key"));
    }

    // Add more test cases for additional methods if necessary

}