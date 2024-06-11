import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.helper.DataUtil;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr("systemId"));
        assertEquals("http://example.com", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
}