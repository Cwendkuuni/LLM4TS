import org.jsoup.nodes.Attribute;
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

        String result = attributes.get("key");

        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");

        String result = attributes.getIgnoreCase("kEy");

        assertEquals("value", result);
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");

        String result = attributes.get("key");

        assertEquals("value", result);
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);

        String result = attributes.get("key");

        assertEquals("", result);
        assertTrue(attributes.hasKey("key"));

        attributes.put("key", false);

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");

        attributes.remove("key");

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");

        attributes.removeIgnoreCase("kEy");

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");

        boolean result = attributes.hasKey("key");

        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key", "value");

        boolean result = attributes.hasKeyIgnoreCase("kEy");

        assertTrue(result);
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int result = attributes.size();

        assertEquals(2, result);
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key", "value");

        attributes.addAll(incoming);

        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
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

        int count = 0;
        for (Attribute attribute : attributes.asList()) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testDataset() {
        attributes.put("key1", "value1");
        attributes.put("data-key2", "value2");
        attributes.put("data-key3", "value3");

        String result = attributes.dataset().get("key2");

        assertEquals("value2", result);
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String result = attributes.html();

        assertEquals(" key1=\"value1\" key2=\"value2\"", result);
    }

    @Test
    public void testEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");

        boolean result = attributes.equals(other);

        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");

        int hashCode = attributes.hashCode();
        int otherHashCode = other.hashCode();

        assertEquals(hashCode, otherHashCode);
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
        assertNotSame(attributes.asList(), clone.asList());
    }

    @Test
    public void testDatasetIterator() {
        attributes.put("key1", "value1");
        attributes.put("data-key2", "value2");
        attributes.put("key3", "value3");
        attributes.put("data-key4", "value4");

        Attributes.DatasetIterator iterator = attributes.new Dataset().new DatasetIterator();

        assertTrue(iterator.hasNext());
        assertEquals("key2", iterator.next().getKey());
        assertTrue(iterator.hasNext());
        assertEquals("key4", iterator.next().getKey());
        assertFalse(iterator.hasNext());
    }
}