import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "newValue");
        assertEquals("newValue", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key4", "value4");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key4"));
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.remove("KeY1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
        attributes.remove("key1");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    // Additional test cases for edge cases and error scenarios can be added here

}