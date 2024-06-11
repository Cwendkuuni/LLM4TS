import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com/");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testConstructorWithValidValues() {
        assertEquals("doctype", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", documentType.attr("systemId"));
        assertEquals("https://www.example.com/", documentType.baseUri());
    }

    @Test
    public void testConstructorWithNullValues() {
        DocumentType documentType = new DocumentType(null, null, null, null);
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdSystemId() {
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" http://www.w3.org/TR/REC-html40/strict.dtd\">";
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithOnlyPublicId() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.0//EN", null, "https://www.example.com/");
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">";
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithOnlySystemId() {
        documentType = new DocumentType("doctype", null, "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com/");
        String expectedOutput = "<!DOCTYPE html http://www.w3.org/TR/REC-html40/strict.dtd\">";
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoPublicIdAndSystemId() {
        documentType = new DocumentType("doctype", null, null, "https://www.example.com/");
        String expectedOutput = "<!DOCTYPE html\">";
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyAttributes() {
        documentType = new DocumentType("", "", "", "");
        String expectedOutput = "<!DOCTYPE html\">";
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

}