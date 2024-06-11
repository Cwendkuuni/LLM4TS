import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {
    
    private Attributes attributes;
    
    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }
    
    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3"));
    }
    
    @Test
    public void testIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("KEY3"));
    }
    
    @Test
    public void testPutString() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", true);
        assertTrue(attributes.hasKey("key4"));
        attributes.put("key4", false);
        assertFalse(attributes.hasKey("key4"));
    }
    
    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
    }
    
    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key3"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY3"));
    }
    
    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes additionalAttributes = new Attributes();
        additionalAttributes.put("key3", "value3");
        additionalAttributes.put("key4", "value4");
        
        attributes.addAll(additionalAttributes);
        
        assertEquals(4, attributes.size());
        assertTrue(attributes.hasKey("key4"));
    }
    
}