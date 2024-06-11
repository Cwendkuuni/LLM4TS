import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "HTML", "HTML5", "http://example.com");
        assertEquals("#doctype", documentType.nodeName());
        assertNotNull(documentType.attr(DocumentType.NAME));
        assertNotNull(documentType.attr(DocumentType.PUBLIC_ID));
        assertNotNull(documentType.attr(DocumentType.SYSTEM_ID));
        assertNotNull(documentType.attr("baseUri"));
    }
}