import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setup() {
        xmlDeclaration = new XmlDeclaration("xml", "https://example.com", true);
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
    public void testOuterHtml() {
        String expectedOutput = "<!xml>";
        assertEquals(expectedOutput, xmlDeclaration.outerHtml());
    }

    @Test
    public void testToString() {
        String expectedOutput = "<!xml>";
        assertEquals(expectedOutput, xmlDeclaration.toString());
    }
}