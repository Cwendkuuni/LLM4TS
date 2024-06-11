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
        String value = attributes.get("key1");
        assertEquals("value1", value);
    }

    @Test
    public void testGetIgnoreCase() {
        String value = attributes.getIgnoreCase("KEY1");
        assertEquals("value1", value);
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        String value = attributes.get("key3");
        assertEquals("value3", value);
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", true);
        String value = attributes.get("key4");
        assertEquals("", value);
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
    public void testHasKey() {
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY3"));
    }

    @Test
    public void testSize() {
        int size = attributes.size();
        assertEquals(2, size);
    }

    @Test
    public void testAddAll() {
        Attributes attributesToAdd = new Attributes();
        attributesToAdd.put("key3", "value3");
        attributesToAdd.put("key4", "value4");
        attributes.addAll(attributesToAdd);
        assertEquals(4, attributes.size());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(2, count);
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
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        int hashCode = attributes.hashCode();
        assertNotNull(hashCode);
    }
}