import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("html", doctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://www.example.com", doctype.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctypeWithPublicId = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");
        DocumentType doctypeWithSystemId = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctypeWithBothIds = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctypeWithoutIds = new DocumentType("html", null, null, "http://www.example.com");

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", doctypeWithPublicId.outerHtmlHead());
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">", doctypeWithSystemId.outerHtmlHead());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", doctypeWithBothIds.outerHtmlHead());
        assertEquals("<!DOCTYPE html>", doctypeWithoutIds.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("", doctype.outerHtmlTail());
    }

    @Test
    public void testHas() {
        DocumentType doctypeWithPublicId = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");
        DocumentType doctypeWithSystemId = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctypeWithBothIds = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctypeWithoutIds = new DocumentType("html", null, null, "http://www.example.com");

        assertEquals(true, doctypeWithPublicId.has(DocumentType.PUBLIC_ID));
        assertEquals(false, doctypeWithPublicId.has(DocumentType.SYSTEM_ID));
        assertEquals(true, doctypeWithSystemId.has(DocumentType.SYSTEM_ID));
        assertEquals(false, doctypeWithSystemId.has(DocumentType.PUBLIC_ID));
        assertEquals(true, doctypeWithBothIds.has(DocumentType.PUBLIC_ID));
        assertEquals(true, doctypeWithBothIds.has(DocumentType.SYSTEM_ID));
        assertEquals(false, doctypeWithoutIds.has(DocumentType.PUBLIC_ID));
        assertEquals(false, doctypeWithoutIds.has(DocumentType.SYSTEM_ID));
    }
}