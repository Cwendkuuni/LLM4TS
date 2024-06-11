import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String value1 = attributes.get("key1");
        assertEquals("value1", value1);

        String value2 = attributes.get("key2");
        assertEquals("value2", value2);

        String value3 = attributes.get("key3");
        assertEquals("", value3);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String value1 = attributes.getIgnoreCase("Key1");
        assertEquals("value1", value1);

        String value2 = attributes.getIgnoreCase("Key2");
        assertEquals("value2", value2);

        String value3 = attributes.getIgnoreCase("Key3");
        assertEquals("", value3);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String value1 = attributes.get("key1");
        assertEquals("value1", value1);

        String value2 = attributes.get("key2");
        assertEquals("value2", value2);

        attributes.put("key1", "new value1");
        String newValue1 = attributes.get("key1");
        assertEquals("new value1", newValue1);

        attributes.put("key3", "value3");
        String value3 = attributes.get("key3");
        assertEquals("value3", value3);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key2", false);

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));

        assertEquals("", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));

        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        assertEquals("", attributes.get("key1"));
        attributes.remove("key1");

        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        assertEquals("", attributes.getIgnoreCase("key1"));
        attributes.removeIgnoreCase("Key1");

        assertEquals("value2", attributes.getIgnoreCase("Key2"));
    }
    
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("key3"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());

        attributes.remove("key2");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        int count = 0;
        for (Attribute attr : attributes) {
            assertEquals(attr, list.get(count));
            count++;
        }
        assertEquals(count, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
        assertEquals(attributes.size(), clone.size());

        clone.remove("key1");
        assertFalse(clone.hasKey("key1"));
        assertTrue(attributes.hasKey("key1"));
    }
}