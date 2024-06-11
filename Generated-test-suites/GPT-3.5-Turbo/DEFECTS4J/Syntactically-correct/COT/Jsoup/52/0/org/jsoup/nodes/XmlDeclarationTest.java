import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class XmlDeclarationTest {
    
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "http://example.com", false);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "http://example.com", false);
        Assert.assertEquals("test", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "http://example.com", false);
        Assert.assertEquals("test", xmlDeclaration.getWholeDeclaration());
    }
    
    // Edge case: xml declaration with version and encoding attributes
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        xmlDeclaration.addAttribute("version", "1.0");
        xmlDeclaration.addAttribute("encoding", "UTF-8");
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "http://example.com", false);
        Assert.assertEquals("<test>", xmlDeclaration.toString());
    }
}