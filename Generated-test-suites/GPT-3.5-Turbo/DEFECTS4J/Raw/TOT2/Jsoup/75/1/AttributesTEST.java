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
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
    }

    @Test
    public void testPut() {
        attributes.put("key4", "value4");
        assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key5", true);
        assertTrue(attributes.hasKeyIgnoreCase("KEY5"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key3");
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertNotSame(attributes, clone);
        assertEquals(attributes.size(), clone.size());
    }

    @Test
    public void testEquals() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");
        other.put("key3", "value3");
        assertEquals(attributes, other);
    }

    @Test
    public void testHashCode() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");
        other.put("key3", "value3");
        assertEquals(attributes.hashCode(), other.hashCode());
    }
}