import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, Document.OutputSettings.html());
            assertEquals("<?xml?>", sb.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, Document.OutputSettings.html());
            assertEquals("", sb.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<?xml?>", xmlDeclaration.toString());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        new XmlDeclaration(null, "http://example.com", false);
    }
    
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclarationWithInvalidName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("invalidName", "http://example.com", false);
        assertEquals("invalidName", xmlDeclaration.getWholeDeclaration());
    }
    
    // Merge all the individual test cases into a complete JUnit 4 test file (XmlDeclarationTest.java)
    // 
    // ... individual test cases
    // 
}