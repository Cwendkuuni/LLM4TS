import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.html());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNotSame(attr, clonedAttr);
    }

    // Add more test cases for edge cases and exceptions as necessary

}