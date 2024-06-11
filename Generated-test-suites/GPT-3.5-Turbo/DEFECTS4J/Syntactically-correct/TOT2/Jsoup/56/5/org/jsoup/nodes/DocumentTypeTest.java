import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        assertNotNull(doctype);
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", sb.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        assertTrue(doctype.has("name"));
        assertTrue(doctype.has("publicId"));
        assertTrue(doctype.has("systemId"));
        assertFalse(doctype.has("otherAttribute"));
    }

    // Add more test cases for edge cases and exception handling scenarios

}