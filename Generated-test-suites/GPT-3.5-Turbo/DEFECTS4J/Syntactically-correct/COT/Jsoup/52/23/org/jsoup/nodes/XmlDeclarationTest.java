
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "example.com", false);
        
        String expectedNodeName = "#declaration";
        String actualNodeName = xmlDeclaration.nodeName();
        
        assertEquals(expectedNodeName, actualNodeName);
    }

    @Test
    public void testGetName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "example.com", false);
        
        String expectedName = "declaration";
        String actualName = xmlDeclaration.name();
        
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetWholeDeclaration_NoAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "example.com", false);
        
        String expectedWholeDeclaration = "declaration";
        String actualWholeDeclaration = xmlDeclaration.getWholeDeclaration();
        
        assertEquals(expectedWholeDeclaration, actualWholeDeclaration);
    }

    @Test
    public void testGetWholeDeclaration_XmlWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "example.com", false);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        
        String expectedWholeDeclaration = "xml version=\"1.0\" encoding=\"UTF-8\"";
        String actualWholeDeclaration = xmlDeclaration.getWholeDeclaration();
        
        assertEquals(expectedWholeDeclaration, actualWholeDeclaration);
    }

    @Test
    public void testOuterHtmlHead_ProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "example.com", true);
        
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("<!").append(xmlDeclaration.getWholeDeclaration()).append(">");
        
        StringBuilder actualHtml = new StringBuilder();
        xmlDeclaration.outerHtmlHead(actualHtml, 0, new Document.OutputSettings());
        
        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
    
    @Test
    public void testOuterHtmlHead_Declaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "example.com", false);
        
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("<?").append(xmlDeclaration.getWholeDeclaration()).append(">");
        
        StringBuilder actualHtml = new StringBuilder();
        xmlDeclaration.outerHtmlHead(actualHtml, 0, new Document.OutputSettings());
        
        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "example.com", false);
        
        StringBuilder expectedHtml = new StringBuilder();
        
        StringBuilder actualHtml = new StringBuilder();
        xmlDeclaration.outerHtmlTail(actualHtml, 0, new Document.OutputSettings());
        
        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "example.com", false);
        
        String expectedToString = xmlDeclaration.outerHtml();
        String actualToString = xmlDeclaration.toString();
        
        assertEquals(expectedToString, actualToString);
    }
}
