
import org.junit.Test;
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
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?name>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("<?name>", xmlDeclaration.toString());
    }
    
}
