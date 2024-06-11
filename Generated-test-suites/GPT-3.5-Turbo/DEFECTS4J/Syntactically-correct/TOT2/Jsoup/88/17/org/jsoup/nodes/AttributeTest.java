import org.jsoup.nodes.Attribute;
import org.jsoup.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

import org.junit.Test;

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
        
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");

        assertEquals("href=\"index.html\"", attribute.html());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute, clonedAttribute);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("href", "index.html");

        assertEquals("href", attribute.getKey());
        assertEquals("index.html", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-id", "123");

        assertTrue(attribute.isDataAttribute());
        assertFalse(new Attribute("name", "John Doe").isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        Attribute nonBooleanAttribute = new Attribute("name", "John Doe");

        assertTrue(booleanAttribute.isBooleanAttribute());
        assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }
}