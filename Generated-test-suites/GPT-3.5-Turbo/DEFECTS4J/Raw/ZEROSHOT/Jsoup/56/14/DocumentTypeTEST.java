import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.OutputSettings.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    private DocumentType documentType;
    
    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        OutputSettings outputSettings = new OutputSettings();
        
        try {
            documentType.outerHtmlHead(accum, depth, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        OutputSettings outputSettings = new OutputSettings();
        
        try {
            documentType.outerHtmlTail(accum, depth, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        String expected = "";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHas() {
        boolean actual = documentType.has("name");
        assertTrue(actual);
        
        actual = documentType.has("publicId");
        assertTrue(actual);
        
        actual = documentType.has("systemId");
        assertTrue(actual);
    }
}