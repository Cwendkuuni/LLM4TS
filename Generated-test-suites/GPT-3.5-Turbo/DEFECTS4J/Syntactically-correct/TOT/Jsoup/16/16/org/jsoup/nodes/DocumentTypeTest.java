import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {
    
    private DocumentType documentType;
    
    @Before
    public void setup() {
        documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "https://www.example.com");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        documentType.attr("publicId", "");
        
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithoutSystemId() {
        documentType.attr("systemId", "");
        
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">");
        
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder expected = new StringBuilder();
        // No modifications to the StringBuilder expected since the method is empty
        
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlTail(actual, 0, null);
        
        assertEquals(expected.toString(), actual.toString());
    }
}