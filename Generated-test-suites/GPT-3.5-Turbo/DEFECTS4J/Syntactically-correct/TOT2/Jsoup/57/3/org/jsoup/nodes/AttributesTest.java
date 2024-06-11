
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    
    private Attributes attributes;
    
    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }
    
    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key3"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testRemove() {
        assertTrue(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
        attributes.remove("key1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "data-value");
        assertTrue(attributes.dataset().containsKey("data-key"));
        assertEquals("data-value", attributes.dataset().get("data-key"));
    }
}

