package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.jsoup.helper.ValidateBody;

import java.io.IOException;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
        Assert.assertEquals("xml", xmlDeclaration.name());

        XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "baseUri", true);
        Assert.assertEquals("#declaration", xmlProcessingInstruction.nodeName());
        Assert.assertEquals("xml", xmlProcessingInstruction.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        String expected = "<xml>";
        String actual = xmlDeclaration.getWholeDeclaration();
        Assert.assertEquals(expected, actual);

        XmlDeclaration xmlVersionDeclaration = new XmlDeclaration("xml", "baseUri", false);
        xmlVersionDeclaration.attributes().set("version", "1.0");
        expected = "<xml version=\"1.0\">";
        actual = xmlVersionDeclaration.getWholeDeclaration();
        Assert.assertEquals(expected, actual);

        XmlDeclaration xmlEncodingDeclaration = new XmlDeclaration("xml", "baseUri", false);
        xmlEncodingDeclaration.attributes().set("encoding", "UTF-8");
        expected = "<xml encoding=\"UTF-8\">";
        actual = xmlEncodingDeclaration.getWholeDeclaration();
        Assert.assertEquals(expected, actual);

        XmlDeclaration xmlProcessingInstructionDeclaration = new XmlDeclaration("xml", "baseUri", true);
        expected = "<?xml?>";
        actual = xmlProcessingInstructionDeclaration.getWholeDeclaration();
        Assert.assertEquals(expected, actual);

        XmlDeclaration xmlVersionProcessingInstructionDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlVersionProcessingInstructionDeclaration.attributes().set("version", "1.0");
        expected = "<?xml version=\"1.0\"?>";
        actual = xmlVersionProcessingInstructionDeclaration.getWholeDeclaration();
        Assert.assertEquals(expected, actual);

        XmlDeclaration xmlEncodingProcessingInstructionDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlEncodingProcessingInstructionDeclaration.attributes().set("encoding", "UTF-8");
        expected = "<?xml encoding=\"UTF-8\"?>";
        actual = xmlEncodingProcessingInstructionDeclaration.getWholeDeclaration();
        Assert.assertEquals(expected, actual);

        XmlDeclaration xmlDeclarationWithBothVersionAndEncoding = new XmlDeclaration("xml", "baseUri", false);
        xmlDeclarationWithBothVersionAndEncoding.attributes().set("version", "1.0");
        xmlDeclarationWithBothVersionAndEncoding.attributes().set("encoding", "UTF-8");
        expected = "<xml version=\"1.0\" encoding=\"UTF-8\">";
        actual = xmlDeclarationWithBothVersionAndEncoding.getWholeDeclaration();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationConstructorWithNullName() {
        new XmlDeclaration(null, "baseUri", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationConstructorWithEmptyName() {
        new XmlDeclaration("", "baseUri", false);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWholeDeclarationWithNullXmlDeclaration() {
        XmlDeclaration xmlDeclaration = null;
        String actual = xmlDeclaration.getWholeDeclaration();
        Assert.assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWholeDeclarationWithEmptyXmlDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", "baseUri", false);
        String actual = xmlDeclaration.getWholeDeclaration();
        Assert.assertEquals("", actual);
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        String expected = xmlDeclaration.getWholeDeclaration();
        String actual = xmlDeclaration.toString();
        Assert.assertEquals(expected, actual);
    }
}