import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    // Test case for get method
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3"));
    }

    // Test case for getIgnoreCase method
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
    }

    // Test case for put method
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    // Test case for put with boolean value
    @Test
    public void testPutWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key2", false);

        assertEquals("true", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }

    // Test case for remove method
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    // Test case for removeIgnoreCase method
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    // Test case for hasKey method
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("key3"));
    }

    // Test case for hasKeyIgnoreCase method
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    // Test case for size method
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());

        attributes.remove("key2");
        assertEquals(0, attributes.size());
    }

    // Test case for addAll method
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
        assertEquals("value3", attributes1.get("key3"));
        assertEquals("value4", attributes1.get("key4"));
    }

    // Test case for iterator method
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();

        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next().getValue());

        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next().getValue());

        assertFalse(iterator.hasNext());
    }

    // Test case for asList method
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributeList = attributes.asList();

        assertEquals(2, attributeList.size());
        assertEquals("value1", attributeList.get(0).getValue());
        assertEquals("value2", attributeList.get(1).getValue());
    }

    // Test case for dataset method
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(1, dataset.size());
        assertEquals("value1", dataset.get("key1"));
    }

    // Test case for clone method
    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes.size(), clone.size());
        assertEquals(attributes.get("key1"), clone.get("key1"));
        assertEquals(attributes.get("key2"), clone.get("key2"));
    }

    // Test case for html method
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        assertEquals(" key1=\"value1\" key2=\"value2\"", html);
    }

    // Test case for equals method
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

    // Test case for hashCode method
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
}