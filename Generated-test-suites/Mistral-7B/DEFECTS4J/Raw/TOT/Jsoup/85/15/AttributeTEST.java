package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.PutAttribute;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributeTest {

    private Document doc;

    @Before
    public void setUp() {
        try {
            doc = new Document();
        } catch (IOException e) {
            fail("Failed to create new Document instance: " + e.getMessage());
        }
    }

    @Test
    public void testNewAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testNewAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.put(attribute.getKey(), attribute));
        assertEquals(attribute, parent.get(attribute.getKey()));
    }

    @Test
    public void testGetSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        assertEquals("value", attribute.getValue());
        attribute.setValue(newValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, doc.outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testCreateFromEncodedWithValidData() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedWithInvalidData() {
        Attribute.createFromEncoded("key", "invalid");
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key"));
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        String html = "<html data-key=\"value\" data-key2></html>";
        Document parsedDoc = Jsoup.parse(html);
        Attribute attribute = parsedDoc.selectFirst("html[data-key]").attr("data-key");
        assertTrue(attribute.shouldCollapseAttribute(doc.outputSettings()));
        assertEquals("value", attribute.getValue());

        attribute = parsedDoc.selectFirst("html[data-key2]").attr("data-key2");
        assertFalse(attribute.shouldCollapseAttribute(doc.outputSettings()));
        assertEquals("data-key2", attribute.getKey());
        assertEquals("", attribute.getValue());

        attribute = parsedDoc.selectFirst("html[data-key=\"\"]").attr("data-key");
        assertTrue(attribute.shouldCollapseAttribute(doc.outputSettings()));
        assertEquals("", attribute.getValue());

        attribute = parsedDoc.selectFirst("html[data-key=key]").attr("data-key");
        assertTrue(attribute.shouldCollapseAttribute(doc.outputSettings()));
        assertEquals("key", attribute.getKey());
        assertEquals("key", attribute.getValue());

        attribute = parsedDoc.selectFirst("html[data-key=key]").attr("data-key2");
        assertFalse(attribute.shouldCollapseAttribute(doc.outputSettings()));
        assertEquals("data-key2", attribute.getKey());
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        for (String booleanAttribute : Attribute.booleanAttributes) {
            assertTrue(Attribute.isBooleanAttribute(booleanAttribute));
        }

        assertFalse(Attribute.isBooleanAttribute("otherAttribute"));
    }
}