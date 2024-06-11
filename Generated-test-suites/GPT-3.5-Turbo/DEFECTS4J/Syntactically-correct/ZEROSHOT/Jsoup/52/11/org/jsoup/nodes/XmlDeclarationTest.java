import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    private XmlDeclaration declaration;

    @Before
    public void setUp() {
        declaration = new XmlDeclaration("xml", "http://example.com", false);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        String expected = "xml";
        assertEquals(expected, declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlHead(sb, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String expected = "<?xml>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlTail(sb, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String expected = "";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testToString() {
        String expected = "<?xml>";
        assertEquals(expected, declaration.toString());
    }
}