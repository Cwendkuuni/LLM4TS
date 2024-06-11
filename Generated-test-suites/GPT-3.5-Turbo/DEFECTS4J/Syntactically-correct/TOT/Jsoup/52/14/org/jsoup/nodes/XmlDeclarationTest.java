
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com", false);
        assertEquals("declaration", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        // Test for XML declaration with multiple attributes
        XmlDeclaration declaration1 = new XmlDeclaration("xml", "http://example.com", false);
        declaration1.attributes().put("version", "1.0");
        declaration1.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration1.getWholeDeclaration());

        // Test for XML declaration with no attributes
        XmlDeclaration declaration2 = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration2.getWholeDeclaration());

        // Test for non-XML declaration
        XmlDeclaration declaration3 = new XmlDeclaration("nonxml", "http://example.com", false);
        assertEquals("nonxml", declaration3.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<!xml>", declaration.toString());
    }
    
    @Test
    public void testOuterHtmlHead() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com", false);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        declaration.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<?declaration>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com", false);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        declaration.outerHtmlTail(stringBuilder, 0, outputSettings);
        assertEquals("", stringBuilder.toString());
    }
}
