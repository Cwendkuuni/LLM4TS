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
        attributes.put("key2", "value2");

        String value1 = attributes.get("key1");
        assertEquals("value1", value1);

        String value2 = attributes.get("key2");
        assertEquals("value2", value2);

        String value3 = attributes.get("key3");
        assertEquals("", value3);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        String value1 = attributes.getIgnoreCase("key1");
        assertEquals("value1", value1);

        String value2 = attributes.getIgnoreCase("KEY2");
        assertEquals("value2", value2);

        String value3 = attributes.getIgnoreCase("key3");
        assertEquals("", value3);
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String value1 = attributes.get("key1");
        assertEquals("value1", value1);

        String value2 = attributes.get("key2");
        assertEquals("value2", value2);
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        attributes.put("key2", false);

        String value1 = attributes.get("key1");
        assertEquals("true", value1);

        String value2 = attributes.get("key2");
        assertEquals("", value2);
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");

        attributes.put(attribute1);
        attributes.put(attribute2);

        String value1 = attributes.get("key1");
        assertEquals("value1", value1);

        String value2 = attributes.get("key2");
        assertEquals("value2", value2);
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
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("key1");

        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));

        assertTrue(attributes.hasKey("Key2"));
        assertTrue(attributes.hasKeyIgnoreCase("Key2"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributesToAdd = new Attributes();
        attributesToAdd.put("key1", "value1");
        attributesToAdd.put("key2", "value2");

        attributes.addAll(attributesToAdd);

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        assertTrue(html.contains("key1=\"value1\""));
        assertTrue(html.contains("key2=\"value2\""));
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertTrue(attributes1.equals(attributes2));
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
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertTrue(attributes.equals(clone));
        assertNotSame(attributes, clone);
    }

    @Test
    public void testDatasetKey() {
        String key1 = "key";
        String key2 = "data-key";

        String dataKey1 = Attributes.dataKey(key1);
        String dataKey2 = Attributes.dataKey(key2);

        assertEquals("data-key", dataKey1);
        assertEquals("data-key", dataKey2);
    }
}