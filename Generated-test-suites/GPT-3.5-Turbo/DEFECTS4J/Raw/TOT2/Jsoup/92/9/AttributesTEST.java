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
        attributes.put("key3", "value3");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
    }

    @Test
    public void testPut() {
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key5", true);
        assertTrue(attributes.hasKey("key5"));
        assertFalse(attributes.hasKey("key6"));
        attributes.put("key5", false);
        assertFalse(attributes.hasKey("key5"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key6", "value6");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key6"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY2");
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key4", "value4");
        newAttributes.put("key5", "value5");
        attributes.addAll(newAttributes);
        assertEquals(5, attributes.size());
    }
}