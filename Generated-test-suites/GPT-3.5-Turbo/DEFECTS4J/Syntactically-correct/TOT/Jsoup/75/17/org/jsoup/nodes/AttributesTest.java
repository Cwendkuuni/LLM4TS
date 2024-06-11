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
        attributes.put("key1", "value1");
        String result = attributes.get("key1");
        assertEquals("value1", result);
    }

    @Test
    public void testGet_NonExistingKey() {
        String result = attributes.get("key2");
        assertEquals("", result);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        String result = attributes.getIgnoreCase("KEY1");
        assertEquals("value1", result);
    }

    @Test
    public void testGetIgnoreCase_NonExistingKey() {
        String result = attributes.getIgnoreCase("KEY2");
        assertEquals("", result);
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        String result = attributes.get("key1");
        assertEquals("value1", result);
    }

    @Test
    public void testPut_ExistingKey() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        String result = attributes.get("key1");
        assertEquals("value2", result);
    }

    @Test
    public void testPut_BooleanAttribute() {
        attributes.put("key1", true);
        boolean result = attributes.hasKey("key1");
        assertTrue(result);
        result = attributes.hasKeyIgnoreCase("KEY1");
        assertTrue(result);

        attributes.put("key2", false);
        result = attributes.hasKey("key2");
        assertFalse(result);
        result = attributes.hasKeyIgnoreCase("KEY2");
        assertFalse(result);
    }

    @Test
    public void testPut_AttributeObject() {
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        String result = attributes.get("key1");
        assertEquals("value1", result);
        assertTrue(attribute.getParent() == attributes);
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        boolean result = attributes.hasKey("key1");
        assertFalse(result);
    }

    @Test
    public void testRemove_NonExistingKey() {
        attributes.put("key1", "value1");
        attributes.remove("key2");
        boolean result = attributes.hasKey("key1");
        assertTrue(result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("KEY1");
        boolean result = attributes.hasKey("key1");
        assertFalse(result);
    }

    @Test
    public void testRemoveIgnoreCase_NonExistingKey() {
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("KEY2");
        boolean result = attributes.hasKey("key1");
        assertTrue(result);
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        boolean result = attributes.hasKey("key1");
        assertTrue(result);
    }

    @Test
    public void testHasKey_NonExistingKey() {
        boolean result = attributes.hasKey("key1");
        assertFalse(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        boolean result = attributes.hasKeyIgnoreCase("KEY1");
        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase_NonExistingKey() {
        boolean result = attributes.hasKeyIgnoreCase("key1");
        assertFalse(result);
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        assertEquals(2, result);
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        Attributes clone = attributes.clone();
        boolean result = attributes.equals(clone);
        assertTrue(result);
    }
}