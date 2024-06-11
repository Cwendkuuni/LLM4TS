package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "-//W3C//DTD HTML 4.01//EN";
        String baseUri = "http://example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNameIsEmpty() {
        String name = "";
        String publicId = "HTML";
        String systemId = "-//W3C//DTD HTML 4.01//EN";
        String baseUri = "http://example.com";
        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML//W3C//DTD HTML 4.01//EN\" >", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertNull(accum.toString());
    }

    @Test
    public void testDocumentTypeWithPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType);

        Node child = document.child(0);
        assertEquals(DocumentType.class, child.getClass());

        DocumentType childDocumentType = (DocumentType) child;
        assertEquals("html", childDocumentType.attr("name"));
        assertEquals("HTML PUBLIC", childDocumentType.attr("publicId"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", childDocumentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeWithOnlyPublicId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML", null, null);
        document.appendChild(documentType);

        Node child = document.child(0);
        assertEquals(DocumentType.class, child.getClass());

        DocumentType childDocumentType = (DocumentType) child;
        assertEquals("html", childDocumentType.attr("name"));
        assertNull(childDocumentType.attr("publicId"));
        assertNull(childDocumentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeWithOnlySystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType);

        Node child = document.child(0);
        assertEquals(DocumentType.class, child.getClass());

        DocumentType childDocumentType = (DocumentType) child;
        assertEquals("html", childDocumentType.attr("name"));
        assertNull(childDocumentType.attr("publicId"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", childDocumentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeWithEmptyPublicId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType);

        Node child = document.child(0);
        assertEquals(DocumentType.class, child.getClass());

        DocumentType childDocumentType = (DocumentType) child;
        assertEquals("html", childDocumentType.attr("name"));
        assertNull(childDocumentType.attr("publicId"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", childDocumentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeWithEmptySystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML", null, null);
        document.appendChild(documentType);

        Node child = document.child(0);
        assertEquals(DocumentType.class, child.getClass());

        DocumentType childDocumentType = (DocumentType) child;
        assertEquals("html", childDocumentType.attr("name"));
        assertNull(childDocumentType.attr("publicId"));
        assertEquals("", childDocumentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeWithNullBaseUri() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType);

        assertEquals("http://", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeWithBaseUri() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", "http://example.com");
        document.appendChild(documentType);

        assertEquals("http://example.com", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType);

        assertEquals("#doctype html\n", documentType.toString());
    }

    @Test
    public void testDocumentTypeEquals() {
        Document document = new Document();
        DocumentType documentType1 = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", null);
        DocumentType documentType2 = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType1);
        document.appendChild(documentType2);

        Document document2 = new Document();
        DocumentType documentType3 = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", null);
        document2.appendChild(documentType3);

        assertEquals(documentType1, documentType1);
        assertEquals(documentType1, documentType2);
        assertEquals(documentType1, documentType3);

        assertNotSame(documentType1, documentType1);
        assertNotSame(documentType1, documentType4());
        assertNotSame(documentType1, new Object());
    }

    @Test
    public void testDocumentTypeCloneNode() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML", "-//W3C//DTD HTML 4.01//EN", null);
        document.appendChild(documentType);

        DocumentType clonedDocumentType = (DocumentType) documentType.cloneNode(true);

        assertEquals(documentType.attr("name"), clonedDocumentType.attr("name"));
        assertEquals(documentType.attr("publicId"), clonedDocumentType.attr("publicId"));
        assertEquals(documentType.attr("systemId"), clonedDocumentType.attr("systemId"));
        assertEquals(documentType.baseUri(), clonedDocumentType.baseUri());
    }

    @Test
    public void testDocumentTypeParse() {
        Document document = new Document();
        Element root = new Element("html");
        document.setRootElement(root);

        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        Parser parser = new Parser();
        parser.parseInto(document, html);

        DocumentType documentType = (DocumentType) document.child(0);
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML PUBLIC", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
    }

    private DocumentType documentType4() {
        return new DocumentType("html4", "HTML4", "-//W3C//DTD HTML 4.01//EN", null);
    }
}