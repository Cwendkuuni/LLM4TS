import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "30");
    }

    @Test
    public void testGet() {
        assertEquals("John", attributes.get("name"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("John", attributes.getIgnoreCase("Name"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("newKey", "newValue");
        assertTrue(attributes.hasKey("newKey"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("isStudent", true);
        assertTrue(attributes.hasKey("isStudent"));
        attributes.put("isTeacher", false);
        assertFalse(attributes.hasKey("isTeacher"));
    }

    @Test
    public void testRemove() {
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("name"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("city", "New York");
        attributes.addAll(newAttributes);
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAsList() {
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        dataset.put("customKey", "customValue");
        assertTrue(attributes.hasKey("data-customKey"));
    }

    // Additional test cases for edge cases and exceptions can be added here

}