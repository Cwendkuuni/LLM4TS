import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    private DocumentType docType;
    
    @Before
    public void setUp() {
        docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expectedOutput, docType.outerHtml());
    }
    
    @Test
    public void testOuterHtmlTail() {
        String expectedOutput = "";
        assertEquals(expectedOutput, docType.outerHtmlTail());
    }
    
    @Test
    public void testHas_whenAttributeExists() {
        assertTrue(docType.has("name"));
    }
    
    @Test
    public void testHas_whenAttributeDoesNotExist() {
        assertFalse(docType.has("version"));
    }
}