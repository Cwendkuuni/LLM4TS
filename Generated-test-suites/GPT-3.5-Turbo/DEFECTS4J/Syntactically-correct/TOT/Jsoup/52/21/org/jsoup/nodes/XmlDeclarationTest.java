import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, null);
        assertEquals("<?xml", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<?xml>", xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        new XmlDeclaration(null, "http://example.com", false);
    }
}