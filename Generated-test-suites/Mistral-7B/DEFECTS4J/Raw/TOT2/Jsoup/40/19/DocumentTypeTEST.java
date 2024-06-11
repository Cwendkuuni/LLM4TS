import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        Document doc = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "/baseUri");
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals("/baseUri", documentType.baseUri());
    }
}