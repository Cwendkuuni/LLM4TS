import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        assertEquals("", attributes.get("invalidKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value3", attributes.getIgnoreCase("key3"));
        assertEquals("", attributes.getIgnoreCase("invalidKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));

        attributes.put("key1", "newvalue");
        assertEquals("newvalue", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key5", true);
        assertTrue(attributes.hasKeyIgnoreCase("key5"));
        assertFalse(attributes.hasKeyIgnoreCase("Key5"));
        assertFalse(attributes.hasKeyIgnoreCase("invalidKey"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKey("invalidKey"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
        attributes.put("key4", "value4");
        assertEquals(4, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key4", "value4");
        incoming.put("key5", "value5");

        attributes.addAll(incoming);

        assertTrue(attributes.hasKey("key4"));
        assertEquals("value4", attributes.get("key4"));

        assertTrue(attributes.hasKey("key5"));
        assertEquals("value5", attributes.get("key5"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attr : attributes) {
            count++;
        }

        assertEquals(attributes.size(), count);
    }

    @Test
    public void testAsList() {
        List<Attribute> attrList = attributes.asList();

        assertEquals(attributes.size(), attrList.size());

        for (Attribute attr : attrList) {
            assertTrue(attributes.hasKey(attr.getKey()));
            assertEquals(attributes.get(attr.getKey()), attr.getValue());
        }
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        String html = attributes.html();

        assertTrue(html.contains("key1"));
        assertTrue(html.contains("key2"));
        assertTrue(html.contains("key3"));

        assertTrue(html.contains("value1"));
        assertTrue(html.contains("value2"));
        assertTrue(html.contains("value3"));
    }

    @Test
    public void testEquals() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");
        other.put("key3", "value3");

        assertTrue(attributes.equals(other));
        assertTrue(other.equals(attributes));

        other.put("key4", "value4");
        assertFalse(attributes.equals(other));
        assertFalse(other.equals(attributes));
    }

    @Test
    public void testHashCode() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");
        other.put("key3", "value3");

        assertEquals(attributes.hashCode(), other.hashCode());

        other.put("key4", "value4");
        assertNotEquals(attributes.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();

        assertEquals(attributes.size(), clone.size());

        for (Attribute attr : clone) {
            assertTrue(attributes.hasKey(attr.getKey()));
            assertEquals(attributes.get(attr.getKey()), attr.getValue());
        }
    }

    @Test
    public void testNormalize() {
        attributes.normalize();

        for (Attribute attr : attributes) {
            assertTrue(attr.getKey().equals(attr.getKey().toLowerCase()));
        }
    }
}