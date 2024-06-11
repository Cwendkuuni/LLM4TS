
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {
    private DocumentType documentType;

    @Before
    public void setUp() {
        // Create a new DocumentType instance for each test method
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
    }

    @Test
    public void testName() {
        // Test the getter method for name
        assertEquals("doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        // Test the outerHtmlHead method for publicId and systemId being not blank
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBlankPublicId() {
        // Test the outerHtmlHead method for publicId being blank
        documentType.attr("publicId", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBlankSystemId() {
        // Test the outerHtmlHead method for systemId being blank
        documentType.attr("systemId", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBlankPublicAndSystemId() {
        // Test the outerHtmlHead method for both publicId and systemId being blank
        documentType.attr("publicId", "");
        documentType.attr("systemId", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        // Test the outerHtmlTail method
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, outputSettings);
        assertEquals("", accum.toString());
    }
}

