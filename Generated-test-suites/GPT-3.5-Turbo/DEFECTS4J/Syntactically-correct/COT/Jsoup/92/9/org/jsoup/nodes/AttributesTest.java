
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

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
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("KEY3"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));

        attributes.put("key1", "value4");
        assertEquals("value4", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", true);
        assertTrue(attributes.hasKey("key4"));
        assertEquals("", attributes.get("key4"));

        attributes.put("key4", false);
        assertFalse(attributes.hasKey("key4"));
        assertEquals("", attributes.get("key4"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key3", "value3");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key3"));
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
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
        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
    }

}
