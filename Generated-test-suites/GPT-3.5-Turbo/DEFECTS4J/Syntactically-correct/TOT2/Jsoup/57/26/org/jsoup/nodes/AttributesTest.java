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
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", true);
        assertTrue(attributes.hasKey("key4"));
        attributes.put("key4", false);
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key3", "value3");
        attributes.addAll(newAttributes);
        assertEquals(3, attributes.size());
    }

    @Test
    public void testIterate() {
        Iterator<Attribute> iterator = attributes.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        List<Attribute> attributeList = attributes.asList();
        assertEquals(2, attributeList.size());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertNotNull(dataset);
    }

    @Test
    public void testHtml() {
        String html = attributes.html();
        assertNotNull(html);
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        assertTrue(attributes.equals(newAttributes));
    }

    @Test
    public void testHashCode() {
        assertEquals(attributes.hashCode(), attributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testDatasetPut() {
        attributes.dataset().put("key", "value");
        assertTrue(attributes.hasKey("data-key"));
    }

    @Test
    public void testRemoveData() {
        attributes.dataset().put("key", "value");
        attributes.dataset().remove("key");
        assertFalse(attributes.hasKey("data-key"));
    }

    @Test
    public void testEmptyDataset() {
        attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testDatasetSize() {
        attributes.dataset().put("key", "value");
        assertEquals(1, attributes.dataset().size());
    }

    @Test
    public void testHtmlException() {
        attributes = new Attributes();
        assertThrows(SerializationException.class, () -> attributes.html());
    }

    @Test
    public void testEmptyIterator() {
        attributes = new Attributes();
        Iterator<Attribute> iterator = attributes.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDataKey() {
        assertEquals("data-key", attributes.dataKey("key"));
    }
}