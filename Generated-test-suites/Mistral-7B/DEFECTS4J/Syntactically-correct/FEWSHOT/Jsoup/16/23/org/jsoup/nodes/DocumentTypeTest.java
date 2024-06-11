package org.jsoup.nodes;

import org.junit.Assert;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document document = new Document();
        DocumentType doctype = new DocumentType(document, "#doctype", "", "");
        Assert.assertEquals("#doctype", doctyp.nodeName());
        Assert.assertNull(doctype.attr("publicId"));
        Assert.assertNull(doctype.attr("systemId"));
        Assert.assertEquals("", doctype.attr("baseUri"));
    }

    @Test
    public void testDocumentTypeWithPublicId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType(document, "-//W3C//DTD XHTML 1.0 Strict//EN", "XHTML1-20000101", "");
        Assert.assertEquals("#doctype", doctype.nodeName());
        Assert.assertEquals("XHTML1-20000101", doctype.attr("publicId"));
        Assert.assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.toString());
    }

    @Test
    public void testDocumentTypeWithSystemId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType(document, "", "about:blank", "");
        Assert.assertEquals("#doctype", doctype.nodeName());
        Assert.assertEquals("about:blank", doctype.attr("systemId"));
        Assert.assertEquals("", doctype.attr("publicId"));
    }

    @Test
    public void testDocumentTypeWithPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType(document, "-//W3C//DTD XHTML 1.0 Strict//EN", "XHTML1-20000101.dtd", "");
        Assert.assertEquals("#doctype", doctype.nodeName());
        Assert.assertEquals("XHTML1-20000101.dtd", doctype.attr("systemId"));
        Assert.assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.toString());
    }

    @Test
    public void testDocumentTypeToString() {
        Document document = new Document();
        DocumentType doctype = new DocumentType(document, "-//W3C//DTD XHTML 1.0 Strict//EN", "XHTML1-20000101", "");
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"XHTML1-20000101.dtd\">", doctype.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document document = new Document();
        DocumentType doctype = new DocumentType(document, "-//W3C//DTD XHTML 1.0 Strict//EN", "XHTML1-20000101", "");
        StringBuilder accumulator = new StringBuilder();
        doctype.outerHtmlHead(accumulator, 0, null);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"XHTML1-20000101.dtd\">", accumulator.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document document = new Document();
        DocumentType doctype = new DocumentType(document, "-//W3C//DTD XHTML 1.0 Strict//EN", "XHTML1-20000101", "");
        StringBuilder accumulator = new StringBuilder();
        doctype.outerHtmlTail(accumulator, 0, null);
        Assert.assertEquals("", accumulator.toString());
    }

    @Test
    public void testParseDocumentType() {
        Document document = new Document();
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"XHTML1-20000101.dtd\">";
        Element element = Parser.parseBodyFragment(html, null);
        DocumentType doctype = (DocumentType) element;
        Assert.assertEquals("#doctype", doctype.nodeName());
        Assert.assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.attr("publicId"));
        Assert.assertEquals("XHTML1-20000101.dtd", doctype.attr("systemId"));
    }
}