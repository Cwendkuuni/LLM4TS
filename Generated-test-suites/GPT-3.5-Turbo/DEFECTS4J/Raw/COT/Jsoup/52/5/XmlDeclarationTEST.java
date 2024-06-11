import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = mock(Document.OutputSettings.class);

        try {
            xmlDeclaration.outerHtmlHead(accum, depth, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("<?xml", accum.toString());
        verify(out, never()).prettyPrint();
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = mock(Document.OutputSettings.class);

        xmlDeclaration.outerHtmlTail(accum, depth, out);

        assertEquals("", accum.toString());
        verify(out, never()).prettyPrint();
    }

    @Test
    public void testToString() {
        assertEquals("<?xml>", xmlDeclaration.toString());
    }
}