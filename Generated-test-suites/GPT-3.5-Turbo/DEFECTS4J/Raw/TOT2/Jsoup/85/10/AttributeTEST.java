import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertNotNull(attribute);
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String oldValue = attribute.setValue("newValue");
        assertEquals("testValue", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        String expectedHtml = "href=\"index.html\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("testKey", "testValue");
        Attribute attribute2 = new Attribute("testKey", "testValue");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Attribute clonedAttribute = attribute.clone();
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}