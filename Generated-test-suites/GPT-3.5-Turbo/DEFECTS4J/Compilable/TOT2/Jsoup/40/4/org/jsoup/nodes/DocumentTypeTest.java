package org.jsoup.nodes;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 3.2//EN", null, "http://www.w3.org/TR/REC-html32.dtd");
        assertEquals("html", documentType.nodeName());
        assertEquals("//W3C//DTD HTML 3.2//EN", documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testEmptyDoctypeName() {
        try {
            DocumentType documentType = new DocumentType("", "-//W3C//DTD HTML 3.2//EN", null, "http://www.w3.org/TR/REC-html32.dtd");
            fail("Empty doctype name should throw exception");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testOuterHtmlGeneration() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 3.2//EN", null, "http://www.w3.org/TR/REC-html32.dtd");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 3.2//EN\">", accum.toString());
    }

}