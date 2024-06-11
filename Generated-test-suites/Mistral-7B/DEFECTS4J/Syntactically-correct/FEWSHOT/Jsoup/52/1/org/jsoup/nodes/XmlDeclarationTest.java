package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclaration() {
        Attributes attr = new Attributes();
        attr.put("version", "1.0");
        attr.put("encoding", "UTF-8");

        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "", true);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("#processing-instruction", xmlProcessingInstruction.nodeName());

        assertEquals("xml", xmlDeclaration.name());
        assertEquals("xml", xmlProcessingInstruction.name());

        assertEquals("xml\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", xmlProcessingInstruction.getWholeDeclaration());

        Element xmlElement = new Element("root");
        xmlElement.append(xmlDeclaration);
        xmlElement.append(xmlProcessingInstruction);

        Document doc = new Document();
        doc.setBaseUri("http://example.com");
        doc.appendChild(xmlElement);

        assertEquals("<root><!--\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n-->\n<!DOCTYPE root SYSTEM \"root.dtd\">\n<root>", xmlElement.outerHtml());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlProcessingInstruction.toString());
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testEmptyXmlDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        assertEquals("xml\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml?>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        Attributes attr = new Attributes();
        attr.put("version", "1.0");
        xmlDeclaration.attributes = attr;

        assertEquals("xml version=\"1.0\"\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        Attributes attr = new Attributes();
        attr.put("encoding", "UTF-8");
        xmlDeclaration.attributes = attr;

        assertEquals("xml encoding=\"UTF-8\"\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml encoding=\"UTF-8\"?>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        Attributes attr = new Attributes();
        attr.put("version", "1.0");
        attr.put("encoding", "UTF-8");
        xmlDeclaration.attributes = attr;

        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlProcessingInstruction() {
        XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "", true);

        assertEquals("<?xml?>\n", xmlProcessingInstruction.getWholeDeclaration());
        assertNull(xmlProcessingInstruction.name());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        new XmlDeclaration(null, "", false);
    }
}