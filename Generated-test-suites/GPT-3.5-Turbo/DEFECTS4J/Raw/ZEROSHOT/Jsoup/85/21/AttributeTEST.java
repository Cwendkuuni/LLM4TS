import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "encodedValue");
        assertEquals("key", encodedAttribute.getKey());
        assertEquals("encodedValue", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.setSyntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(out));
        attribute.setValue("");
        assertTrue(attribute.shouldCollapseAttribute(out));
        attribute.setValue("key");
        assertTrue(attribute.shouldCollapseAttribute(out));
        attribute.setValue("value");
        assertFalse(attribute.shouldCollapseAttribute(out));

        out.setSyntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(equalAttribute));

        Attribute notEqualAttribute = new Attribute("key", "newValue");
        assertFalse(attribute.equals(notEqualAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());

        Attribute notEqualAttribute = new Attribute("key", "newValue");
        assertNotEquals(attribute.hashCode(), notEqualAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}