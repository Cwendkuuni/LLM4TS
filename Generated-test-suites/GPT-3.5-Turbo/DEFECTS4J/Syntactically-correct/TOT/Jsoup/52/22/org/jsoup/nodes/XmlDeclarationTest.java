import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testConstructorValid() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration.name());
        assertFalse(declaration.isProcessingInstruction());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorInvalidName() {
        new XmlDeclaration(null, "http://example.com", false);
    }

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testGetWholeDeclarationValid() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationNoAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationInvalidName() {
        XmlDeclaration declaration = new XmlDeclaration("x<!", "http://example.com", true);
        assertEquals("x<!", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder builder = new StringBuilder();
        try {
            declaration.outerHtmlHead(builder, 0, null);
        } catch (Exception e) {
            // do nothing
        }
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">", builder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder builder = new StringBuilder();
        try {
            declaration.outerHtmlTail(builder, 0, null);
        } catch (Exception e) {
            // do nothing
        }
        assertEquals("", builder.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">", declaration.toString());
    }

    @Test
    public void testIsProcessingInstructionTrue() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertTrue(declaration.isProcessingInstruction());
    }

    @Test
    public void testIsProcessingInstructionFalse() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertFalse(declaration.isProcessingInstruction());
    }
}