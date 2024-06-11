import org.jsoup.nodes.DocumentType;
import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        documentType.outerHtmlHead(accum, 0, out);

        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" http://www.w3.org/TR/REC-html40/loose.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_NoPublicId() {
        documentType = new DocumentType("html", "", "http://www.w3.org/TR/REC-html40/loose.dtd", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        documentType.outerHtmlHead(accum, 0, out);

        String expected = "<!DOCTYPE html http://www.w3.org/TR/REC-html40/loose.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_NoSystemId() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        documentType.outerHtmlHead(accum, 0, out);

        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_NoPublicIdAndSystemId() {
        documentType = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        documentType.outerHtmlHead(accum, 0, out);

        String expected = "<!DOCTYPE html\">";
        assertEquals(expected, accum.toString());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}