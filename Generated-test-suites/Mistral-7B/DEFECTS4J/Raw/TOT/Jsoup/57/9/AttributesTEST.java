import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", true);
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("NonExistentKey"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        attributes.put(null, "value5");
    }

    @Test
    public void testPutWithNullValue() {
        attributes.put("key5", null);
        assertEquals("", attributes.get("key5"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key6", false);
        assertFalse(attributes.hasKey("key6"));
        attributes.put("key6", true);
        assertTrue(attributes.hasKey("key6"));
        assertFalse(attributes.getIgnoreCase("Key6").equals("true"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullKey() {
        attributes.remove(null);
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
        assertTrue(attributes.hasKeyIgnoreCase("Key3"));
    }

    @Test
    public void testSize() {
        assertEquals(4, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key7", "value7");
        attributes.addAll(newAttributes);
        assertEquals(5, attributes.size());
        assertTrue(attributes.hasKey("key7"));
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
        assertEquals("value4", dataset.get("key4"));
        assertFalse(dataset.containsKey("key3"));
    }

    @Test
    public void testHtml() {
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, new Document.OutputSettings());
            assertEquals(" key1=\"value1\" key2=\"value2\" key3=\"\" key6=\"\" data-key4=\"value4\"", accum.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}