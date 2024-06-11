import org.jsoup.nodes.DocumentType;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        String name = "doctype";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://www.example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Assert.assertEquals(name, documentType.attr("name"));
        Assert.assertEquals(publicId, documentType.attr("publicId"));
        Assert.assertEquals(systemId, documentType.attr("systemId"));
        Assert.assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", null, null, null);

        Assert.assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        String name = "doctype";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        DocumentType documentType = new DocumentType(name, publicId, systemId, null);

        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, null);

        Assert.assertEquals("<!DOCTYPE " + name + " PUBLIC \"" + publicId + "\" \"" + systemId + "\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype", null, null, null);

        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, null);

        Assert.assertEquals("", stringBuilder.toString());
    }
}