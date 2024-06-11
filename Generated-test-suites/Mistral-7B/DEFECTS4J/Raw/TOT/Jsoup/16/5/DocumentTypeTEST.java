import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        String name = "#html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }
}