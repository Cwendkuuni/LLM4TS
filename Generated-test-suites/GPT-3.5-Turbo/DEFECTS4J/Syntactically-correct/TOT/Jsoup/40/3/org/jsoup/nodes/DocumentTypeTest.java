
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    // Test for the constructor
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        assertEquals("HTML", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
    }

    // Test for nodeName method
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        assertEquals("#doctype", docType.nodeName());
    }

    // Test for outerHtmlHead method with all attributes
    @Test
    public void testOuterHtmlHeadWithAttributes() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }

    // Test for outerHtmlHead method with missing attributes
    @Test
    public void testOuterHtmlHeadWithoutAttributes() {
        DocumentType docType = new DocumentType("HTML", null, null, "http://example.org");
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE HTML>", sb.toString());
    }

    // Test for outerHtmlTail method
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }
}

