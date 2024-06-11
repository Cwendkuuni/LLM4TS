
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();

        // Test case 1: Get attribute value when attribute set is null
        assertEquals("", attributes.get("key"));

        // Test case 2: Get attribute value when attribute is not set
        attributes.put("key1", "value1");
        assertEquals("", attributes.get("key2"));

        // Test case 3: Get attribute value when attribute is set
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();

        // Test case 1: Get attribute value when attribute set is null
        assertEquals("", attributes.getIgnoreCase("key"));

        // Test case 2: Get attribute value when attribute is not set
        attributes.put("key1", "value1");
        assertEquals("", attributes.getIgnoreCase("Key2"));

        // Test case 3: Get attribute value when attribute is set
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();

        // Test case 1: Put new attribute
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));

        // Test case 2: Replace existing attribute
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        // Test case 1: Put new boolean attribute with value true
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertEquals("true", attributes.get("key1"));

        // Test case 2: Put new boolean attribute with value false
        attributes.put("key2", false);
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();

        // Test case 1: Remove existing attribute
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        // Test case 2: Remove non-existing attribute
        attributes.remove("nonexistent");
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();

        // Test case 1: Remove existing attribute with case-insensitive key
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        // Test case 2: Remove non-existing attribute with case-insensitive key
        attributes.removeIgnoreCase("nonexistent");
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        // Test case 1: Check if attribute key exists when attribute set is null
        assertFalse(attributes.hasKey("key"));

        // Test case 2: Check if attribute key exists when attribute is not set
        attributes.put("key1", "value1");
        assertFalse(attributes.hasKey("key2"));

        // Test case 3: Check if attribute key exists when attribute is set
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();

        // Test case 1: Check if attribute key exists (case-insensitive) when attribute set is null
        assertFalse(attributes.hasKeyIgnoreCase("key"));

        // Test case 2: Check if attribute key exists (case-insensitive) when attribute is not set
        attributes.put("key1", "value1");
        assertFalse(attributes.hasKeyIgnoreCase("Key2"));

        // Test case 3: Check if attribute key exists (case-insensitive) when attribute is set
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();

        // Test case 1: Get size of attribute set when attribute set is null
        assertEquals(0, attributes.size());

        // Test case 2: Get size of attribute set when attributes are added
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());

        attributes.remove("key2");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
        assertTrue(attributes1.hasKey("key4"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        List<Attribute> attributeList = attributes.asList();

        assertEquals(3, attributeList.size());
        assertTrue(attributeList.contains(attributes.get("key1")));
        assertTrue(attributeList.contains(attributes.get("key2")));
        assertTrue(attributeList.contains(attributes.get("key3")));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("data-key3", "value3");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value3", dataset.get("key3"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals(" key1=\"value1\" key2=\"value2\" key3=\"value3\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = attributes1.clone();

        assertEquals(attributes1, attributes2);
        assertNotSame(attributes1, attributes2);
        assertNotSame(attributes1.asList(), attributes2.asList());
        assertNotSame(attributes1.iterator(), attributes2.iterator());
    }

    @Test
    public void testDataKey() {
        Attributes attributes = new Attributes();

        assertEquals("data-key", attributes.dataKey("key"));
    }
}
