import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        assertEquals("", attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KeY2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", true);
        assertTrue(attributes.hasKey("key4"));
        assertEquals("", attributes.get("key4"));

        attributes.put("key4", false);
        assertFalse(attributes.hasKey("key4"));
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
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key3", "value3");
        newAttributes.put("key4", "value4");

        attributes.addAll(newAttributes);

        assertEquals(4, attributes.size());
    }

    @Test
    public void testDataset() {
        assertTrue(attributes.dataset() instanceof Map);
    }

    @Test
    public void testHtml() throws SerializationException {
        assertNotNull(attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");

        assertEquals(attributes, newAttributes);
    }

    @Test
    public void testHashCode() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        assertEquals(attributes.hashCode(), newAttributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
    }
}