import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?name>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("<?name>", declaration.toString());
    }
}