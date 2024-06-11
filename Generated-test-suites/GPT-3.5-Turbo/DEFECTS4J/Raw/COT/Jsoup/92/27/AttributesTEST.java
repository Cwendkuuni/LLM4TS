import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
        assertEquals(3, attributes.size());
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("KEY1", "updatedValue");
        assertEquals("updatedValue", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY3"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key3", "value3");
        newAttributes.put("key4", "value4");

        attributes.addAll(newAttributes);

        assertEquals("value3", attributes.get("key3"));
        assertEquals("value4", attributes.get("key4"));
        assertEquals(4, attributes.size());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(attributes.size(), count);
    }

    @Test
    public void testAsList() {
        assertEquals(attributes.size(), attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }

    @Test
    public void testHtml() {
        String expectedHtml = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expectedHtml, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributesCopy = new Attributes();
        attributesCopy.put("key1", "value1");
        attributesCopy.put("key2", "value2");

        assertTrue(attributes.equals(attributesCopy));
    }

    @Test
    public void testHashCode() {
        Attributes attributesCopy = new Attributes();
        attributesCopy.put("key1", "value1");
        attributesCopy.put("key2", "value2");

        assertEquals(attributes.hashCode(), attributesCopy.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertTrue(attributes.equals(clone));
        assertNotSame(attributes, clone);
    }

    @Test
    public void testNormalize() {
        attributes.normalize();
        assertEquals("key1", attributes.asList().get(0).getKey());
        assertEquals("key2", attributes.asList().get(1).getKey());
    }
}