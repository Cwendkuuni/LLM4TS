
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "", false);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "", false);
        declaration.attributes().put("version", "1.0");
        declaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithNoAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "", false);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws Exception {
        XmlDeclaration declaration = new XmlDeclaration("xml", "", false);
        StringBuilder stringBuilder = new StringBuilder();
        declaration.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<?xml>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadWithAttributes() throws Exception {
        XmlDeclaration declaration = new XmlDeclaration("xml", "", false);
        declaration.attributes().put("version", "1.0");
        declaration.attributes().put("encoding", "UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        declaration.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">", stringBuilder.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "", false);
        assertEquals("<?xml>", declaration.toString());
    }
}
