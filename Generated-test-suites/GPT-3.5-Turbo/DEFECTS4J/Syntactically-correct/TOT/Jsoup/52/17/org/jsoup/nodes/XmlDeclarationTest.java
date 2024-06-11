import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testXmlNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclarationForXMLDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationForNonXMLDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "http://example.com", false);
        assertEquals("test", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHeadForProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadForNonProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", sb.toString());
    }
  
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
    }
}