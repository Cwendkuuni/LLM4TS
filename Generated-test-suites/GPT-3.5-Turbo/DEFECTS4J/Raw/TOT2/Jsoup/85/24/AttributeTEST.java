import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("testKey", "testValue");
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
        assertEquals("testValue", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("testKey", "testValue");
        Attribute attribute2 = new Attribute("testKey", "testValue");
        assertEquals(attribute1, attribute2);

        attribute2 = new Attribute("testKey", "newValue");
        assertNotEquals(attribute1, attribute2);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

    // Add additional test cases for edge cases and exception handling scenarios as needed

}