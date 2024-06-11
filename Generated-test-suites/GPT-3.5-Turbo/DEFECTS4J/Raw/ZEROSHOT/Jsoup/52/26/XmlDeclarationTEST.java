import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.*;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
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
        assertEquals("<!xml>", xmlDeclaration.outerHtml());
    }
}