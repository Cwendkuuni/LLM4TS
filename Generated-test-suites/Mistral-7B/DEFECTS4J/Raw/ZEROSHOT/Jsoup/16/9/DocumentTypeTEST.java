import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html");

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html";
        if (!StringUtil.isBlank(docType.attr("publicId"))) {
            expected += " PUBLIC \"";
            expected += docType.attr("publicId");
            expected += "\"";
        }
        if (!StringUtil.isBlank(docType.attr("systemId"))) {
            expected += ' ';
            expected += docType.attr("systemId");
            expected += "\"";
        }
        expected += '>';

        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithChildren() {
        Document doc = new Document();
        Element rootElement = new Element("html");
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        rootElement.appendChild(docType);

        StringBuilder accum = new StringBuilder();
        doc.outputSettings().setPrettyPrint(true);
        doc.outputSettings().setEscapeMode(Document.OutputSettings.Mode.HTML);
        rootElement.outerHtml(accum);

        String expected = "<html>\n<!DOCTYPE html";
        if (!StringUtil.isBlank(docType.attr("publicId"))) {
            expected += " PUBLIC \"";
            expected += docType.attr("publicId");
            expected += "\"";
        }
        if (!StringUtil.isBlank(docType.attr("systemId"))) {
            expected += ' ';
            expected += docType.attr("systemId");
            expected += "\"";
        }
        expected += '>';

        assertEquals(expected, accum.toString());
    }
}