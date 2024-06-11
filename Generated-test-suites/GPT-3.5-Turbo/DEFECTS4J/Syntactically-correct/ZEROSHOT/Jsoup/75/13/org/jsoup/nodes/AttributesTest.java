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
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");
        String result = attributes.getIgnoreCase("KEY");
        assertEquals("value", result);
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        assertTrue(result);
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        boolean result = attributes.hasKey("key");
        assertFalse(result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KEY");
        boolean result = attributes.hasKey("key");
        assertFalse(result);
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key", "value");
        boolean result = attributes.hasKeyIgnoreCase("KEY");
        assertTrue(result);
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        assertEquals(2, result);
    }

}