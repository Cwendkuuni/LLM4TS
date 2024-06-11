import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
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
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("KEY3"));
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
        attributes.put("key5", false);
        assertEquals("", attributes.get("key5"));
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
        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
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
        Attributes incoming = new Attributes();
        incoming.put("key3", "value3");
        incoming.put("key4", "value4");
        attributes.addAll(incoming);
        assertEquals(4, attributes.size());
        assertEquals("value3", attributes.get("key3"));
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testIterator() {
        List<String> expectedKeys = Arrays.asList("key1", "key2");
        List<String> actualKeys = new ArrayList<>();
        for (Attribute attribute : attributes) {
            actualKeys.add(attribute.getKey());
        }
        assertEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testAsList() {
        List<Attribute> expectedAttributes = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );
        List<Attribute> actualAttributes = attributes.asList();
        assertEquals(expectedAttributes.size(), actualAttributes.size());
        for (int i = 0; i < expectedAttributes.size(); i++) {
            assertEquals(expectedAttributes.get(i), actualAttributes.get(i));
        }
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("data-value1", dataset.get("data-key1"));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testHtml() {
        String expectedHtml = "";
        expectedHtml += " key1=\"value1\"";
        expectedHtml += " key2=\"value2\"";
        assertEquals(expectedHtml, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertTrue(attributes.equals(attributes2));

        attributes2.put("key3", "value3");
        assertFalse(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertEquals(attributes.hashCode(), attributes2.hashCode());

        attributes2.put("key3", "value3");
        assertNotEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertEquals(attributes.size(), clone.size());
        for (Attribute attribute : attributes) {
            assertTrue(clone.hasKey(attribute.getKey()));
            assertEquals(attribute.getValue(), clone.get(attribute.getKey()));
        }
    }

}