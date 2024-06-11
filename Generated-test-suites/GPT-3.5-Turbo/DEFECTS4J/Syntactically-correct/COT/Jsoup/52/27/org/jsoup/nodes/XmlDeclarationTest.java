
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.jupiter.api.Assertions.*;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration;

    @Test
    public void testNodeName() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);

        // Edge case: Test when the name is "xml" and there are attributes
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        // Edge case: Test when the name is not "xml"
        xmlDeclaration = new XmlDeclaration("other", "http://example.com", false);
        assertEquals("other", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<" + "?xml" + ">", xmlDeclaration.toString());
    }
}
