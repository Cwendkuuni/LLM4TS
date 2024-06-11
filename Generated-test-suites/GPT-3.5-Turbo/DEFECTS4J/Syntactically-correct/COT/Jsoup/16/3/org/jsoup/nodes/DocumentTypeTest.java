import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
  
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("name", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals("baseUri", documentType.baseUri());
    }
  
    @Test
    public void testConstructor_EmptyStrings() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertEquals("", documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testConstructor_NullValues() {
        DocumentType documentType = new DocumentType(null, null, null, null);
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidCharacters() {
        new DocumentType("name<>", "publicId<>", "systemId<>", "baseUri<>");
    }

    // Add more test methods for other public methods in DocumentType class

    // 
  
    // Place the test methods here
  
    // 
}