import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key2", "value2");
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        attributes.remove("key3");
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key4", "value4");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key5", "value5");
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(1, count);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key6", "value6");
        assertTrue(attributes.dataset().containsKey("key6"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key7", "value7");
        assertEquals(" key7=\"value7\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key8", "value8");
        Attributes attributes2 = new Attributes();
        attributes2.put("key8", "value8");
        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key9", "value9");
        assertEquals(748795204, attributes.hashCode());
    }
}