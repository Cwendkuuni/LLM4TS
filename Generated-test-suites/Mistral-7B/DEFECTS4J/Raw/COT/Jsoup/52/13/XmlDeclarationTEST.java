package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.jsoup.helper.StringUtil;

import java.io.IOException;

/**
 * Tests for XmlDeclaration class
 */
public class XmlDeclarationTest {

    @Test
    public void testConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expected = "#declaration";
        String actual = xmlDeclaration.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructorWithName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xmlVersion", null, false);
        String expected = "xmlVersion";
        String actual = xmlDeclaration.name();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructorWithProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        String expected = "#processing-instruction('<?xml '";
        String actual = xmlDeclaration.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWholeDeclarationEmpty() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expected = "#declaration";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWholeDeclarationVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes = new Attributes();
        xmlDeclaration.attributes.put("version", "1.0");
        String expected = "#declaration version=\"1.0\"";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWholeDeclarationEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes = new Attributes();
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String expected = "#declaration encoding=\"UTF-8\"";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWholeDeclarationVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes = new Attributes();
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String expected = "#declaration version=\"1.0\" encoding=\"UTF-8\"";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
        Validate.isTrue(false, "Name should not be null");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Appendable accum = StringUtil.createAppendable();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        String expected = "<xml version=\"1.0\" encoding=\"UTF-8\">";
        String actual = StringUtil.removeStart(accum.toString(), "\n");
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHeadWithProcessingInstruction() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        Appendable accum = StringUtil.createAppendable();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        String expected = "<?xml ?>";
        String actual = StringUtil.removeStart(accum.toString(), "\n");
        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadWithNullAccumulator() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expected = "<xml version=\"1.0\" encoding=\"UTF-8\">";
        String actual = xmlDeclaration.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        String expected = "<?xml ?>";
        String actual = xmlDeclaration.toString();
        assertEquals(expected, actual);
    }
}