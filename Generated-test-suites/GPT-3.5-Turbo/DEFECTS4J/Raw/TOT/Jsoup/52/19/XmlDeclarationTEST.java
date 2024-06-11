import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    private XmlDeclaration declaration;
    
    @Before
    public void setup() {
        declaration = new XmlDeclaration("xml", "http://example.com", false);
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testGetName() {
        assertEquals("xml", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration_WithNoAttributes() {
        assertEquals("xml", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclaration_WithVersionAttribute() {
        declaration.addAttribute("version", "1.0");
        assertEquals("xml version=\"1.0\"", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclaration_WithEncodingAttribute() {
        declaration.addAttribute("encoding", "UTF-8");
        assertEquals("xml encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclaration_WithBothAttributes() {
        declaration.addAttribute("version", "1.0");
        declaration.addAttribute("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?xml>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlHead_WithAttributes() {
        declaration.addAttribute("version", "1.0");
        declaration.addAttribute("encoding", "UTF-8");
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">", sb.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("<?xml>", declaration.toString());
    }
}