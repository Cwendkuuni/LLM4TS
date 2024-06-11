import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertEquals("John", attributes.getIgnoreCase("NAME"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertTrue(attributes.hasKey("name"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("selected", true);
        assertTrue(attributes.hasKey("selected"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.removeIgnoreCase("NAME");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        Attributes attributes2 = new Attributes();
        attributes2.put("age", "25");
        attributes1.addAll(attributes2);
        assertTrue(attributes1.hasKey("age"));
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        assertEquals("value", attributes.dataset().get("key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertEquals(" name=\"John\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        Attributes attributes2 = attributes1.clone();
        assertEquals(attributes1, attributes2);
    }
}

