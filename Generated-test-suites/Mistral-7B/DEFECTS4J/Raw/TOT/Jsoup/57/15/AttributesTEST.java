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
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", false);
        assertFalse(attributes.hasKey("key4"));
        attributes.put("key4", true);
        assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testPutOverwrite() {
        attributes.put("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));
    }

    @Test
    public void testPutNull() {
        try {
            attributes.put(null, "value");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key5", "value5");
        attributes.removeIgnoreCase("KEY5");
        assertFalse(attributes.hasKey("key5"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        attributes.remove(null);
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key6", "value6");
        otherAttributes.put("key7", "value7");

        attributes.addAll(otherAttributes);
        assertEquals(5, attributes.size());
        assertTrue(attributes.hasKey("key6"));
        assertTrue(attributes.hasKey("key7"));
    }

    @Test
    public void testIterator() {
        List<Attribute> attributesList = attributes.asList();
        assertEquals(5, attributesList.size());
        for (Attribute attribute : attributesList) {
            assertEquals(attribute.getKey(), attribute.getKey());
            assertEquals(attribute.getValue(), attribute.getValue());
        }
    }

    @Test
    public void testDataset() {
        Attributes datasetAttributes = attributes.dataset();
        assertEquals(2, datasetAttributes.size());
        assertTrue(datasetAttributes.hasKey("key1"));
        assertTrue(datasetAttributes.hasKey("key2"));
    }

    @Test
    public void testHtml() {
        StringBuilder actualHtml = new StringBuilder();
        try {
            attributes.html(actualHtml, null);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        String expectedHtml = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expectedHtml, actualHtml.toString());
    }
}