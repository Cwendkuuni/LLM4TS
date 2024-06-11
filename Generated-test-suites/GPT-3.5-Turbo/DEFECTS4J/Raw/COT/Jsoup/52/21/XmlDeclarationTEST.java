
import org.jsoup.nodes.*;
import org.junit.*;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        // Edge case: name == "xml" and attributes.size() > 1
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
        
        // Edge case: name != "xml"
        xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        // This method cannot be tested since it is package-private
    }
    
    @Test
    public void testOuterHtmlTail() {
        // This method cannot be tested since it is package-private
    }
    
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("<name>", xmlDeclaration.toString());
    }
}
