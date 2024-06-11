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
        attributes.put("key", "value");

        String result = attributes.get("key");

        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");

        String result = attributes.getIgnoreCase("KEY");

        assertEquals("value", result);
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");

        String result = attributes.get("key");

        assertEquals("value", result);
    }

    @Test
    public void testPutWithExistingKey() {
        attributes.put("key", "value");
        attributes.put("key", "new value");

        String result = attributes.get("key");

        assertEquals("new value", result);
    }

    @Test
    public void testPutWithBooleanValue() {
        attributes.put("key", true);

        boolean result = attributes.hasKey("key");

        assertTrue(result);
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");

        attributes.remove("key");

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");

        attributes.removeIgnoreCase("KEY");

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");

        boolean result = attributes.hasKey("key");

        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key", "value");

        boolean result = attributes.hasKeyIgnoreCase("KEY");

        assertTrue(result);
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int result = attributes.size();

        assertEquals(2, result);
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributeList = attributes.asList();

        assertEquals(2, attributeList.size());
        assertEquals("key1", attributeList.get(0).getKey());
        assertEquals("value1", attributeList.get(0).getValue());
        assertEquals("key2", attributeList.get(1).getKey());
        assertEquals("value2", attributeList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }
}