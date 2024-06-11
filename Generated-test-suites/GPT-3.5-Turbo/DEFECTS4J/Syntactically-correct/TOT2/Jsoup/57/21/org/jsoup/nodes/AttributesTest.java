import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPutString() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "value1");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("Key1"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertTrue(dataset.containsKey("key1"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        String html = attributes.html();
        assertTrue(html.contains("key1"));
        assertTrue(html.contains("value1"));
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        attributes.put("key1", "value1");
        otherAttributes.put("key1", "value1");
        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        int hash1 = attributes.hashCode();

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        int hash2 = otherAttributes.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();

        assertTrue(attributes.equals(clonedAttributes));
        assertNotSame(attributes, clonedAttributes);
    }
}