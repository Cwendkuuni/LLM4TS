import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead_blankAttributes() {
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHead_withName() {
        documentType.attr("name", "html");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHead_withPublicId() {
        documentType.attr("publicId", "-//W3C//DTD HTML 4.01 Transitional//EN");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHead_withSystemId() {
        documentType.attr("systemId", "http://www.w3.org/TR/html4/loose.dtd");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/loose.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("", stringBuilder.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_emptyName() {
        DocumentType invalidDocumentType = new DocumentType("", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://example.com");
    }

    @Test
    public void testConstructor_noPublicId() {
        DocumentType validDocumentType = new DocumentType("html", null, "http://www.w3.org/TR/html4/loose.dtd", "https://example.com");
        assertEquals("", validDocumentType.attr("publicId"));
    }

    @Test
    public void testConstructor_noSystemId() {
        DocumentType validDocumentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "https://example.com");
        assertEquals("", validDocumentType.attr("systemId"));
    }

    @Test
    public void testConstructor_emptyBaseUri() {
        DocumentType validDocumentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "");
        assertEquals("", validDocumentType.baseUri());
    }
}