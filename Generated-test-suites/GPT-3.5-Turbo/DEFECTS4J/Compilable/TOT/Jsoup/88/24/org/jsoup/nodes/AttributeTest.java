package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        String key = attribute.getKey();
        assertEquals("key", key);
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        String newKey = attribute.getKey();
        assertEquals("newKey", newKey);
    }

    @Test
    public void testGetValue() {
        String value = attribute.getValue();
        assertEquals("value", value);
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        String newValue = attribute.getValue();
        assertEquals("newValue", newValue);
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToString() {
        String str = attribute.toString();
        assertEquals("key=\"value\"", str);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "&quot;encodedValue&quot;");
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("\"encodedValue\"", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        boolean isData = attribute.isDataAttribute();
        assertFalse(isData);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
        assertFalse(shouldCollapse);
    }

    @Test
    public void testIsBooleanAttribute() {
        boolean isBoolean = attribute.isBooleanAttribute();
        assertFalse(isBoolean);
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(equalAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

}