package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", null, false);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        final Map<String, String> attributes = new HashMap<>();
        attributes.put("version", "1.0");
        xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().putAll(attributes);
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());

        final Map<String, String> attributes2 = new HashMap<>();
        attributes2.put("version", "1.0");
        attributes2.put("encoding", "UTF-8");
        xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().putAll(attributes2);
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("declaration1", null, false);
        assertEquals("declaration1", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertNull(xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        assertEquals("<xml>", xmlDeclaration.toString());

        final Map<String, String> attributes = new HashMap<>();
        attributes.put("version", "1.0");
        xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().putAll(attributes);
        assertEquals("<xml version=\"1.0\"/>", xmlDeclaration.toString());

        final Map<String, String> attributes2 = new HashMap<>();
        attributes2.put("version", "1.0");
        attributes2.put("encoding", "UTF-8");
        xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().putAll(attributes2);
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.toString());

        xmlDeclaration = new XmlDeclaration("declaration1", null, false);
        assertEquals("<declaration1>", xmlDeclaration.toString());

        xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertNull(xmlDeclaration.toString());
    }

    @Test
    public void testOuterHtmlHead() {
        final Document document = new Document();
        final Appendable appendable = document.bodyAppender();
        xmlDeclaration.outerHtmlHead(appendable, 0, Document.OutputSettings.defaultOutputSettings());
        assertEquals("<!xml>", appendable.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        final Document document = new Document();
        final Appendable appendable = document.bodyAppender();
        xmlDeclaration.outerHtmlTail(appendable, 0, Document.OutputSettings.defaultOutputSettings());
        assertEquals("", appendable.toString());
    }

    @Test
    public void testEquals() {
        final XmlDeclaration xmlDeclaration1 = new XmlDeclaration("xml", null, false);
        final XmlDeclaration xmlDeclaration2 = new XmlDeclaration("xml", null, false);
        final XmlDeclaration xmlDeclaration3 = new XmlDeclaration("xml", null, true);
        final XmlDeclaration xmlDeclaration4 = new XmlDeclaration("xml", null, false);
        final XmlDeclaration xmlDeclaration5 = new XmlDeclaration("xml", null, false);

        assertSame(xmlDeclaration1, xmlDeclaration1);
        assertSame(xmlDeclaration1, xmlDeclaration4);
        assertSame(xmlDeclaration1, xmlDeclaration5);
        assertNotSame(xmlDeclaration1, xmlDeclaration2);
        assertNotSame(xmlDeclaration1, xmlDeclaration3);
    }

    @Test
    public void testHashCode() {
        final XmlDeclaration xmlDeclaration1 = new XmlDeclaration("xml", null, false);
        final XmlDeclaration xmlDeclaration2 = new XmlDeclaration("xml", null, false);
        final XmlDeclaration xmlDeclaration3 = new XmlDeclaration("xml", null, true);
        final XmlDeclaration xmlDeclaration4 = new XmlDeclaration("xml", null, false);
        final XmlDeclaration xmlDeclaration5 = new XmlDeclaration("xml", null, false);

        assertEquals(xmlDeclaration1.hashCode(), xmlDeclaration1.hashCode());
        assertEquals(xmlDeclaration1.hashCode(), xmlDeclaration4.hashCode());
        assertEquals(xmlDeclaration1.hashCode(), xmlDeclaration5.hashCode());
        assertNotEquals(xmlDeclaration1.hashCode(), xmlDeclaration2.hashCode());
        assertNotEquals(xmlDeclaration1.hashCode(), xmlDeclaration3.hashCode());
    }
}