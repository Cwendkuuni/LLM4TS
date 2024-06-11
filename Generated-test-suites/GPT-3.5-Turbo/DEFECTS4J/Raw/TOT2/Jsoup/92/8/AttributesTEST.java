import org.jsoup.nodes.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
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
    public void testAddAndGetAttribute() {
        Attribute attribute = new Attribute("test", "value");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("test"));
        assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testAddBooleanAttribute() {
        attributes.put("boolAttr", true);
        assertTrue(attributes.hasKeyIgnoreCase("boolAttr"));
        assertTrue(attributes.getIgnoreCase("boolAttr").isEmpty());
    }

    @Test
    public void testRemoveAttribute() {
        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));
        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testIterator() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        attributes.put(attribute1).put(attribute2);
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testClone() {
        attributes.put("test", "value");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
        assertNotSame(attributes, clonedAttributes);
    }

}