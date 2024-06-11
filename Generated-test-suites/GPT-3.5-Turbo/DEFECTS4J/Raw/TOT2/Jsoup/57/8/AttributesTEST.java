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
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY2", "value2");
        
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPutString() {
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        
        assertTrue(attributes.hasKey("key3"));
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key4", true);
        
        assertTrue(attributes.hasKey("key4"));
        assertEquals("", attributes.get("key4")); // value should be empty since it was removed
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key5", "value5");
        attributes.remove("key5");
        
        assertFalse(attributes.hasKey("key5"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY6", "value6");
        attributes.removeIgnoreCase("key6");
        
        assertFalse(attributes.hasKey("key6"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key7", "value7");
        attributes.put("key8", "value8");
        
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key9", "value9");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key10", "value10");
        
        attributes1.addAll(attributes2);
        
        assertTrue(attributes1.hasKey("key10"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        Map<String, String> dataset = attributes.dataset();
        
        assertTrue(dataset.containsKey("key"));
        assertEquals("data-value", dataset.get("key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key11", "value11");
        
        assertEquals(" key11=\"value11\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key12", "value12");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key12", "value12");
        
        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key13", "value13");
        
        assertEquals(attributes.hashCode(), attributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key14", "value14");
        
        Attributes clone = attributes.clone();
        
        assertTrue(clone.hasKey("key14"));
        assertEquals("value14", clone.get("key14"));
    }
}