import org.jsoup.nodes.Attributes;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        attributes.put("Key1", "value1");
        
        assertEquals("value1", attributes.getIgnoreCase("kEy1"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        assertEquals("value1", attributes.get("key1"));
        
        attributes.put("key1", "newValue");
        
        assertEquals("newValue", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        
        assertEquals("key1", attributes.get("key1"));
        
        attributes.put("key2", false);
        
        assertFalse(attributes.hasKey("key2"));
    }

    // Add test cases for other public methods 

}