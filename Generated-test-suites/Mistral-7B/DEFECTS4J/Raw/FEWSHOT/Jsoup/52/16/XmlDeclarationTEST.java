package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml?>", xmlDeclaration.toString());

        XmlDeclaration xmlDeclarationVersion = new XmlDeclaration("xml", null, false);
        xmlDeclarationVersion.attributes = new Attributes();
        xmlDeclarationVersion.attributes.put("version", "1.0");
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclarationVersion.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclarationVersion.toString());

        XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
        assertEquals("<?xml?>", xmlProcessingInstruction.getWholeDeclaration());
        assertEquals("<?xml?>", xmlProcessingInstruction.toString());

        XmlDeclaration xmlDeclarationEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationEncoding.attributes = new Attributes();
        xmlDeclarationEncoding.attributes.put("encoding", "UTF-8");
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclarationEncoding.getWholeDeclaration());
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclarationEncoding.toString());

        XmlDeclaration xmlDeclarationVersionEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationVersionEncoding.attributes = new Attributes();
        xmlDeclarationVersionEncoding.attributes.put("version", "1.0");
        xmlDeclarationVersionEncoding.attributes.put("encoding", "UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclarationVersionEncoding.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclarationVersionEncoding.toString());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Document document = new Document();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Element xmlDeclarationElement = document.createElement(xmlDeclaration.nodeName());
        xmlDeclarationElement.node = xmlDeclaration;
        xmlDeclarationElement.outerHtmlHead(document.body().appendChild(xmlDeclarationElement), 0, null);
        assertEquals("<?xml?>", document.body().text());

        XmlDeclaration xmlDeclarationVersion = new XmlDeclaration("xml", null, false);
        xmlDeclarationVersion.attributes = new Attributes();
        xmlDeclarationVersion.attributes.put("version", "1.0");
        Element xmlDeclarationVersionElement = document.createElement(xmlDeclarationVersion.nodeName());
        xmlDeclarationVersionElement.node = xmlDeclarationVersion;
        xmlDeclarationVersionElement.outerHtmlHead(document.body().appendChild(xmlDeclarationVersionElement), 0, null);
        assertEquals("<?xml version=\"1.0\"?>", document.body().text());

        XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
        Element xmlProcessingInstructionElement = document.createElement(xmlProcessingInstruction.nodeName());
        xmlProcessingInstructionElement.node = xmlProcessingInstruction;
        xmlProcessingInstructionElement.outerHtmlHead(document.body().appendChild(xmlProcessingInstructionElement), 0, null);
        assertEquals("<?xml?>", document.body().text());

        XmlDeclaration xmlDeclarationEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationEncoding.attributes = new Attributes();
        xmlDeclarationEncoding.attributes.put("encoding", "UTF-8");
        Element xmlDeclarationEncodingElement = document.createElement(xmlDeclarationEncoding.nodeName());
        xmlDeclarationEncodingElement.node = xmlDeclarationEncoding;
        xmlDeclarationEncodingElement.outerHtmlHead(document.body().appendChild(xmlDeclarationEncodingElement), 0, null);
        assertEquals("<?xml encoding=\"UTF-8\"?>", document.body().text());

        XmlDeclaration xmlDeclarationVersionEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationVersionEncoding.attributes = new Attributes();
        xmlDeclarationVersionEncoding.attributes.put("version", "1.0");
        xmlDeclarationVersionEncoding.attributes.put("encoding", "UTF-8");
        Element xmlDeclarationVersionEncodingElement = document.createElement(xmlDeclarationVersionEncoding.nodeName());
        xmlDeclarationVersionEncodingElement.node = xmlDeclarationVersionEncoding;
        xmlDeclarationVersionEncodingElement.outerHtmlHead(document.body().appendChild(xmlDeclarationVersionEncodingElement), 0, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", document.body().text());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Document document = new Document();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Element xmlDeclarationElement = document.createElement(xmlDeclaration.nodeName());
        xmlDeclarationElement.node = xmlDeclaration;
        xmlDeclarationElement.outerHtmlHead(document.body().appendChild(xmlDeclarationElement), 0, null);
        xmlDeclarationElement.outerHtmlTail(document.body().appendChild(xmlDeclarationElement), 0, null);
        assertEquals("<?xml?>", document.body().text());

        XmlDeclaration xmlDeclarationVersion = new XmlDeclaration("xml", null, false);
        xmlDeclarationVersion.attributes = new Attributes();
        xmlDeclarationVersion.attributes.put("version", "1.0");
        Element xmlDeclarationVersionElement = document.createElement(xmlDeclarationVersion.nodeName());
        xmlDeclarationVersionElement.node = xmlDeclarationVersion;
        xmlDeclarationVersionElement.outerHtmlHead(document.body().appendChild(xmlDeclarationVersionElement), 0, null);
        xmlDeclarationVersionElement.outerHtmlTail(document.body().appendChild(xmlDeclarationVersionElement), 0, null);
        assertEquals("<?xml version=\"1.0\"?>", document.body().text());

        XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
        Element xmlProcessingInstructionElement = document.createElement(xmlProcessingInstruction.nodeName());
        xmlProcessingInstructionElement.node = xmlProcessingInstruction;
        xmlProcessingInstructionElement.outerHtmlHead(document.body().appendChild(xmlProcessingInstructionElement), 0, null);
        xmlProcessingInstructionElement.outerHtmlTail(document.body().appendChild(xmlProcessingInstructionElement), 0, null);
        assertEquals("<?xml?>", document.body().text());

        XmlDeclaration xmlDeclarationEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationEncoding.attributes = new Attributes();
        xmlDeclarationEncoding.attributes.put("encoding", "UTF-8");
        Element xmlDeclarationEncodingElement = document.createElement(xmlDeclarationEncoding.nodeName());
        xmlDeclarationEncodingElement.node = xmlDeclarationEncoding;
        xmlDeclarationEncodingElement.outerHtmlHead(document.body().appendChild(xmlDeclarationEncodingElement), 0, null);
        xmlDeclarationEncodingElement.outerHtmlTail(document.body().appendChild(xmlDeclarationEncodingElement), 0, null);
        assertEquals("<?xml encoding=\"UTF-8\"?>", document.body().text());

        XmlDeclaration xmlDeclarationVersionEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationVersionEncoding.attributes = new Attributes();
        xmlDeclarationVersionEncoding.attributes.put("version", "1.0");
        xmlDeclarationVersionEncoding.attributes.put("encoding", "UTF-8");
        Element xmlDeclarationVersionEncodingElement = document.createElement(xmlDeclarationVersionEncoding.nodeName());
        xmlDeclarationVersionEncodingElement.node = xmlDeclarationVersionEncoding;
        xmlDeclarationVersionEncodingElement.outerHtmlHead(document.body().appendChild(xmlDeclarationVersionEncodingElement), 0, null);
        xmlDeclarationVersionEncodingElement.outerHtmlTail(document.body().appendChild(xmlDeclarationVersionEncodingElement), 0, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", document.body().text());

        Element emptyElement = document.createElement("#declaration");
        emptyElement.outerHtmlHead(document.body().appendChild(emptyElement), 0, null);
        emptyElement.outerHtmlTail(document.body().appendChild(emptyElement), 0, null);
        assertEquals("", document.body().text());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("<?xml?>", xmlDeclaration.toString());

        XmlDeclaration xmlDeclarationVersion = new XmlDeclaration("xml", null, false);
        xmlDeclarationVersion.attributes = new Attributes();
        xmlDeclarationVersion.attributes.put("version", "1.0");
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclarationVersion.toString());

        XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
        assertEquals("<?xml?>", xmlProcessingInstruction.toString());

        XmlDeclaration xmlDeclarationEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationEncoding.attributes = new Attributes();
        xmlDeclarationEncoding.attributes.put("encoding", "UTF-8");
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclarationEncoding.toString());

        XmlDeclaration xmlDeclarationVersionEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationVersionEncoding.attributes = new Attributes();
        xmlDeclarationVersionEncoding.attributes.put("version", "1.0");
        xmlDeclarationVersionEncoding.attributes.put("encoding", "UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclarationVersionEncoding.toString());

        Element emptyElement = document.createElement("#declaration");
        assertEquals("", emptyElement.toString());
    }

    @Test
    public void testNullAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertNull(xmlDeclaration.attributes);
    }
}