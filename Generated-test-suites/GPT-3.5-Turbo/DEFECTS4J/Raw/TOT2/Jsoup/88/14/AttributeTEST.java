import org.jsoup.nodes.Attribute;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.setValue("newValue"));
    }

    @Test
    public void testHTML() {
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());
    }

    // Additional test cases for edge cases and exceptions
    @Test
    public void testEmptyKey() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute("", "value"));
    }

    @Test
    public void testNullValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.setValue(null));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertNotNull(clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNotSame(attribute, clonedAttribute);
    }
}