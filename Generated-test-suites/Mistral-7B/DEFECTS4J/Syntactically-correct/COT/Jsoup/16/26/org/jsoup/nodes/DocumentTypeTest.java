import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicId", documentType.attr("publicId"));
        Assert.assertEquals("systemId", documentType.attr("systemId"));
        Assert.assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        Assert.assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId=\"systemId\">";
        Assert.assertEquals(expected, accum.toString());

        documentType.attr("publicId", "");
        documentType.attr("systemId", "");
        accum.setLength(0);
        documentType.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        Assert.assertEquals("", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameConstructor() {
        new DocumentType(null, "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringNameConstructor() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicIdConstructor() {
        new DocumentType("html", null, "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringPublicIdConstructor() {
        new DocumentType("html", "", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemIdConstructor() {
        new DocumentType("html", "publicId", null, "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringSystemIdConstructor() {
        new DocumentType("html", "publicId", "", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriConstructor() {
        new DocumentType("html", "publicId", "systemId", null);
    }

    @Test
    public void testToString() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId=\"systemId\">";
        Assert.assertEquals(expected, documentType.toString());

        documentType.attr("publicId", "");
        documentType.attr("systemId", "");
        expected = "<!DOCTYPE html>";
        Assert.assertEquals(expected, documentType.toString());
    }
}