import org.jsoup.nodes.Attributes;
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
        assertEquals("", attributes.get("nonExistingKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KeY2"));
        assertEquals("", attributes.getIgnoreCase("nonExistingKey"));
    }

    @Test
    public void testPut() {
        attributes.put("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
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
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonExistingKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistingKey"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key3", "value3");
        newAttributes.put("key4", "value4");
        
        attributes.addAll(newAttributes);
        
        assertEquals(4, attributes.size());
        assertTrue(attributes.hasKey("key3"));
    }

    @Test
    public void testIterator() {
        assertTrue(attributes.iterator().hasNext());
    }

    @Test
    public void testAsList() {
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "data-value");
        assertTrue(attributes.dataset().containsKey("data-key"));
    }

    @Test
    public void testHtml() {
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");

        assertTrue(attributes.equals(newAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");

        assertEquals(attributes.hashCode(), newAttributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();

        assertTrue(attributes.equals(clonedAttributes));
    }
}