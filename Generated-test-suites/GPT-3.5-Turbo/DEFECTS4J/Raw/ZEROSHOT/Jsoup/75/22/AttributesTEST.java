import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String value1 = attributes.get("key1");
        String value2 = attributes.get("key2");
        assertEquals("value1", value1);
        assertEquals("value2", value2);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key1", "value1");
        attributes.put("kEy2", "value2");

        String value1 = attributes.getIgnoreCase("key1");
        String value2 = attributes.getIgnoreCase("KEY2");
        assertEquals("value1", value1);
        assertEquals("value2", value2);
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "value2");
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key1"));

        attributes.put("key1", false);
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");

        attributes.put(attribute1);
        attributes.put(attribute2);

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        attributes.removeIgnoreCase("KeY1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
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

        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));

        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }
}