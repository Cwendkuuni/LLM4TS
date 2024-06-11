import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullAttributes() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("key");
        String expected = "";
        assertEquals(expected, actual);
        // This test case checks the functionality when the attributes are null
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
        // This test case checks the functionality when putting an attribute
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);
        // This test case checks the functionality when putting a boolean attribute
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
        // This test case checks the functionality when putting an Attribute object
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        assertEquals("", actual);
        // This test case checks the functionality when removing an attribute
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("Key");
        String actual = attributes.get("Key");
        assertEquals("", actual);
        // This test case checks the functionality when removing an attribute ignoring case
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);
        // This test case checks the functionality when checking if an attribute exists
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKeyIgnoreCase("Key");
        assertFalse(actual);
        // This test case checks the functionality when checking if an attribute exists ignoring case
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        assertEquals(expected, actual);
        // This test case checks the functionality when getting the size of the attributes
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int actual = attributes1.size();
        int expected = 2;
        assertEquals(expected, actual);
        // This test case checks the functionality when adding all attributes from another Attributes object
    }
}