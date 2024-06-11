package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testConstructor() {
        Attribute constructorAttribute = new Attribute("key", "value", null);
        assertEquals("key", constructorAttribute.getKey());
        assertEquals("value", constructorAttribute.getValue());
        assertNull(constructorAttribute.parent);
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

    @Test(expected = NullPointerException.class)
    public void testSetKeyNull() {
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testSetValueWithParent() {
        Attributes parent = new Attributes();
        parent.put("key", "value");

        attribute.setParent(parent);
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parent.get("key"));
    }

    @Test
    public void testHtml() throws IOException {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testHtmlWithNullValue() throws IOException {
        attribute.setValue(null);
        assertEquals("key", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testToStringWithNullValue() {
        attribute.setValue(null);
        assertEquals("key", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertNotNull(encodedAttribute);
        assertEquals("unencodedKey", encodedAttribute.getKey());
        assertEquals("encodedValue", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);

        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertEquals(attribute, sameAttribute);
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());

        Attribute differentAttribute = new Attribute("key", "newValue");
        assertNotEquals(attribute, differentAttribute);
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());

        assertNotEquals(attribute, "key");
        assertNotEquals(attribute, null);
    }

    @Test(expected = RuntimeException.class)
    public void testCloneNotSupported() {
        attribute.clone();
    }
}