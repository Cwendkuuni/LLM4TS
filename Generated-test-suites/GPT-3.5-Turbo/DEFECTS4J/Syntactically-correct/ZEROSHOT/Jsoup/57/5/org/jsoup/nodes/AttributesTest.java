import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        assertEquals("John", attributes.getIgnoreCase("name"));
        assertEquals("25", attributes.getIgnoreCase("age"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        assertEquals("checked", attributes.get("checked"));
        attributes.put("checked", false);
        assertFalse(attributes.hasKey("checked"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("name", "John");
        attributes.put(attribute);
        assertEquals("John", attributes.get("name"));
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
        attributes.removeIgnoreCase("nAmE");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertTrue(attributes.hasKey("name"));
        assertFalse(attributes.hasKey("age"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertTrue(attributes.hasKeyIgnoreCase("nAmE"));
        assertFalse(attributes.hasKeyIgnoreCase("age"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("name", "John");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");

        Attributes attributes2 = new Attributes();
        attributes2.put("phone", "12345");

        attributes1.addAll(attributes2);
        assertEquals(3, attributes1.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(2, count);
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
        attributes.put("data-name", "John");
        attributes.put("data-age", "25");

        assertEquals(2, attributes.dataset().size());
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        assertEquals(" name=\"John\" age=\"25\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");

        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        attributes2.put("age", "25");

        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");

        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        attributes2.put("age", "25");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");

        Attributes attributes2 = attributes1.clone();
        assertTrue(attributes1.equals(attributes2));
    }
}