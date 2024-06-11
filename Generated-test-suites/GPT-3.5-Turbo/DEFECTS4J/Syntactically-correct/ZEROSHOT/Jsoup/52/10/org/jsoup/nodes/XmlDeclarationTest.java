import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {
    
    private XmlDeclaration xmlDeclaration;
    
    @Before
    public void setup() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
    }
    
    @Test
    public void testNodeName() {
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        Assert.assertEquals("xml", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        Assert.assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
        Assert.assertEquals("<!xml>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
        Assert.assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("<!xml>", xmlDeclaration.toString());
    }
    
    // Additional test cases can be added to further test the methods in the XmlDeclaration class
    
}