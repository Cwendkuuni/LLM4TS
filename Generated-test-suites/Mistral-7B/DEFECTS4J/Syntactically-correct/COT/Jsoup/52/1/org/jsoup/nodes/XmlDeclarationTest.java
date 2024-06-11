package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
        Assert.assertEquals("xml", xmlDeclaration.name());
        Assert.assertFalse(xmlDeclaration.isProcessingInstruction());
    }

    @Test
    public void testXmlDeclarationConstructorWithProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("<?xml", null, true);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
        Assert.assertEquals("<?xml", xmlDeclaration.name());
        Assert.assertTrue(xmlDeclaration.isProcessingInstruction());
    }

    @Test
    public void testGetWholeDeclarationEmpty() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Assert.assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Map<String, String> attributes = new HashMap<>();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        xmlDeclaration.attributes = attributes;
        Assert.assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"></declaration>", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testGetWholeDeclarationNullName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWholeDeclarationInvalidDeclarationName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("abcd", null, false);
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Assert.assertEquals("<declaration></declaration>", xmlDeclaration.toString());

        xmlDeclaration = new XmlDeclaration("<?xml", null, true);
        Assert.assertEquals("<?xml>", xmlDeclaration.toString());

        xmlDeclaration = new XmlDeclaration("xml", null, false);
        Map<String, String> attributes = new HashMap<>();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        xmlDeclaration.attributes = attributes;
        Assert.assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"></declaration>", xmlDeclaration.toString());
    }
}