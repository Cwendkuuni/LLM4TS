import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        documentType.attr("publicId", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutSystemId() {
        documentType.attr("systemId", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}