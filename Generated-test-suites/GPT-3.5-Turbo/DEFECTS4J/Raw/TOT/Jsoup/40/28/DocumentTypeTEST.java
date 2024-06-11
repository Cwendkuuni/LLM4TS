
import org.jsoup.nodes.DocumentType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    private DocumentType doctype;

    @Before
    public void setUp() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";

        doctype = new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        doctype.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNameOnly() {
        DocumentType doctypeWithNameOnly = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        doctypeWithNameOnly.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE html>";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType doctypeWithEmptyPublicId = new DocumentType("html", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", null);

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        doctypeWithEmptyPublicId.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType doctypeWithEmptySystemId = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "", null);

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        doctypeWithEmptySystemId.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoAttributes() {
        DocumentType doctypeWithNoAttributes = new DocumentType(null, null, null, null);

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        doctypeWithNoAttributes.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE>";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        doctype.outerHtmlTail(accum, depth, out);

        String expectedOutput = "";
        assertEquals(expectedOutput, accum.toString());
    }
}
