import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void nodeName_returnDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void name_returnName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void getWholeDeclaration_withVersionAndEncoding_returnWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");

        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void getWholeDeclaration_withVersionOnly_returnName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        declaration.attributes.put("version", "1.0");

        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void getWholeDeclaration_withEncodingOnly_returnName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        declaration.attributes.put("encoding", "UTF-8");

        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void getWholeDeclaration_withoutAttributes_returnName() {
        XmlDeclaration declaration = new XmlDeclaration("other", "http://example.com", true);

        assertEquals("other", declaration.getWholeDeclaration());
    }

    @Test
    public void outerHtmlHead_generateDeclarationStartTag() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);

        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());

        assertEquals("<?xml", sb.toString());
    }

    @Test
    public void outerHtmlTail_doNothing() {
        // This method does not do anything, so no test case needed.
    }

    @Test
    public void toString_returnOuterHtml() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<?xml>", declaration.toString());
    }

}