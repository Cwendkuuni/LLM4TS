import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");
        String expected = "value";

        assertEquals(expected, result);

        result = attributes.getIgnoreCase("key");
        assertEquals(expected, result);

        result = attributes.get("invalidKey");
        assertEquals("", result);

        result = attributes.getIgnoreCase("InvalidKey");
        assertEquals("", result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();

        attributes.put("key", "value1");
        attributes.put("key", "value2");

        String result = attributes.get("key");
        String expected = "value2";

        assertEquals(expected, result);

        attributes.put("key", "value3");
        result = attributes.get("key");
        assertEquals(expected, result);

        attributes.put("newKey", "newValue");
        result = attributes.get("newKey");
        assertEquals("newValue", result);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();

        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.size() > 1);

        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));

        attributes.put("newKey", "newValue");
        attributes.remove("newKey");
        assertFalse(attributes.hasKey("newKey"));

        attributes.remove("invalidKey");
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        attributes.removeIgnoreCase("key1");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("KEY2"));

        attributes.removeIgnoreCase("Key3");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();

        assertTrue(attributes.hasKey(""));
        assertFalse(attributes.hasKey("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();

        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");

        attributes1.addAll(attributes2);

        assertEquals(2, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertTrue(dataset.containsKey("data-key2"));
        assertEquals("data-value1", dataset.get("data-key1"));
        assertEquals("data-value2", dataset.get("data-key2"));
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        StringBuilder sb = new StringBuilder();
        attributes.html(sb, null);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(2, clonedAttributes.size());
        assertTrue(clonedAttributes.hasKey("key1"));
        assertTrue(clonedAttributes.hasKey("key2"));
        assertEquals("value1", clonedAttributes.get("key1"));
        assertEquals("value2", clonedAttributes.get("key2"));
    }

}