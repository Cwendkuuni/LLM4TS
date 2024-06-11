package org.jsoup.nodes;
import org.junit.Test;
import org.junit.Before;
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
        String expectedString = "key=\"value\"";
        assertEquals(expectedString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "encoded%20value");
        assertEquals("key", encodedAttribute.getKey());
        assertEquals("encoded value", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(out));
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
        assertEquals(attribute, equalAttribute);
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

}