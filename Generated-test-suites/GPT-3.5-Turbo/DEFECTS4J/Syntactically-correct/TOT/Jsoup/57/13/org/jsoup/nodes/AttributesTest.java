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
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGet_KeyNotSet() {
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetIgnoreCase_KeyNotSet() {
        assertEquals("", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPut_ReplaceExisting() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPut_BooleanAttribute_True() {
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertEquals("key", attributes.get("key"));
    }

    @Test
    public void testPut_BooleanAttribute_False() {
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPut_Attribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testHasKey_True() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey_False() {
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase_True() {
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKeyIgnoreCase_False() {
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testSize_Empty() {
        assertEquals(0, attributes.size());
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
        
        for (Attribute attribute : attributes) {
            assertTrue(attributes.hasKey(attribute.getKey()));
        }
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        for (Attribute attribute : attributes.asList()) {
            assertTrue(attributes.hasKey(attribute.getKey()));
        }
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        assertEquals(2, attributes.dataset().size());
        assertTrue(attributes.dataset().containsKey("key1"));
        assertTrue(attributes.dataset().containsKey("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        String expectedHtml = " key1='value1' key2='value2'";
        assertEquals(expectedHtml, attributes.html());
    }

    @Test
    public void testEquals_True() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        
        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testEquals_False() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        
        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        int expectedHashCode = attributes.asList().hashCode();
        assertEquals(expectedHashCode, attributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        Attributes clone = attributes.clone();
        
        assertTrue(clone.equals(attributes));
        assertNotSame(clone, attributes);
    }

}