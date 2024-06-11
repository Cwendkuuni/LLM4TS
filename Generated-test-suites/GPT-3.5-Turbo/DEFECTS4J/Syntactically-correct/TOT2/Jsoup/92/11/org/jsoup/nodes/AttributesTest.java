import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("NonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("key1", false);
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes.put(attributes2.get("key3"));
        assertTrue(attributes.hasKey("key3"));
        assertFalse(attributes.hasKey("key4"));
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals(2, attributes.dataset().size());
        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }
}