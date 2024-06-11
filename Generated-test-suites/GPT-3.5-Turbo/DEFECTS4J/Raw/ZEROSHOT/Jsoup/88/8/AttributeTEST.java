import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setup() {
        attribute = new Attribute("testKey", "testValue");
    }

    @Test
    public void testGetKey() {
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("testKey=\"testValue\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("testKey", "test&amp;Value");
        assertEquals("testKey", encodedAttribute.getKey());
        assertEquals("test&Value", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        attribute.setKey("data-test");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(out));
        attribute.setKey("allowfullscreen");
        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        attribute.setKey("checked");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute, equalAttribute);
        equalAttribute.setKey("newKey");
        assertNotEquals(attribute, equalAttribute);
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
        equalAttribute.setKey("newKey");
        assertNotEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}