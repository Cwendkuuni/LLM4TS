package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertNotNull(doctype);
        assertEquals("name", doctype.attr("name"));
        assertEquals("publicId", doctype.attr("publicId"));
        assertEquals("systemId", doctype.attr("systemId"));
        
        DocumentType emptyDoctype = new DocumentType("", "", "", "");
        assertNotNull(emptyDoctype);
        assertEquals("", emptyDoctype.attr("name"));
        assertEquals("", emptyDoctype.attr("publicId"));
        assertEquals("", emptyDoctype.attr("systemId"));
        
        DocumentType nullDoctype = new DocumentType(null, null, null, null);
        assertNotNull(nullDoctype);
        assertNull(nullDoctype.attr("name"));
        assertNull(nullDoctype.attr("publicId"));
        assertNull(nullDoctype.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
        
        StringBuilder accumEmpty = new StringBuilder();
        doctype.outerHtmlHead(accumEmpty, depth, out);
        assertEquals("<!DOCTYPE html >", accumEmpty.toString());
        
        StringBuilder accumNull = new StringBuilder();
        Document.OutputSettings outNull = null;
        doctype.outerHtmlHead(accumNull, depth, outNull);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accumNull.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }

}