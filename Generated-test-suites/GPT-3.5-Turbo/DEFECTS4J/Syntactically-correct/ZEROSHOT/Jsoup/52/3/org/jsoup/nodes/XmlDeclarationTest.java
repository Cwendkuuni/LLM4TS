import org.jsoup.helper.Validate;

import java.io.IOException;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        Assert.assertEquals("name", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
        
        xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        Assert.assertEquals("name", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(sb, 0, outputSettings);
        Assert.assertEquals("<!name>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        xmlDeclaration.outerHtmlTail(sb, 0, outputSettings);
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        Assert.assertEquals("<!name>", xmlDeclaration.toString());
    }
}