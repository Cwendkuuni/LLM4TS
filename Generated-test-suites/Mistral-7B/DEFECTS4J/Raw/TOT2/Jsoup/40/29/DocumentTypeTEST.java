import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("publicId", documentType.attr("publicId"));
        // Add more assertions as needed
    }
}