import org.jsoup.nodes.Attributes;
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
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKeyIgnoreCase("booleanKey"));
    }

    @Test
    public void testRemove() {
        attributes.put("key4", "value4");
        attributes.remove("key4");
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key5", "value5");
        attributes.removeIgnoreCase("KEY5");
        assertFalse(attributes.hasKeyIgnoreCase("KEY5"));
    }

    @Test
    public void testSize() {
        attributes.put("key6", "value6");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key7", "value7");
        attributes.put("key7", "value7");
        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();
        attributes.put("key8", "value8");
        otherAttributes.put("key8", "value8");
        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key9", "value9");
        Attributes clone = attributes.clone();
        assertEquals(attributes.get("key9"), clone.get("key9"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "data-value");
        assertEquals("data-value", attributes.dataset().get("key"));
    }

}