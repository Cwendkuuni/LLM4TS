package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

import java.io.IOException;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());

        XmlDeclaration xmlPiDeclaration = new XmlDeclaration("<?xml", null, true);
        assertEquals("#declaration", xmlPiDeclaration.nodeName());
        assertEquals("<?xml", xmlPiDeclaration.name());
        assertEquals("<?xml", xmlPiDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationNormalDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes = new Attributes();
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWholeDeclarationEmptyDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expected = "<declaration/>";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlDeclarationConstructorNullName() {
        Validate.notNull(new XmlDeclaration(null, null, false));
    }

    @Test
    public void testOuterHtmlHeadNormalDeclaration() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes = new Attributes();
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        Document document = new Document();
        Appendable accum = document.createAppender("UTF-8");
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHeadProcessingInstruction() throws IOException {
        XmlDeclaration xmlPiDeclaration = new XmlDeclaration("<?xml", null, true);
        Document document = new Document();
        Appendable accum = document.createAppender("UTF-8");
        xmlPiDeclaration.outerHtmlHead(accum, 0, null);
        String expected = "<?xml>";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadNullAccumulator() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Document document = new Document();
        Appendable accum = document.createAppender("UTF-8");
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        String expected = "";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes = new Attributes();
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
        String actual = xmlDeclaration.toString();
        assertEquals(expected, actual);
    }
}