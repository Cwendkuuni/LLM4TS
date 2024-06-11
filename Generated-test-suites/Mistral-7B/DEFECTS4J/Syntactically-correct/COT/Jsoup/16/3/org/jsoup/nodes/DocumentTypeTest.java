import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "systemId", "baseUri");
        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, null);

        String expected = "<!DOCTYPE html>\n";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "publicId", "", "baseUri");
        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" >\n";
        assertEquals(expected, accumulator.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType docType = new DocumentType("html", null, null, null);
        docType.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlTail(accumulator, 0, null);

        String expected = "";
        assertEquals(expected, accumulator.toString());
    }
}