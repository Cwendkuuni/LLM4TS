import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseURI";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    // edge cases testing
    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType docType = new DocumentType(null, null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testEmptyNameDocumentTypeCreation() {
        String name = "";
        DocumentType docType = new DocumentType(name, "publicID", "systemID", "baseURI");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("", docType.attr("name"));
        assertEquals("publicID", docType.attr("publicId"));
        assertEquals("systemID", docType.attr("systemId"));
        assertEquals("baseURI", docType.baseUri());
    }

    @Test
    public void testEmptyPublicIdDocumentTypeCreation() {
        DocumentType docType = new DocumentType("name", null, "systemID", "baseURI");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("name", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertEquals("systemID", docType.attr("systemId"));
        assertEquals("baseURI", docType.baseUri());
    }

    @Test
    public void testEmptySystemIdDocumentTypeCreation() {
        DocumentType docType = new DocumentType("name", "publicID", null, "baseURI");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("name", docType.attr("name"));
        assertEquals("publicID", docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals("baseURI", docType.baseUri());
    }

    // exception handling testing
    @Test(expected = IllegalArgumentException.class)
    public void testNullNameDocumentTypeCreation() {
        String name = null;
        DocumentType docType = new DocumentType(name, "publicID", "systemID", "baseURI");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameDocumentTypeCreation() {
        String name = "";
        DocumentType docType = new DocumentType(name, "publicID", "systemID", "baseURI");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicIdDocumentTypeCreation() {
        DocumentType docType = new DocumentType("name", null, "systemID", "baseURI");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPublicIdDocumentTypeCreation() {
        DocumentType docType = new DocumentType("name", "", "systemID", "baseURI");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemIdDocumentTypeCreation() {
        DocumentType docType = new DocumentType("name", "publicID", null, "baseURI");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySystemIdDocumentTypeCreation() {
        DocumentType docType = new DocumentType("name", "publicID", "", "baseURI");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriDocumentTypeCreation() {
        DocumentType docType = new DocumentType("name", "publicID", "systemID", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBaseUriDocumentTypeCreation() {
        DocumentType docType = new DocumentType("name", "publicID", "systemID", "");
    }

}