import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.Validate;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertNotNull(xmlDeclaration.name());
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        assertNotNull(xmlDeclaration.getWholeDeclaration());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtml() {
        assertNotNull(xmlDeclaration.outerHtml());
        assertEquals("<!xml>", xmlDeclaration.outerHtml());
    }

    @Test
    public void testToString() {
        assertNotNull(xmlDeclaration.toString());
        assertEquals("<!xml>", xmlDeclaration.toString());
    }
    
}