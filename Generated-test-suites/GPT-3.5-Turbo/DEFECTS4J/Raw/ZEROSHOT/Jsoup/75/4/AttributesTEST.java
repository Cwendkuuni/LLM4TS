import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
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
        attributes.put("key2", "value2");

        String result1 = attributes.get("key1");
        String result2 = attributes.get("key2");
        String result3 = attributes.get("key3");

        assertEquals("value1", result1);
        assertEquals("value2", result2);
        assertEquals("", result3);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");

        String result1 = attributes.getIgnoreCase("key1");
        String result2 = attributes.getIgnoreCase("key2");
        String result3 = attributes.getIgnoreCase("key3");

        assertEquals("value1", result1);
        assertEquals("value2", result2);
        assertEquals("", result3);
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String result1 = attributes.get("key1");
        String result2 = attributes.get("key2");

        assertEquals("value1", result1);
        assertEquals("value2", result2);
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");

        attributes.removeIgnoreCase("key1");

        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int result = attributes.size();

        assertEquals(2, result);
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        attributes.put("key2", false);

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes.Attribute attribute = new Attributes.Attribute("key1", "value1");

        attributes.put(attribute);

        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testNormalize() {
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        attributes.normalize();

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKey("key2"));
    }

}