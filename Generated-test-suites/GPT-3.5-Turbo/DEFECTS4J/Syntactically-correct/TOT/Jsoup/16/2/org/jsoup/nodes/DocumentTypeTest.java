import org.jsoup.nodes.DocumentType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType doctype;

    @Before
    public void setUp() {
        doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.org");
    }

    @Test
    public void testConstructor() {
        assertEquals("html", doctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr("systemId"));
        assertEquals("http://www.example.org", doctype.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();

        doctype.outerHtmlHead(accum, 0, settings);
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType doctypeEmptyPublicId = new DocumentType("html", "", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.org");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();

        doctypeEmptyPublicId.outerHtmlHead(accum, 0, settings);
        String expectedOutput = "<!DOCTYPE html http://www.w3.org/TR/html4/loose.dtd\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType doctypeEmptySystemId = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "", "http://www.example.org");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();

        doctypeEmptySystemId.outerHtmlHead(accum, 0, settings);
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicAndSystemId() {
        DocumentType doctypeEmptyPublicAndSystemId = new DocumentType("html", "", "", "http://www.example.org");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();

        doctypeEmptyPublicAndSystemId.outerHtmlHead(accum, 0, settings);
        String expectedOutput = "<!DOCTYPE html>";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();

        doctype.outerHtmlTail(accum, 0, settings);
        String expectedOutput = "";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTailWithDepth() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();

        doctype.outerHtmlTail(accum, 2, settings);
        String expectedOutput = "";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTailWithOutputSettings() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings().prettyPrint();

        doctype.outerHtmlTail(accum, 0, settings);
        String expectedOutput = "";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTailWithDepthAndOutputSettings() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings().prettyPrint();

        doctype.outerHtmlTail(accum, 2, settings);
        String expectedOutput = "";
        assertEquals(expectedOutput, accum.toString());
    }
}