package org.jsoup.nodes;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        // Typical use case
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://example.com");
        assert docType.nodeName().equals("#doctype");
        assert docType.attr("name").equals("html");
        assert docType.attr("publicId").equals("-//W3C//DTD XHTML 1.0 Transitional//EN");
        assert docType.attr("systemId").equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        assert docType.baseUri().equals("http://example.com");
        
        // Edge case - Empty values
        DocumentType docType2 = new DocumentType("", "", "", "");
        assert docType2.nodeName().equals("#doctype");
        assert docType2.attr("name").isEmpty();
        assert docType2.attr("publicId").isEmpty();
        assert docType2.attr("systemId").isEmpty();
        assert docType2.baseUri().isEmpty();
        
        // Error scenario - Null values
        try {
            new DocumentType(null, null, null, null);
            assert false; // Should not reach here
        } catch (IllegalArgumentException e) {
            assert true; // Exception expected
        }
    }

    @Test
    public void testOuterHtmlHead() {
        // Typical use case - With both publicId and systemId
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://example.com");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assert accum.toString().equals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        
        // Typical use case - With publicId only
        DocumentType docType2 = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "", "http://example.com");
        StringBuilder accum2 = new StringBuilder();
        docType2.outerHtmlHead(accum2, 0, null);
        assert accum2.toString().equals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        
        // Edge case - With systemId only
        DocumentType docType3 = new DocumentType("html", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://example.com");
        StringBuilder accum3 = new StringBuilder();
        docType3.outerHtmlHead(accum3, 0, null);
        assert accum3.toString().equals("<!DOCTYPE html http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        
        // Edge case - No publicId or systemId
        DocumentType docType4 = new DocumentType("html", "", "", "http://example.com");
        StringBuilder accum4 = new StringBuilder();
        docType4.outerHtmlHead(accum4, 0, null);
        assert accum4.toString().equals("<!DOCTYPE html>");
    }

    @Test
    public void testOuterHtmlTail() {
        // No logic to test in this method
        assert true;
    }
}

