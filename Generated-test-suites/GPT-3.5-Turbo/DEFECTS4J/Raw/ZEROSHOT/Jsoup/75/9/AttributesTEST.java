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
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("KEY3"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value3");
        assertEquals("value3", attributes.get("key1"));
        attributes.put("key3", "value4");
        assertEquals("value4", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        assertEquals("", attributes.get("key1"));
        attributes.put("key2", false);
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key1", "value3");
        attributes.put(attribute);
        assertEquals("value3", attributes.get("key1"));
        assertEquals(attributes, attribute.parent);
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        attributes.remove("key3");
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));
        attributes.removeIgnoreCase("key3");
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY3"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
        attributes.remove("key1");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key3", "value3");
        incoming.put("key4", "value4");
        attributes.addAll(incoming);
        assertTrue(attributes.hasKey("key3"));
        assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testAsList() {
        var list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        var dataset = attributes.dataset();
        dataset.put("key3", "value3");
        assertTrue(attributes.hasKey("data-key3"));
        assertEquals("value3", attributes.get("data-key3"));
    }

    @Test
    public void testHtml() {
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.toString());
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
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
        clone.put("key1", "value3");
        assertNotEquals(attributes, clone);
    }

    @Test
    public void testNormalize() {
        attributes.normalize();
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("KEY1"));
        assertFalse(attributes.hasKey("KEY2"));
    }
}