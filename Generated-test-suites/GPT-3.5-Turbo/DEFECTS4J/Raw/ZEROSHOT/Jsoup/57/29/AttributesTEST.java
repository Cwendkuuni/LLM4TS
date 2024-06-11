import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", true);
        assertEquals("true", attributes.get("key4"));
        attributes.put("key4", false);
        assertEquals("", attributes.get("key4"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key3", "value3");
        attributes.put(attribute);
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY1");
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY3"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key3", "value3");
        newAttributes.put("key4", "value4");
        attributes.addAll(newAttributes);
        assertEquals("value3", attributes.get("key3"));
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testIterator() {
        Iterator<Attribute> iterator = attributes.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        List<Attribute> attributeList = attributes.asList();
        assertEquals(2, attributeList.size());
        assertEquals("value1", attributeList.get(0).getValue());
        assertEquals("value2", attributeList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        dataset.put("key3", "value3");
        assertEquals("value3", attributes.get("data-key3"));
    }

    @Test
    public void testHtml() {
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
    }

    @Test
    public void testDataKey() {
        String dataKey = Attributes.dataKey("key");
        assertEquals("data-key", dataKey);
    }
}