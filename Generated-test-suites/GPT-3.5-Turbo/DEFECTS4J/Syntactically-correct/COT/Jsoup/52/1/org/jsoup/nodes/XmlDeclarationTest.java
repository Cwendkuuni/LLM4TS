import org.jsoup.helper.Validate;
import org.jsoup.nodes.Node;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        assertEquals("declaration", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration_WithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclaration_WithoutAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("element", "baseUri", false);
        assertEquals("element", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        StringBuilder acum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            declaration.outerHtmlHead(acum, 0, outputSettings);
        } catch (IOException e) {
            fail("IOException was thrown.");
        }
        assertEquals("<?declaration>", acum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        StringBuilder acum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        declaration.outerHtmlTail(acum, 0, outputSettings);
        assertEquals("", acum.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        assertEquals("<?declaration>", declaration.toString());
    }

}