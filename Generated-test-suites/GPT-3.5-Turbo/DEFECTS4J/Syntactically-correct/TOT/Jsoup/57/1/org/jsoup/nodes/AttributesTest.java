import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.List;
import java.util.Map;

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
        String value = attributes.get("key1");
        assertEquals("value1", value);
    }

    @Test
    public void testGet_emptyKey() {
        attributes.put("key1", "value1");
        String value = attributes.get("");
        assertEquals("", value);
    }

    @Test
    public void testGet_ignoreCase() {
        attributes.put("Key1", "value1");
        String value = attributes.getIgnoreCase("key1");
        assertEquals("value1", value);
    }

    @Test
    public void testGet_ignoreCase_emptyKey() {
        attributes.put("Key1", "value1");
        String value = attributes.getIgnoreCase("");
        assertEquals("", value);
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testPut_emptyKey() {
        attributes.put("", "value1");
        assertTrue(attributes.hasKey(""));
    }

    @Test
    public void testPut_booleanValueTrue() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertEquals("key1", attributes.iterator().next().getKey());
    }

    @Test
    public void testPut_booleanValueFalse() {
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemove_ignoreCase() {
        attributes.put("Key1", "value1");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("Key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("Key1"));
    }

    @Test
    public void testHasKey_ignoreCase() {
        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("key1", list.get(0).getKey());
        assertEquals("key2", list.get(1).getKey());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "<script>alert('XSS');</script>");

        String html = attributes.html();
        assertTrue(html.contains("key1=\"value1\""));
        assertTrue(html.contains("key2=\"&lt;script&gt;alert('XSS');&lt;/script&gt;\""));
    }

    @Test
    public void testEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes, attributes2);
    }

    @Test
    public void testEquals_differentAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        assertNotEquals(attributes, attributes2);
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes attributes2 = attributes.clone();

        assertEquals(attributes, attributes2);
        assertNotSame(attributes, attributes2);
    }

    @Test
    public void testClone_emptyAttributes() {
        Attributes attributes2 = attributes.clone();

        assertEquals(attributes, attributes2);
        assertNotSame(attributes, attributes2);
    }
}