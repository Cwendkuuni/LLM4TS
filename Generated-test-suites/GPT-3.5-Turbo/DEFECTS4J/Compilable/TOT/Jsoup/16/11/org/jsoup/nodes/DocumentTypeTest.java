package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org");
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", docType.attr("systemId"));
    }
    
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org");
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "", "");
        StringBuilder stringBuilder = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        docType.outerHtmlHead(stringBuilder, depth, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">", stringBuilder.toString());
    }
    
     @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "");
        StringBuilder stringBuilder = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        docType.outerHtmlHead(stringBuilder, depth, outputSettings);
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", stringBuilder.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "");
        StringBuilder stringBuilder = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        docType.outerHtmlTail(stringBuilder, depth, outputSettings);
        assertEquals("", stringBuilder.toString());
    }
}