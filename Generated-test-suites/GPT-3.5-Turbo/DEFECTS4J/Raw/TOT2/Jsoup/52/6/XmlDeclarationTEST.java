import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "http://example.com", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "http://example.com", false);
        assertEquals("test", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("test", "http://example.com", false);
        assertEquals("test", xmlDeclaration2.getWholeDeclaration());
    }

    // Additional edge cases and error scenario tests can be added here

}