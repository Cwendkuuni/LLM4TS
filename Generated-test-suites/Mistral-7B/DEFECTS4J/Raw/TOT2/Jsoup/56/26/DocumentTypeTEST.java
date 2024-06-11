import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertNotNull(docType);
        assertEquals("html", docType.nodeName());
    }
}