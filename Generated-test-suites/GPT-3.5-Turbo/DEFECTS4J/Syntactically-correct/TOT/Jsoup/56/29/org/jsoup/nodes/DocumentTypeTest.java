import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    private DocumentType documentType;
    
    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead_Html5Doctype() {
        DocumentType html5Doctype = new DocumentType("html", null, null, "http://www.example.com");
        String expectedOutput = "<!doctype";
        
        StringBuilder stringBuilder = new StringBuilder();
        try {
            html5Doctype.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        
        assertEquals(expectedOutput, stringBuilder.toString());
    }
    
    @Test
    public void testOuterHtmlHead_NormalDoctype() {
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
        
        StringBuilder stringBuilder = new StringBuilder();
        try {
            documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        
        assertEquals(expectedOutput, stringBuilder.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        String expectedOutput = "";
        
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        
        assertEquals(expectedOutput, stringBuilder.toString());
    }
    
    @Test
    public void testHas_ExistingAttribute() {
        assertTrue(documentType.has(DocumentType.NAME));
    }
    
    @Test
    public void testHas_NonExistingAttribute() {
        assertFalse(documentType.has("nonExistingAttribute"));
    }
}