import org.junit.Test;
import org.jsoup.nodes.DocumentType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html>", accum.toString());
        
        doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "", "");
        accum = new StringBuilder();
        doctype.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">", accum.toString());
        
        doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "");
        accum = new StringBuilder();
        doctype.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }
}