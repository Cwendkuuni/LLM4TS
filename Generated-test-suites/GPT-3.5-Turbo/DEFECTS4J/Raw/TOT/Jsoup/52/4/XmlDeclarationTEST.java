import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("<")
                .append("?")
                .append(declaration.getWholeDeclaration())
                .append(">");
        StringBuilder accum = new StringBuilder();
        try {
            declaration.outerHtmlHead(accum, 0, null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(expectedOutput.toString(), accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder expectedOutput = new StringBuilder();
        StringBuilder accum = new StringBuilder();
        try {
            declaration.outerHtmlTail(accum, 0, null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(expectedOutput.toString(), accum.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("<")
                .append("?")
                .append(declaration.getWholeDeclaration())
                .append(">");
        assertEquals(expectedOutput.toString(), declaration.toString());
    }

}