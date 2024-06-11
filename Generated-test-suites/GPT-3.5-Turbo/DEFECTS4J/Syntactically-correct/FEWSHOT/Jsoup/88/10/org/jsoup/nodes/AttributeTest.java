package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class AttributeTest {
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("class", "container");
        assertNotNull(attribute);
        assertEquals("class", attribute.getKey());
        assertEquals("container", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("class", "container");
        attribute.setKey("id");
        assertEquals("id", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("class", "container");
        attribute.setValue("wrapper");
        assertEquals("wrapper", attribute.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("href", "https://www.example.com");
        String html = attribute.html();
        assertNotNull(html);
        assertEquals("href=\"https://www.example.com\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("href", "https://www.example.com");
        String str = attribute.toString();
        assertNotNull(str);
        assertEquals("href=\"https://www.example.com\"", str);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("href", "https://www.example.com");
        assertNotNull(attribute);
        assertEquals("href", attribute.getKey());
        assertEquals("https://www.example.com", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-src", "https://www.example.com");
        boolean isDataAttribute = attribute.isDataAttribute();
        assertEquals(true, isDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("autofocus", null);
        Attribute attribute2 = new Attribute("class", "container");
        boolean shouldCollapse1 = attribute1.shouldCollapseAttribute(new Document.OutputSettings());
        boolean shouldCollapse2 = attribute2.shouldCollapseAttribute(new Document.OutputSettings());
        assertEquals(true, shouldCollapse1);
        assertEquals(false, shouldCollapse2);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("disabled", null);
        Attribute attribute2 = new Attribute("class", "container");
        boolean isBooleanAttribute1 = attribute1.isBooleanAttribute();
        boolean isBooleanAttribute2 = attribute2.isBooleanAttribute();
        assertEquals(true, isBooleanAttribute1);
        assertEquals(false, isBooleanAttribute2);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("class", "container");
        Attribute attribute2 = new Attribute("class", "container");
        Attribute attribute3 = new Attribute("id", "wrapper");
        assertEquals(true, attribute1.equals(attribute2));
        assertEquals(false, attribute1.equals(attribute3));
        assertEquals(false, attribute1.equals(null));
        assertEquals(false, attribute1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("class", "container");
        int hashCode = attribute.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("class", "container");
        Attribute clone = attribute.clone();
        assertNotNull(clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
}