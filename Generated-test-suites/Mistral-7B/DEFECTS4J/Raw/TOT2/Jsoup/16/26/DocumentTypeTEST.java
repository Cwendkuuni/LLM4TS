import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("publicId1", documentType.attr("publicId"));
        assertEquals("systemId1", documentType.attr("systemId"));
        assertEquals("baseUri1", documentType.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructionWithNullName() {
        new DocumentType(null, "publicId", "systemId", "baseUri");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructionWithNullPublicId() {
        new DocumentType("name", null, "systemId", "baseUri");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructionWithNullSystemId() {
        new DocumentType("name", "publicId", null, "baseUri");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructionWithNullBaseUri() {
        new DocumentType("name", "publicId", "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructionWithEmptyName() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructionWithEmptyPublicId() {
        new DocumentType("name", "", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructionWithEmptySystemId() {
        new DocumentType("name", "publicId", "", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructionWithEmptyBaseUri() {
        new DocumentType("name", "publicId", "systemId", "");
    }
}

//