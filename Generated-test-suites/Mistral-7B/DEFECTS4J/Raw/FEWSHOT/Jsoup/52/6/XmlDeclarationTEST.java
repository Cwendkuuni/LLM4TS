package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("version", "1.1");
        attrs.put("encoding", "UTF-8");

        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false, attrs);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml version=\"1.1\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        Element xmlElement = new Element("xml");
        xmlElement.appendText("Hello World");
        xmlElement.appendChild(xmlDeclaration);

        StringBuilder sb = new StringBuilder();
        try {
            xmlElement.outerHtml(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("<xml version=\"1.1\" encoding=\"UTF-8\">" +
                "<!--\n" +
                "<!--\n" +
                "xml version=\"1.1\" encoding=\"UTF-8\"\n" +
                "-->\n" +
                "Hello World" +
                "</xml>", sb.toString());
    }

    @Test
    public void testXmlDeclarationWithOnlyName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        Element xmlElement = new Element("xml");
        xmlElement.appendChild(xmlDeclaration);

        StringBuilder sb = new StringBuilder();
        try {
            xmlElement.outerHtml(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("<xml><!--\nxml-->\n</xml>", sb.toString());
    }

    @Test
    public void testXmlDeclarationAsProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());

        Element xmlElement = new Element("xml");
        xmlElement.appendChild(xmlDeclaration);

        StringBuilder sb = new StringBuilder();
        try {
            xmlElement.outerHtml(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("<?xml>\n", sb.toString());
    }

    @Test
    public void testXmlDeclarationWithNullName() {
        try {
            new XmlDeclaration(null, null, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new XmlDeclaration(null, null, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.getWholeDeclaration());

        Element xmlElement = new Element("");
        xmlElement.appendChild(xmlDeclaration);

        StringBuilder sb = new StringBuilder();
        try {
            xmlElement.outerHtml(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("<>" +
                "<!--\n<!--\n-->" +
                "-->\n", sb.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndNoAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        Element xmlElement = new Element("xml");
        xmlElement.appendChild(xmlDeclaration);

        StringBuilder sb = new StringBuilder();
        try {
            xmlElement.outerHtml(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("<xml><!--xml-->\n</xml>", sb.toString());
    }

    @Test
    public void testXmlDeclarationToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("<xml><!--xml-->\n", xmlDeclaration.toString());
    }
}