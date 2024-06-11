import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        // Test if get method returns the correct value for a given key
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Test if getIgnoreCase method returns the correct value for a case-insensitive key
        Attributes attributes = new Attributes();
        attributes.put("Key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        // Test if put method adds a new attribute correctly
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
    }

    @Test
    public void testPutBoolean() {
        // Test if put method handles boolean attributes correctly
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testRemove() {
        // Test if remove method removes an attribute correctly
        Attributes attributes = new Attributes();
        attributes.put("key4", "value4");
        attributes.remove("key4");
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testSize() {
        // Test if size method returns the correct number of attributes
        Attributes attributes = new Attributes();
        attributes.put("key5", "value5");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        // Test if addAll method adds all attributes from another set correctly
        Attributes attributes1 = new Attributes();
        attributes1.put("key6", "value6");
        Attributes attributes2 = new Attributes();
        attributes2.put("key7", "value7");
        attributes1.addAll(attributes2);
        assertTrue(attributes1.hasKey("key7"));
    }

    @Test
    public void testDataset() {
        // Test if dataset method retrieves HTML5 custom data attributes correctly
        Attributes attributes = new Attributes();
        attributes.put("data-custom", "customValue");
        assertTrue(attributes.dataset().containsKey("custom"));
    }

}