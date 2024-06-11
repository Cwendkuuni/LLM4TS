import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPut() {
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
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        attributes.addAll(newAttributes);
        assertTrue(attributes.hasKey("key1"));
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
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.dataset().put("key1", "value1");
        assertTrue(attributes.hasKey("data-key1"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String expected = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expected, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        attributes.put("key1", "value1");
        assertTrue(attributes.equals(newAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        attributes.put("key1", "value1");
        assertEquals(attributes.hashCode(), newAttributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        Attributes clone = attributes.clone();
        assertTrue(attributes.equals(clone));
    }
}