package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attribute;
import org.jsoup.nodes.attributes.Attributes;
import org.jsoup.nodes.attributes.IAttribute;
import org.jsoup.parser.Parser;

public class DocumentTypeTest {

    private Document doc;
    private DocumentType doctype;

    @Before
    public void setup() {
        doc = new Document();
        doctype = new DocumentType("html", "HTML", "DTD HTML 4.01 Frameset", doc.baseUri());
    }

    @Test
    public void testDocumentTypeCreation() {
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.getName());
        assertFalse(doctype.hasPublicId());
        assertFalse(doctype.hasSystemId());
        assertEquals("DTD HTML 4.01 Frameset", doctype.outerHtml());
    }

    @Test
    public void testDocumentTypeWithPublicId() {
        DocumentType doctypeWithPublicId = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01 Frameset", doc.baseUri());
        assertTrue(doctypeWithPublicId.hasPublicId());
        assertTrue(doctypeWithPublicId.hasSystemId());
        assertEquals("!DOCTYPE html PUBLIC \"HTML_PUBLIC - DTD HTML 4.01 Frameset\"", doctypeWithPublicId.outerHtml());
    }

    @Test
    public void testDocumentTypeWithSystemId() {
        DocumentType doctypeWithSystemId = new DocumentType("html", "", "DTD HTML 4.01 Frameset", doc.baseUri() + "/dtds/html.dtd");
        assertTrue(doctypeWithSystemId.hasPublicId());
        assertTrue(doctypeWithSystemId.hasSystemId());
        assertEquals("!DOCTYPE html SYSTEM \"/dtds/html.dtd\"", doctypeWithSystemId.outerHtml());
    }

    @Test
    public void testDocumentTypeWithBothPublicIdAndSystemId() {
        DocumentType doctypeWithPublicAndSystemId = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01 Frameset", doc.baseUri() + "/dtds/html.dtd");
        assertTrue(doctypeWithPublicAndSystemId.hasPublicId());
        assertTrue(doctypeWithPublicAndSystemId.hasSystemId());
        assertEquals("!DOCTYPE html PUBLIC \"HTML_PUBLIC - DTD HTML 4.01 Frameset\" SYSTEM \"/dtds/html.dtd\"", doctypeWithPublicAndSystemId.outerHtml());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Element body = doc.createElement("body");
        doc.appendChild(body);
        Parser parser = new Parser();
        String html = "<!DOCTYPE html PUBLIC \"HTML_PUBLIC - DTD HTML 4.01 Frameset\" SYSTEM \"/dtds/html.dtd\">";
        Document parsedHtmlDoc = parser.parse(html);
        DocumentType parsedDoctype = parsedHtmlDoc.getElementsByTag("!DOCTYPE").first();

        Appendable accum = mock(Appendable.class);
        when(accum.append(String.class)).thenReturn(accum);

        doctype.outerHtmlHead(accum, 0, OutputSettings.html5());

        String expected = "<!doctype html>\n";
        assertEquals(expected, accum.toString());

        parsedDoctype.outerHtmlHead(accum, 0, OutputSettings.html4());
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC - DTD HTML 4.01 Frameset\" SYSTEM \"/dtds/html.dtd\">\n", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Appendable accum = mock(Appendable.class);
        when(accum.append(String.class)).thenReturn(accum);

        doctype.outerHtmlTail(accum, 0, OutputSettings.html5());
        String expected = "";
        assertEquals(expected, accum.toString());

        doc.setBaseUri("/path/to/baseUri");
        doctype = new DocumentType("html", "", "", doc.baseUri());
        doctype.outerHtmlTail(accum, 0, OutputSettings.html5());
        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    @Test
    public void testHasAttribute() {
        Attributes attrs = new Attributes();
        attrs.put("name", "value");
        doctype = new DocumentType("html", "", "", doc.baseUri());
        doctype.attr(new Attribute("name", "value"));
        assertTrue(doctype.has("name"));
        assertFalse(doctype.has("otherName"));
    }

    @Test
    public void testNodeNameCaseSensitivity() {
        DocumentType lowercaseDoctype = new DocumentType("HTML", "", "", doc.baseUri());
        assertEquals("#doctype", lowercaseDoctype.nodeName());

        DocumentType uppercaseDoctype = new DocumentType("HTML", "", "", doc.baseUri());
        assertEquals("#doctype", uppercaseDoctype.nodeName());
    }
}