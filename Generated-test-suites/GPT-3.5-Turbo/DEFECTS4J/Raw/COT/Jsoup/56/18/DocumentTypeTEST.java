
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void nodeNameTest() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void outerHtmlHeadTest() throws IOException {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        
        String expected = "<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void outerHtmlTailTest() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        
        assertTrue(stringBuilder.toString().isEmpty());
    }

}
