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
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("value3", attributes.getIgnoreCase("KEY3"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "newValue");
        assertEquals("newValue", attributes.get("key1"));

        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));

        attributes.put("key5", true);
        assertEquals("", attributes.getIgnoreCase("key5"));

        attributes.put("key6", false);
        assertFalse(attributes.hasKeyIgnoreCase("key6"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        attributes.removeIgnoreCase("KEY2");
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
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

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());

        attributes.remove("key1");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key4", "value4");
        newAttributes.put("key5", "value5");

        attributes.addAll(newAttributes);

        assertTrue(attributes.hasKey("key4"));
        assertTrue(attributes.hasKey("key5"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attribute : attributes) {
            assertNotNull(attribute.getKey());
            assertNotNull(attribute.getValue());
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testAsList() {
        assertEquals(3, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }

    @Test
    public void testHtml() {
        String html = attributes.html();

        assertTrue(html.contains("key1=\"value1\""));
        assertTrue(html.contains("key2=\"value2\""));
        assertTrue(html.contains("key3=\"value3\""));
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        otherAttributes.put("key3", "value3");

        assertEquals(attributes, otherAttributes);
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        otherAttributes.put("key3", "value3");

        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
    }

    @Test
    public void testNormalize() {
        attributes.normalize();

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKey("key3"));
    }
}