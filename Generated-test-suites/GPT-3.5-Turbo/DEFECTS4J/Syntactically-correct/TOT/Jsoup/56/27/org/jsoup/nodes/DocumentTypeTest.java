
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(docType);
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testConstructorWithEmptyValues() {
        String name = "";
        String publicId = "";
        String systemId = "";
        String baseUri = "";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(docType);
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testConstructorWithNullValues() {
        String name = null;
        String publicId = null;
        String systemId = null;
        String baseUri = null;

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(docType);
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testHas() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertTrue(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertTrue(docType.has("systemId"));

        assertFalse(docType.has("nonExistentAttribute"));
    }
}

