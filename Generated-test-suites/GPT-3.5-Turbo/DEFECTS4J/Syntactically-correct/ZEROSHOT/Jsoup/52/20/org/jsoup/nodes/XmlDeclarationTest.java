import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com/xml", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com/xml", false);
        assertEquals("xml", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com/xml", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com/xml", false);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?xml>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com/xml", false);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com/xml", false);
        assertEquals("<?xml>", xmlDeclaration.toString());
    }
    
}